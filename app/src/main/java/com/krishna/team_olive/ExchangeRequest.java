package com.krishna.team_olive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRequest extends AppCompatActivity {

    private RecyclerView recyclerView_echange_req;
    ExchangeRequestAdapter exchangeRequestAdapter;
    List<ExchangeModel> mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_request);

        recyclerView_echange_req = findViewById(R.id.recyclerview_exchange_req);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_echange_req.setLayoutManager(linearLayoutManager);

        mlist = new ArrayList<>();

        exchangeRequestAdapter = new ExchangeRequestAdapter(this,mlist);
        recyclerView_echange_req.setAdapter(exchangeRequestAdapter);

    }
}