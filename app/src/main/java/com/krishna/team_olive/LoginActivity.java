package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout et_emailLogin;
    private TextInputLayout et_passwordLogin;
    private TextView tv_signIn, tv_forget_pswd;

    private Button btn_login;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        et_emailLogin = findViewById(R.id.et_emailLogin);
        et_passwordLogin = findViewById(R.id.et_passwordLogin);
        tv_signIn = findViewById(R.id.tv_signIn);
        btn_login = findViewById(R.id.btn_login_go);
        tv_forget_pswd = findViewById(R.id.btn_forget_pswd);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_emailLogin.getEditText().getText().toString().isEmpty() || et_passwordLogin.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.signInWithEmailAndPassword(et_emailLogin.getEditText().getText().toString(), et_passwordLogin.getEditText().getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else
                                            Toast.makeText(LoginActivity.this, "Verify your account first", Toast.LENGTH_SHORT).show();
                                    }

                                } else
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        tv_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_forget_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentForgotPassword forgotPassword = new FragmentForgotPassword();
                forgotPassword.show(getSupportFragmentManager(), forgotPassword.getTag());
            }
        });

    }
}