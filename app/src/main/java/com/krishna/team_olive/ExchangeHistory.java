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

public class ExchangeHistory extends AppCompatActivity {
    RecyclerView rv_history;
    FirebaseDatabase db;
    Exchange_history_Adapter adapter;
    List<AddedItemDescriptionModel> list;
    List<AddedItemDescriptionModel> list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_history);

        rv_history=findViewById(R.id.recyclerview_history);
        rv_history.setHasFixedSize(true);
        list=new ArrayList<>();
        list2=new ArrayList<>();

        rv_history.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Exchange_history_Adapter(ExchangeHistory.this,list2);
        rv_history.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child("My Exchanges").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String postid;
                List<String> postid=new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    postid.add(dataSnapshot.getValue(String.class));
                    // Toast.makeText(ExchangeHistory.this,dataSnapshot.getValue(String.class) , Toast.LENGTH_SHORT).show();
                }

//
                FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                            AddedItemDescriptionModel am=dataSnapshot.getValue(AddedItemDescriptionModel.class);
                            list.add(am);
                        }
                        list2.clear();
                        for(int i=0;i<postid.size();i++) {
                            String id=postid.get(i);
                            for(int j=0;j<list.size();j++) {
                                if (list.get(j).getPostid().equals(id)) {
                                    list2.add(list.get(j));
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
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