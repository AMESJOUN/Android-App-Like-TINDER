package com.example.tinder.Matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private String CurrentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mRecycleView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecycleView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(),MatchesActivity.this);
        mRecycleView.setAdapter(mMatchesAdapter);


        getUserMatchId();







    }

    private void getUserMatchId() {
        DatabaseReference matchdb= FirebaseDatabase.getInstance().getReference().child("Users").child("CurrentUserId").child("connections").child("matches");
        matchdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key){
        DatabaseReference userdb= FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userId= snapshot.getKey();
                    String name="";
                    String profileImageUrl="";

                    if(snapshot.child("name").getValue() != null){
                        name = snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("profileImageUrl").getValue() != null){
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    MatchesObject obj = new MatchesObject();
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getDataSetMatches() {

        return resultsMatches;
    }
}