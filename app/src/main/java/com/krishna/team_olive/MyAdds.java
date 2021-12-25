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

public class MyAdds extends AppCompatActivity {
    ArrayList<MyAddModel> al;
    RecyclerView rvMyAdd;
    MyAddsAdapter myAddsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adds);
        rvMyAdd=findViewById(R.id.recyclerview_my_adds);
        al=new ArrayList<MyAddModel>();
        LinearLayoutManager lm=new LinearLayoutManager(this);
        rvMyAdd.setLayoutManager(lm);
        rvMyAdd.setHasFixedSize(true);
        MyAddsAdapter m=new MyAddsAdapter(this,al);
        rvMyAdd.setAdapter(m);
        FirebaseDatabase.getInstance().getReference().child("mypostswithuser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    AddedItemDescriptionModel am=dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    MyAddModel mam=new MyAddModel(am.getName(),am.getExchangeCateogary(),null);
                    al.add(mam);
                    m.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}