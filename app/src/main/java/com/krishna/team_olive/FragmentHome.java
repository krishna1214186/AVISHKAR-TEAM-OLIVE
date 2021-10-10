package com.krishna.team_olive;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {

    private RecyclerView recyclerView_posts;
    private PostAdapter postAdapter;
    public static ArrayList<AddedItemDescriptionModel> addedItemDescriptionModelArrayList;
    FirebaseDatabase firebaseDatabase ;
    String isNGO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView_posts = view.findViewById(R.id.recyclerview_posts);
        recyclerView_posts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView_posts.setLayoutManager(linearLayoutManager);
        addedItemDescriptionModelArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),addedItemDescriptionModelArrayList);
        recyclerView_posts.setAdapter(postAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child("isNGO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    isNGO = snapshot.getKey();
                }
            }
            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(isNGO == "Y"){
            showNGOposts();
        }else{
            showNormalPosts();
        }


        return view;
    }

    private void showNGOposts() {
        FirebaseDatabase.getInstance().getReference().child("NGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object = snapshot.getValue(AddedItemDescriptionModel.class);
                    addedItemDescriptionModelArrayList.add(object);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showNormalPosts(){
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object = snapshot.getValue(AddedItemDescriptionModel.class);
                    addedItemDescriptionModelArrayList.add(object);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}