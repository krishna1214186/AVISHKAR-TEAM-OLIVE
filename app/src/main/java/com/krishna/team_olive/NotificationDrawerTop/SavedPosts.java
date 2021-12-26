package com.krishna.team_olive.NotificationDrawerTop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.MainActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.R;
import com.krishna.team_olive.AdapterClasses.SavedPostsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavedPosts extends AppCompatActivity {

    ImageView iv_back;

    RecyclerView recyclerview_fav;
    SavedPostsAdapter savedPostsAdapter;
    List<AddedItemDescriptionModel> addedItemDescriptionModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedPosts.this, MainActivity.class));
            }
        });

        recyclerview_fav = findViewById(R.id.recyclerview_fav);
        recyclerview_fav.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerview_fav.setLayoutManager(linearLayoutManager);

        addedItemDescriptionModelList = new ArrayList<>();

        savedPostsAdapter = new SavedPostsAdapter(addedItemDescriptionModelList,this);
        recyclerview_fav.setAdapter(savedPostsAdapter);

        FirebaseDatabase.getInstance().getReference().child("MyLikes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String post_id = dataSnapshot.getKey().toString();
                    FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(post_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AddedItemDescriptionModel addedItemDescriptionModel = snapshot.getValue(AddedItemDescriptionModel.class);
                            addedItemDescriptionModelList.add(addedItemDescriptionModel);
                            savedPostsAdapter.notifyDataSetChanged();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                savedPostsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}