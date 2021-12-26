package com.krishna.team_olive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyExchangeHistory extends AppCompatActivity {
    ArrayList<MyexchangeHistoryModel> al;
    RecyclerView rvHistory;
    MyexchangeHistoryAdapter myexchangeHistoryAdapter;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exchange_history);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyExchangeHistory.this, MainActivity.class));
            }
        });
        rvHistory=findViewById(R.id.recyclerview_exchange_history);
        al=new ArrayList<MyexchangeHistoryModel>();
        LinearLayoutManager lm=new LinearLayoutManager(this);
        rvHistory.setLayoutManager(lm);
        rvHistory.setHasFixedSize(true);
        MyexchangeHistoryAdapter m=new MyexchangeHistoryAdapter(this,al);
        rvHistory.setAdapter(m);
        FirebaseDatabase.getInstance().getReference().child("UserExchanges").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String postid=dataSnapshot.getValue(String.class);

                    FirebaseDatabase.getInstance().getReference().child("exchanged_post").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AddedItemDescriptionModel am=snapshot.child(postid).getValue(AddedItemDescriptionModel.class);
                            MyexchangeHistoryModel mm=new MyexchangeHistoryModel(null,am.getName(),am.getCateogary(),am.getExchangeCateogary());
                            al.add(mm);
                            m.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}