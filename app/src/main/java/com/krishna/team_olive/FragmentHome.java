package com.krishna.team_olive;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {

    int SEARCH_RETURN = 01;
    private RecyclerView recyclerView_posts;
    private PostAdapter postAdapter, postAdapter_search;
    public static ArrayList<AddedItemDescriptionModel> addedItemDescriptionModelArrayList, addedItemDescriptionModelArrayList_search;
    public ArrayList<String> arrayListString;
    FirebaseDatabase firebaseDatabase ;
    RelativeLayout search_bar;
    String isNGO ;
    searchselected activity;
    FirebaseAuth auth;

    public interface searchselected{
        void onsearchselected();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=(FragmentHome.searchselected) context;
    }

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


        search_bar = (RelativeLayout) view.findViewById(R.id.rl_search_bar);
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase fd;

        FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(snapshot.getKey() == "isNGO"){
                        isNGO = snapshot.getValue().toString();
                    }
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

        postAdapter = new PostAdapter(getContext(),addedItemDescriptionModelArrayList);
        recyclerView_posts.setAdapter(postAdapter);

        return view;
    }

    private void showNormalPosts(){
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object ;
                    object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    addedItemDescriptionModelArrayList.add(object);
                }
               postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

/*
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                    }
                }
            });

 */
}