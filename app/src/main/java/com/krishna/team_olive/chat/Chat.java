package com.krishna.team_olive.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.AdapterClasses.ChatAdapter;
import com.krishna.team_olive.R;

import java.util.ArrayList;
import java.util.Date;

public class Chat extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    RecyclerView rvChat;
    TextView tv_name;
    ImageView btn_send;
    EditText message;
    ImageView bk_btn;
    String receiveId;
    static boolean active=false;
    TextView tv_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        String senderId=auth.getUid();
        receiveId =getIntent().getStringExtra("uid");
        String username= getIntent().getStringExtra("name");
        tv_name=findViewById(R.id.name);
        btn_send=findViewById(R.id.sendBtn);
        rvChat=findViewById(R.id.chattingRecyclerView);
        tv_name.setText(username);
        tv_status=findViewById(R.id.tv_status);
        active=true;

        FirebaseDatabase.getInstance().getReference().child("activitystate").child(senderId+receiveId).child("state").setValue(active);

        FirebaseDatabase.getInstance().getReference().child("unseen").child(senderId+receiveId).child("count").setValue(0);
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
                // FirebaseDatabase.getInstance().getReference().child("lastconnected").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+receiveId).setValue(new Date().getTime());
      /* HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected")
                .updateChildren(State);*/
                finish();
            }
        });
        final ArrayList<ChatList> messageModels=new ArrayList<>();
        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this);
        rvChat.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        // layoutManager.setReverseLayout(true);
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
                rvChat.smoothScrollToPosition(messageModels.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       /* database.getReference().child("chats").child(SenderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                   // ChatList temp=dataSnapshot.getValue(ChatList.class);

                    database.getReference().child("chats").child(SenderRoom).child(dataSnapshot.getKey()).child("isSeen").setValue("1");
                    Toast.makeText(Chat.this, "updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageToSend=message.getText().toString();
                final ChatList cl=new ChatList(senderId,messageToSend);
                cl.setTimeStamp(new Date().getTime());

                cl.isSeen="1";
                ChatList cl2=new ChatList(senderId,messageToSend);
                cl2.setTimeStamp(cl.getTimeStamp());
                cl2.isSeen="0";
                message.setText("");

                Toast.makeText(Chat.this, cl2.getIsSeen(), Toast.LENGTH_SHORT).show();



                FirebaseDatabase.getInstance().getReference().child("lastMessage").child(SenderRoom).child("lastmessage").setValue(messageToSend);
                FirebaseDatabase.getInstance().getReference().child("lastMessage").child(ReceiverRoom).child("lastmessage").setValue(messageToSend);
      /*  database.getReference().child("activitystate").child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class)) {

                    database.getReference().child("unseen").child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").setValue(0);
                } else {
                    database.getReference().child("unseen").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("count")) {
                                int a = snapshot.child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").getValue(Integer.class);
                                a = a + 1;
                                database.getReference().child("unseen").child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").setValue(a);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


                database.getReference().child("chats").child(SenderRoom).push().setValue(cl).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

                database.getReference().child("chats").child(ReceiverRoom).push().setValue(cl2).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Chat.this, "kjgugy", Toast.LENGTH_SHORT).show();
                    }
                });




                database.getReference().child("activitystate").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(receiveId+FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Boolean x = snapshot.child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("state").getValue(Boolean.class);

                            if (x) {
                                //Toast.makeText(Chat.this, "came here", Toast.LENGTH_SHORT).show();
                                database.getReference().child("unseen").child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").setValue(0);
                            } else {

                                database.getReference().child("unseen").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        //  if(snapshot.child(receiveId+FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        //Toast.makeText(Chat.this, "came here", Toast.LENGTH_SHORT).show();
                                        int a = snapshot.child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").getValue(Integer.class);
                                        a = a + 1;
                                        database.getReference().child("unseen").child(receiveId + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("count").setValue(a);
                                        // }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        else
                        {
                            FirebaseDatabase.getInstance().getReference().child("activitystate").child(receiveId+senderId).child("state").setValue(false);
                            FirebaseDatabase.getInstance().getReference().child("unseen").child(receiveId+senderId).child("count").setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference().child("lastconnected").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+receiveId).setValue(new Date().getTime());
        active=false;
        FirebaseDatabase.getInstance().getReference().child("activitystate").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+receiveId).child("state").setValue(active);
      /*  HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected")
                .updateChildren(State);*/
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference().child("lastconnected").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+receiveId).setValue(new Date().getTime());
       /* HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected")
                .updateChildren(State);
    }*/
}