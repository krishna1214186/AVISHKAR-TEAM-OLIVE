package com.krishna.team_olive;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class RouteAndRating extends AppCompatActivity  {
    private MaterialDialog mAnimatedDialog;
    private Dialog dialog;
float rating;
String feedback;
String uid;
String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_and_rating);
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("address");
        dialog=new Dialog(RouteAndRating.this);
        mAnimatedDialog = new MaterialDialog.Builder(this)
                .setTitle("Hurray!!")
                .setMessage("Your request has been accepted")
                .setCancelable(false)
                .setPositiveButton("Navigate to user", R.drawable.ic_location, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(RouteAndRating.this, NavMap.class);
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Already exchanged", R.drawable.ic_check, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        dialog.setContentView(R.layout.rate_user);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button save=dialog.findViewById(R.id.btn_save);
                        dialog.show();
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RatingBar rb=dialog.findViewById(R.id.rb_stars);
                                rating=rb.getRating();
                                //Toast.makeText(RouteAndRating.this, rating+"", Toast.LENGTH_SHORT).show();
dialog.dismiss();
                                dialog.setContentView(R.layout.feedback_user);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                                Button btn_feedback=dialog.findViewById(R.id.bt_send);
                                EditText et_feedback=dialog.findViewById(R.id.et_feedback);
                                btn_feedback.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        feedback=et_feedback.getText().toString();
                                        int stars=(int)rating;
                                        FirebaseDatabase.getInstance().getReference().child("Ratings").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int number=snapshot.child(uid).child("number").getValue(Integer.class);
                                                number=number+1;
                                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(uid).child("number").setValue(number);
                                                int y=snapshot.child(uid).child("rating").getValue(Integer.class);
                                                y=(y+stars)/number;
                                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(uid).child("rating").setValue(y);
                                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(uid).child("review").push().setValue(feedback);


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        dialog.dismiss();
                                    }
                                });


                            }
                        });
                    }
                })

                .setAnimation("delete_anim.json")
                .build();
        mAnimatedDialog.show();



    }


}