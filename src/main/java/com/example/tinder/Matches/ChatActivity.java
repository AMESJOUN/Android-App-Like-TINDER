package com.example.tinder.Matches;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;

    private EditText mSendEditText;
    private Button mSendButton;

    private String CurrentUserId,matchId,chatId;

    DatabaseReference mDatabaseUser,mDatabaseChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");


        CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId).child("connections").child("matches").child(matchId).child("chatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();



        mRecycleView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setHasFixedSize(false);
        mMatchesLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecycleView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(),ChatActivity.this);
        mRecycleView.setAdapter(mMatchesAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton =findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void sendMessage(){

        String sendMessageText =  mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createByUser",CurrentUserId);
            newMessage.put("text",sendMessageText);

            newMessageDb.setValue(newMessage);


        }
        mSendEditText.setText(null);
    }


    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId = snapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists()){
                    String message=null;
                    String createByUser=null;

                    if(snapshot.child("text").getValue()!=null){
                        message =snapshot.child("text").getValue().toString();
                    }

                    if(snapshot.child("createByUser").getValue()!=null){
                        createByUser=snapshot.child("createByUser").getValue().toString();
                    }

                    if(message!=null && createByUser != null){
                        Boolean currentUserBoolean=false;
                        if(createByUser.equals(CurrentUserId)){
                            currentUserBoolean=true;

                        }

                        MatchesObject newMessage= new MatchesObject(message,currentUserBoolean);
                        resultsMatches.add(newMessage);
                        mMatchesAdapter.notifyDataSetChanged();

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


    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getDataSetMatches() {

        return resultsMatches;
    }
}