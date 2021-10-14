package com.krishna.team_olive;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.messages.MessagesAdapter;
import com.krishna.team_olive.messages.MessagesList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class FragmentChat extends Fragment {
    FirebaseAuth auth;
    RecyclerView rvMessageList;
    DatabaseReference refrence, refrence_2;
    public String name, uid,  chatkey;
    public int unseenMessage;
    public  String lastmessage;
    ArrayList<MessagesList> listOfPersons;
    ArrayList<String> UIDS;
    MessagesAdapter messageAdapter;
    TextView tv;




    public FragmentChat() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        listOfPersons = new ArrayList<MessagesList>();
        UIDS = new ArrayList<String>();
        listOfPersons.clear();
        rvMessageList = v.findViewById(R.id.messagesRecyclerView);
        rvMessageList.setHasFixedSize(true);
        rvMessageList.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessagesAdapter(listOfPersons, getContext());
        rvMessageList.setAdapter(messageAdapter);
        refrence=FirebaseDatabase.getInstance().getReference().child("persons for chatting").child(FirebaseAuth.getInstance().getUid());
        refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfPersons.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                  name=dataSnapshot.child("name").getValue(String.class);
                  uid=dataSnapshot.child("uid").getValue(String.class);

                    MessagesList ml=new MessagesList(name,uid,lastmessage,0);
                    listOfPersons.add(ml);
                }
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected")
                .updateChildren(State);*/
        return v;
    }



}