package com.krishna.team_olive;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp2Activity extends AppCompatActivity {

    private EditText et_locationSignIn;
    private EditText et_phoneSignIn;

    private Button btn_signIn;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        et_locationSignIn = findViewById(R.id.et_location);
        et_phoneSignIn = findViewById(R.id.et_phone);
        btn_signIn = findViewById(R.id.btn_enter);

        SharedPreferences getshared = getSharedPreferences("data", Context.MODE_PRIVATE);
        String name = getshared.getString("name", "not known");
//        String email = getshared.getString("email", "not known");
        String pswd = getshared.getString("password", "not known");
//        String uid = auth.getCurrentUser().getUid();


        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(firebaseAuth.getCurrentUser().getEmail(), pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                                Users users = new Users(firebaseAuth.getCurrentUser().getEmail(), "No", et_locationSignIn.getText().toString(),
                                        name, et_phoneSignIn.getText().toString(), "",
                                        firebaseAuth.getCurrentUser().getUid());
                                firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(users);
                                HashMap<String, Object> hm = new HashMap<>();
                                hm.put("CARS", 0);
                                hm.put("BOOKS", 0);
                                hm.put("CYCLES", 0);
                                hm.put("FURNITURE", 0);
                                hm.put("TV", 0);
                                hm.put("LAPTOP", 0);
                                hm.put("OTHERS", 0);
                                hm.put("MOBILES", 0);
                                hm.put("CLOTHES", 0);
                                hm.put("BIKES", 0);
                                hm.put("ELECTRONICITEMS", 0);

                                firebaseDatabase.getReference().child("histo").child(firebaseAuth.getCurrentUser().getUid()).setValue(hm);
                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(firebaseAuth.getCurrentUser().getUid()).child("rating").setValue(0);
                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(firebaseAuth.getCurrentUser().getUid()).child("number").setValue(0);
                                FirebaseDatabase.getInstance().getReference().child("Ratings").child(firebaseAuth.getCurrentUser().getUid()).child("review").push().setValue("");
                                Intent i = new Intent(SignUp2Activity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else
                                Toast.makeText(SignUp2Activity.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUp2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(SignUp2Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}