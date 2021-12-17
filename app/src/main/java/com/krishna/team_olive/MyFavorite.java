package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFavorite extends AppCompatActivity {

    RecyclerView recyclerview_fav;
    FavoriteAdapter favoriteAdapter;
    List<AddedItemDescriptionModel> addedItemDescriptionModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        recyclerview_fav = findViewById(R.id.recyclerview_fav);
        recyclerview_fav.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerview_fav.setLayoutManager(linearLayoutManager);

        addedItemDescriptionModelList = new ArrayList<>();

        favoriteAdapter = new FavoriteAdapter(addedItemDescriptionModelList,this);
        recyclerview_fav.setAdapter(favoriteAdapter);

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
                            favoriteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                favoriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}