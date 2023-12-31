package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.example.tinder.Cards.arrayAdapter;
import com.example.tinder.Cards.cards;
import com.example.tinder.Matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private cards cards_data[];

    private com.example.tinder.Cards.arrayAdapter arrayAdapter;
    private int i;

  /*  @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;
*/

    private FirebaseAuth mAuth;
    private String currentUId;
        private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* ButterKnife.inject(this);*/

        usersDb=FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth =FirebaseAuth.getInstance();
        currentUId=mAuth.getCurrentUser().getUid();


        checkUserSex();
        rowItems = new ArrayList<cards>();

       /* al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");
*/
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView ) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

                cards obj =(cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                isConnectionMatche(userId);
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                cards obj =(cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
               /* al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;*/
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatche(String userId) {
    DatabaseReference  currenUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
    currenUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()){
            Toast.makeText(MainActivity.this,"new Connection",Toast.LENGTH_SHORT).show();

            String key =FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();


            usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
            usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("ChatId").setValue(key);
        }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });

    }

    private String userSex;
    private String oppositeUserSex;

    public void checkUserSex(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb= usersDb.child(user.getUid());
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.getKey().equals(user.getUid())){
                    if(snapshot.exists()){
                        if(snapshot.child("sex") != null){
                            userSex=snapshot.child("sex").getValue().toString();
                            oppositeUserSex="Female";

                            switch (userSex){
                                case "Male":
                                    oppositeUserSex ="Female";
                                    break;
                                case "Female":
                                    oppositeUserSex ="Male";
                            }
                            getOppositeUserSex();
                        }
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void getOppositeUserSex(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                if (dataSnapshot.child("sex").getValue() != null) {
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)) {

                        String profileImageurl = "default";
                        if (dataSnapshot.child("profileImageurl").getKey().equals("default")) {

                            profileImageurl = dataSnapshot.child("profileImageurl").getKey().toString();

                        }
                        cards Item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getKey().toString(), profileImageurl);

                        rowItems.add(Item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void logOutUser(View view) {
        mAuth.signOut();
        Intent intent =new Intent(MainActivity.this,chooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToSettings(View view) {
        Intent intent =new Intent(MainActivity.this,SettingsActivity.class);

        startActivity(intent);
        return;

    }

    public void goToMatches(View view) {
        Intent intent =new Intent(MainActivity.this, MatchesActivity.class);

        startActivity(intent);
        return;

    }
}