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
import com.krishna.team_olive.AdapterClasses.ExchangeRequestAdapter;
import com.krishna.team_olive.MainActivity;
import com.krishna.team_olive.ModelClasses.ExchangeModel;
import com.krishna.team_olive.R;

import java.util.ArrayList;
import java.util.List;

//Activity for the request that comes to a user for the exchange of the item he has posted from diffrent users.

public class ExchangeRequest extends AppCompatActivity {

    private RecyclerView recyclerView_echange_req;
    private ExchangeRequestAdapter exchangeRequestAdapter;
    private List<ExchangeModel> exchangeModelList;
    private ImageView iv_back;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_request);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExchangeRequest.this, MainActivity.class));
            }
        });

        recyclerView_echange_req = findViewById(R.id.recyclerview_exchange_req);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_echange_req.setLayoutManager(linearLayoutManager);

        exchangeModelList = new ArrayList<>();

        exchangeRequestAdapter = new ExchangeRequestAdapter(this, exchangeModelList);
        recyclerView_echange_req.setAdapter(exchangeRequestAdapter);

        FirebaseDatabase.getInstance().getReference().child("Exchange Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exchangeModelList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ExchangeModel exchangeModel = dataSnapshot.getValue(ExchangeModel.class);
                    exchangeModelList.add(exchangeModel);
                }
                exchangeRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}