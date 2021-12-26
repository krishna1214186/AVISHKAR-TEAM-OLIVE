package com.krishna.team_olive.NotificationDrawerTop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.AdapterClasses.Exchange_history_Adapter;
import com.krishna.team_olive.R;

import java.util.ArrayList;
import java.util.List;

//Activity for exchange history which shows a list of past exchanges that we have done.

public class ExchangeHistory extends AppCompatActivity {
    private RecyclerView recyclerViewHistory;
    private Exchange_history_Adapter exchangeHistoryAdapter;
    private List<AddedItemDescriptionModel> list;
    private List<AddedItemDescriptionModel> list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_history);

        recyclerViewHistory = findViewById(R.id.recyclerview_history);
        recyclerViewHistory.setHasFixedSize(true);
        list = new ArrayList<>();
        list2 = new ArrayList<>();

        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        exchangeHistoryAdapter = new Exchange_history_Adapter(ExchangeHistory.this, list2);
        recyclerViewHistory.setAdapter(exchangeHistoryAdapter);
        FirebaseDatabase.getInstance().getReference().child("Myexchanges").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> postid = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    postid.add(dataSnapshot.getValue(String.class));
                }

                FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            AddedItemDescriptionModel am = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                            list.add(am);


                        }
                        list2.clear();
                        for (int i = 0; i < postid.size(); i++) {
                            String id = postid.get(i);
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getPostid().equals(id)) {
                                    list2.add(list.get(j));

                                }
                            }

                        }
                        exchangeHistoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}