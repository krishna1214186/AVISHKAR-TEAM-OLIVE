package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {


    private RecyclerView recyclerView_search;
    private EditText searchbar2;
    private List<AddedItemDescriptionModel> search_list;
    private SearchAdapter searchAdapter;
    private FirebaseAuth auth;
    private String isNGO;
    CheckBox check_ratings, check_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        auth = FirebaseAuth.getInstance();

        recyclerView_search = findViewById(R.id.recyclerview_search);
        searchbar2 = findViewById(R.id.search_bar2);
        check_distance = findViewById(R.id.check_distance);
        check_ratings = findViewById(R.id.check_ratings);

        recyclerView_search.setHasFixedSize(true);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        search_list = new ArrayList<>();
        searchAdapter = new SearchAdapter(SearchActivity.this,search_list,true);
        recyclerView_search.setAdapter(searchAdapter);

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
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(isNGO == "Y"){
            searchNGOposts();
        }else{
            searchnonNGOposts();
        }

        searchbar2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchdetailnonNGO(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        check_ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_ratings.isChecked()){
                    
                }
            }
        });
    }

        private void searchdetailnonNGO(String s){
        Query query = FirebaseDatabase.getInstance().getReference().child("nonNGOposts").orderByChild("name").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                search_list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel objes = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    search_list.add(objes);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }

        private void searchnonNGOposts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("nonNGOposts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                search_list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel obje = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    search_list.add(obje);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        private void searchNGOposts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NGOposts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                search_list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel obje = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    search_list.add(obje);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}