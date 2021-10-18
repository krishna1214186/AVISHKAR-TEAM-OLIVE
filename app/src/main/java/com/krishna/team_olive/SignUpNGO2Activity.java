package com.krishna.team_olive;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.getCredential;

public class SignUpNGO2Activity extends AppCompatActivity {

    EditText et_location, et_phone;

    Button btn_enter;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        et_location = findViewById(R.id.et_location);
        et_phone = findViewById(R.id.et_phone);
        btn_enter = findViewById(R.id.btn_enter);

        SharedPreferences getshared = getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = getshared.getString("name","not known");
//        String email = getshared.getString("email", "not known");
        String pswd =  getshared.getString("password", "not known");
//        String uid = auth.getCurrentUser().getUid();


        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword(auth.getCurrentUser().getEmail(),pswd ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if (auth.getCurrentUser().isEmailVerified()) {

                                Users users = new Users(auth.getCurrentUser().getEmail(), "No", et_location.getText().toString(),
                                        name, et_phone.getText().toString(),"",
                                        auth.getCurrentUser().getUid());
                                database.getReference().child("users").child(auth.getCurrentUser().getUid()).setValue(users);
                                database.getReference().child("users").child(auth.getCurrentUser().getUid()).setValue(users);
                                HashMap<String,Object> hm=new HashMap<>();
                                hm.put("CARS",0);
                                hm.put("BOOKS",0);
                                hm.put("CYCLES",0);
                                hm.put("FURNITURE",0);
                                hm.put("TV",0);
                                hm.put("LAPTOP",0);
                                hm.put("OTHERS",0);
                                hm.put("MOBILES",0);
                                hm.put("CLOTHES",0);
                                hm.put("BIKES",0);
                                hm.put("ELECTRONICITEMS",0);

                                database.getReference().child("histo").child(auth.getCurrentUser().getUid()).setValue(hm);
                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid()).child("rating").setValue(0);
                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid()).child("number").setValue(0);
                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid()).child("review").push().setValue("");

                                Intent i = new Intent(SignUpNGO2Activity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else
                                Toast.makeText(SignUpNGO2Activity.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpNGO2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if (auth.getCurrentUser().isEmailVerified()){
            Intent intent = new Intent(SignUpNGO2Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}