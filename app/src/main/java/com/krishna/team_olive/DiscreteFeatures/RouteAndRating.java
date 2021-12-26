package com.krishna.team_olive.DiscreteFeatures;

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
import com.krishna.team_olive.MainActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.R;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;

public class RouteAndRating extends AppCompatActivity  {
    private MaterialDialog mAnimatedDialog;
    private Dialog dialog;
float rating;
String feedback;
String uid;
String address;
String postid;
private Dialog dialog_congratulations;
private Dialog dialog_rating;
private Dialog dialog_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_and_rating);
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("address");
        postid=getIntent().getStringExtra("postid");
        dialog=new Dialog(RouteAndRating.this);
        dialog_congratulations=new Dialog(RouteAndRating.this);
        dialog_congratulations.setContentView(R.layout.congratulations_dialog);
        dialog_congratulations.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_congratulations.show();
        Button btn_navigate=dialog_congratulations.findViewById(R.id.btn_navigate);
        btn_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RouteAndRating.this, NavMap.class);
                intent.putExtra("add",address);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        Button btn_already=dialog_congratulations.findViewById(R.id.btn_already);
        btn_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dialog.dismiss();
                dialog_rating=new Dialog(RouteAndRating.this);
             dialog_rating.setContentView(R.layout.rate_user);
             dialog_rating.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
             dialog_rating.show();
             Button btn_next=dialog_rating.findViewById(R.id.btn_next);
             btn_next.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     RatingBar rb=dialog_rating.findViewById(R.id.rb_stars);
                     rating=rb.getRating();
                     dialog_rating.dismiss();
                     dialog_feedback=new Dialog(RouteAndRating.this);
                     dialog_feedback.setContentView(R.layout.feedback_user);
                     dialog_feedback.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                     dialog_feedback.show();
                     Button btn_save=dialog_feedback.findViewById(R.id.btn_save);
                     btn_save.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             EditText et_feedback=dialog_feedback.findViewById(R.id.et_feedback);
                             feedback=et_feedback.getText().toString();
                             if(feedback.isEmpty())
                             {
                                 Toast.makeText(RouteAndRating.this, "Please Dont provide empty feedback", Toast.LENGTH_SHORT).show();
                             }
                             else
                             {
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
                                         FirebaseDatabase.getInstance().getReference().child("UserExchanges").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(postid);
                                         FirebaseDatabase.getInstance().getReference().child("UserExchanges").child(uid).push().setValue(postid);
                                         FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                 if(snapshot.hasChild(postid))
                                                 {
                                                     AddedItemDescriptionModel am=snapshot.child(postid).getValue(AddedItemDescriptionModel.class);
                                                     FirebaseDatabase.getInstance().getReference().child("exchanged_post").child(postid).setValue(am);
                                                 }
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
                                 Toast.makeText(RouteAndRating.this, "Your feedback is saved Succesfully", Toast.LENGTH_SHORT).show();
                                 Intent intent=new Intent(RouteAndRating.this, MainActivity.class);
                                 startActivity(intent);
                             }
                         }
                     });

                 }
             });




            }
        });

        /*mAnimatedDialog = new MaterialDialog.Builder(this)
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
        mAnimatedDialog.show();*/



    }


}