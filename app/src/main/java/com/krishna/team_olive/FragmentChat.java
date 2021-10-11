package com.krishna.team_olive;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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


public class FragmentChat extends Fragment {
    FirebaseAuth auth;

    DatabaseReference refrence,refrence_2;
    String name,uid,lastmessage,chatkey;
    int unseenMessage;
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
       // tv=v.findViewById(R.id.tv_try);
       refrence= FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("persons for chatting");

       refrence.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot : snapshot.getChildren())
               {
                   //uid=dataSnapshot.child(dataSnapshot.getKey()).getValue(String.class);
                   //Toast.makeText(getContext(), uid, Toast.LENGTH_SHORT).show();
                   uid=dataSnapshot.child("uid").getValue(String.class);
                   //uid=dataSnapshot.getKey();
                   //tv.setText(uid);

                   refrence_2=FirebaseDatabase.getInstance().getReference().child("users");
                   refrence_2.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           for(DataSnapshot dataSnapshot2 : snapshot.getChildren())
                           {
                               if(uid.equals(dataSnapshot2.child("uid").getValue(String.class)))
                               {
                                  name=dataSnapshot.child("name").getValue(String.class);

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
       lastmessage="";
       unseenMessage=0;
       

        return v;

    }
}