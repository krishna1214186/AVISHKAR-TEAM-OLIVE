package com.krishna.team_olive.LoginAndSignUp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.krishna.team_olive.MainActivity;
import com.krishna.team_olive.ModelClasses.Users;
import com.krishna.team_olive.R;

import java.util.HashMap;

public class SignUp2Activity extends AppCompatActivity {

    private TextInputLayout et_locationSignIn;
    private TextInputLayout et_phoneSignIn;

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
        et_phoneSignIn = findViewById(R.id.et_phoneNumber);
        btn_signIn = findViewById(R.id.btn_enter);

        String email = getIntent().getStringExtra("email");
        String pswd = getIntent().getStringExtra("pswd");
        String name = getIntent().getStringExtra("name");
//        String uid = auth.getCurrentUser().getUid();


        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                                Users users = new Users(firebaseAuth.getCurrentUser().getEmail(), "No", et_locationSignIn.getEditText().getText().toString(),
                                        name, et_phoneSignIn.getEditText().getText().toString(), "https://firebasestorage.googleapis.com/v0/b/team-olive-29eea.appspot.com/o/avatar%2Fman%20(1).png?alt=media&token=ac70b688-2340-46bf-930d-173073c74753",
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

    }
}