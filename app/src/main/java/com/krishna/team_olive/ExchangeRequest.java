package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRequest extends AppCompatActivity {

    private RecyclerView recyclerView_exchange_req;
    ExchangeRequestAdapter exchangeRequestAdapter;
    List<ExchangeModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_request);

        recyclerView_exchange_req = findViewById(R.id.recyclerview_exchange_req);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_exchange_req.setLayoutManager(linearLayoutManager);

        list = new ArrayList<>();

        exchangeRequestAdapter = new ExchangeRequestAdapter(this, list);
        recyclerView_exchange_req.setAdapter(exchangeRequestAdapter);

        FirebaseDatabase.getInstance().getReference().child("Exchange Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ExchangeModel exchangeModel = dataSnapshot.getValue(ExchangeModel.class);
                    list.add(exchangeModel);
                }
                exchangeRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}