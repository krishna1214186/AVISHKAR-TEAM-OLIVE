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


public class FragmentChat extends Fragment {
    FirebaseAuth auth;
RecyclerView rvMessageList;
    DatabaseReference refrence,refrence_2;
   public String name,uid,lastmessage,chatkey;
  public  int unseenMessage;
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
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment
        auth=FirebaseAuth.getInstance();
        listOfPersons=new ArrayList<MessagesList>();
        UIDS=new ArrayList<String>();
        listOfPersons.clear();
        rvMessageList=v.findViewById(R.id.messagesRecyclerView);
        rvMessageList.setHasFixedSize(true);
        rvMessageList.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter=new MessagesAdapter(listOfPersons);
        rvMessageList.setAdapter(messageAdapter);
        refrence= FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("persons for chatting");
        refrence_2=FirebaseDatabase.getInstance().getReference().child("users");
tv=v.findViewById(R.id.tv_try);
       refrence.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot : snapshot.getChildren())
               {

                   uid=dataSnapshot.child("uid").getValue(String.class);
                  // UIDS.add(uid);
                 //  if(uid.equals("w4HosIHFauOBFn35J7ZtlsZ3my73"))
                     //  Toast.makeText(getContext(), "Anuj", Toast.LENGTH_SHORT).show();

                   refrence_2.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot2) {
                           for(DataSnapshot dataSnapshot2 : snapshot2.getChildren())
                           {
                               if(uid.equals(dataSnapshot2.child("uid").getValue(String.class)))
                               {
                                  name=dataSnapshot2.child("name").getValue(String.class);
                                  // if(name.equals("Anuj"))
                                        //Toast.makeText(getContext(), "yash", Toast.LENGTH_SHORT).show();

                                  lastmessage="";
                                  unseenMessage=0;
                                  MessagesList ml=new MessagesList(name,uid,lastmessage,0,"1");

                                  listOfPersons.add(ml);
                                  //tv.setText(listOfPersons.get(0).getName());
                                   messageAdapter.updateData(listOfPersons);
                                   break;
                               }
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

       });
      /* for(int i=0;i<UIDS.size();i++) {
           uid=UIDS.get(i);
           refrence_2.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                       if (uid.equals(dataSnapshot2.child("uid").getValue(String.class))) {
                           name = dataSnapshot2.child("name").getValue(String.class);
                          // if (name.equals("Anuj"))
                               //Toast.makeText(getContext(), "yash", Toast.LENGTH_SHORT).show();

                           lastmessage = "";
                           unseenMessage = 0;
                           MessagesList ml = new MessagesList(name, uid, lastmessage, 0, "1");

                           listOfPersons.add(ml);
                           //tv.setText(listOfPersons.get(0).getName());
                           messageAdapter.updateData(listOfPersons);
                           break;
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }*/






        return v;

    }
}