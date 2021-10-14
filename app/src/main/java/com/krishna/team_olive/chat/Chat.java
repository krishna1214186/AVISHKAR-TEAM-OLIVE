package com.krishna.team_olive.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Chat extends AppCompatActivity {
FirebaseDatabase database;
FirebaseAuth auth;
RecyclerView rvChat;
TextView tv_name;
ImageView btn_send;
EditText message;
ImageView bk_btn;
TextView tv_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        String senderId=auth.getUid();
        String receiveId=getIntent().getStringExtra("uid");
        String username= getIntent().getStringExtra("name");
        tv_name=findViewById(R.id.name);
        btn_send=findViewById(R.id.sendBtn);
        rvChat=findViewById(R.id.chattingRecyclerView);
        tv_name.setText(username);
        tv_status=findViewById(R.id.tv_status);
        FirebaseDatabase.getInstance().getReference().child("connections").child(receiveId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected=snapshot.getValue(Boolean.class);
                if(connected)
                    tv_status.setText("online");
                else
                    tv_status.setText("offline");
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
bk_btn=findViewById(R.id.backBtn);
message=findViewById(R.id.messageEditTxt);

bk_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected")
                .updateChildren(State);
        finish();
    }
});
final ArrayList<ChatList> messageModels=new ArrayList<>();
final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this);
rvChat.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rvChat.setLayoutManager(layoutManager);
        final String SenderRoom=senderId+receiveId;
        final String ReceiverRoom=receiveId+senderId;

        database.getReference().child("chats").child(SenderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    ChatList chats=dataSnapshot.getValue(ChatList.class);
                    messageModels.add(chats);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

btn_send.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String messageToSend=message.getText().toString();
        final ChatList cl=new ChatList(senderId,messageToSend);
        cl.setTimeStamp(new Date().getTime());
        message.setText("");


        database.getReference().child("chats").child(SenderRoom).push().setValue(cl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                database.getReference().child("chats").child(ReceiverRoom).push().setValue(cl).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });
    }
});


    }

    @Override
    protected void onStop() {
        super.onStop();
        HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected")
                .updateChildren(State);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected")
                .updateChildren(State);
    }
}