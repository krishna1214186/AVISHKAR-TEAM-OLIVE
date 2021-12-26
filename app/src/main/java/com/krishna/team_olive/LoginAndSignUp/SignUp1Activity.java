package com.krishna.team_olive.LoginAndSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.krishna.team_olive.R;

public class SignUp1Activity extends AppCompatActivity {


    private TextInputLayout et_nameSignIn;
    private TextInputLayout et_emailSignIn;
    private TextInputLayout et_passwordSignIn;
    private TextInputLayout et_confirmPasswordSignIn;

    private Button btn_nextSignIn;
    private TextView tv_isNGOSignIn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        firebaseAuth = FirebaseAuth.getInstance();

        et_nameSignIn = findViewById(R.id.et_nameSignIn);
        et_emailSignIn = findViewById(R.id.et_emailSignIn);
        et_passwordSignIn = findViewById(R.id.et_passwordSignIn);
        et_confirmPasswordSignIn = findViewById(R.id.et_confirmPasswordSignIn);
        btn_nextSignIn = findViewById(R.id.btn_nextSignIn);
        tv_isNGOSignIn = findViewById(R.id.tv_isNGOSignIn);

        tv_isNGOSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp1Activity.this, SignUpNGO1Activity.class);
                startActivity(intent);
                finish();
            }
        });


        btn_nextSignIn.setOnClickListener(new View.OnClickListener() {



                @Override
                public void onClick(View v) {

                    if (et_nameSignIn.getEditText().getText().toString().isEmpty() || et_emailSignIn.getEditText().getText().toString().isEmpty() || et_passwordSignIn.getEditText().getText().toString().isEmpty() || et_confirmPasswordSignIn.getEditText().getText().toString().isEmpty()) {
                        Toast.makeText(SignUp1Activity.this, "Enter all fields !", Toast.LENGTH_SHORT).show();
                    } else if (!(et_passwordSignIn.getEditText().getText().toString().equals(et_confirmPasswordSignIn.getEditText().getText().toString()))) {
                        Toast.makeText(SignUp1Activity.this, "Both passowrds do not match !", Toast.LENGTH_SHORT).show();
                    } else {

                        firebaseAuth.createUserWithEmailAndPassword(et_emailSignIn.getEditText().getText().toString(), et_passwordSignIn.getEditText().getText().toString()).
                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Intent intent = new Intent(SignUp1Activity.this, SignUp2Activity.class);
                                                    intent.putExtra("email", et_emailSignIn.getEditText().getText().toString());
                                                    intent.putExtra("pswd", et_passwordSignIn.getEditText().getText().toString());
                                                    intent.putExtra("name", et_nameSignIn.getEditText().getText().toString());
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    Toast.makeText(SignUp1Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                    }
                }
            });
        }
    }

