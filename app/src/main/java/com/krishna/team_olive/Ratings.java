package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ratings extends AppCompatActivity {
ImageView str1,str2,str3,str4,str5;
EditText et;
Button btn_save;
    int star=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        String uid=getIntent().getStringExtra("ud");
        Toast.makeText(Ratings.this, uid, Toast.LENGTH_SHORT).show();

        str1=findViewById(R.id.star_1);
        str2=findViewById(R.id.star_2);
        str3=findViewById(R.id.star_3);
        str4=findViewById(R.id.star_4);
        str5=findViewById(R.id.star_5);
        et=findViewById(R.id.tv_review);
        btn_save=findViewById(R.id.btn_save);
        str1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1.setImageResource(R.drawable.star_filled);
                str2.setImageResource(R.drawable.star_unfilled);
                str3.setImageResource(R.drawable.star_unfilled);
                str4.setImageResource(R.drawable.star_unfilled);
                str5.setImageResource(R.drawable.star_unfilled);
                star=1;

            }
        });
        str2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1.setImageResource(R.drawable.star_filled);
                str2.setImageResource(R.drawable.star_filled);
                str3.setImageResource(R.drawable.star_unfilled);
                str4.setImageResource(R.drawable.star_unfilled);
                str5.setImageResource(R.drawable.star_unfilled);
star=2;
            }
        });
        str3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1.setImageResource(R.drawable.star_filled);
                str2.setImageResource(R.drawable.star_filled);
                str3.setImageResource(R.drawable.star_filled);
                str4.setImageResource(R.drawable.star_unfilled);
                str5.setImageResource(R.drawable.star_unfilled);
star=3;
            }
        });
        str4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1.setImageResource(R.drawable.star_filled);
                str2.setImageResource(R.drawable.star_filled);
                str3.setImageResource(R.drawable.star_filled);
                str4.setImageResource(R.drawable.star_filled);
                str5.setImageResource(R.drawable.star_unfilled);
                star=4;

            }
        });
        str5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1.setImageResource(R.drawable.star_filled);
                str2.setImageResource(R.drawable.star_filled);
                str3.setImageResource(R.drawable.star_filled);
                str4.setImageResource(R.drawable.star_filled);
                str5.setImageResource(R.drawable.star_filled);
star=5;
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Ratings").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int rating=0;
                        int number=0;
                       /* for(DataSnapshot datasnapshot:snapshot.getChildren())
                        {
                            rating=datasnapshot.child("rating").getValue(Integer.class);
                            number=datasnapshot.child("number").getValue(Integer.class);
                        }*/
                        rating=snapshot.child("rating").getValue(Integer.class);
                        number=snapshot.child("number").getValue(Integer.class);
                        rating=(rating*number+star)/(number+1);
                        number+=1;
                        String review=et.getText().toString();
                        Toast.makeText(Ratings.this, ""+star, Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference().child("Ratings").child(uid).child("rating").setValue(rating);
                        FirebaseDatabase.getInstance().getReference().child("Ratings").child(uid).child("number").setValue(number);
                        FirebaseDatabase.getInstance().getReference().child("Ratings").child(uid).child("review").push().setValue(review);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



    }
}