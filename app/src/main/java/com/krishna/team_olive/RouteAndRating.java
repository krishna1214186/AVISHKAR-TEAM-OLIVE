package com.krishna.team_olive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class RouteAndRating extends AppCompatActivity  {
    private MaterialDialog mAnimatedDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_and_rating);
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
                        Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .setAnimation("delete_anim.json")
                .build();
        mAnimatedDialog.show();



    }


}