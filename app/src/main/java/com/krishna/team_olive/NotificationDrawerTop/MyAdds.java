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
import com.krishna.team_olive.AdapterClasses.MyAddsAdapter;
import com.krishna.team_olive.MainActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.ModelClasses.MyAddModel;
import com.krishna.team_olive.R;

import java.util.ArrayList;

public class MyAdds extends AppCompatActivity {
    ArrayList<MyAddModel> al;
    RecyclerView rvMyAdd;
    MyAddsAdapter myAddsAdapter;

    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adds);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAdds.this, MainActivity.class));
            }
        });

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