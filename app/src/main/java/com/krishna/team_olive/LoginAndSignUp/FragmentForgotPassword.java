package com.krishna.team_olive.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.krishna.team_olive.R;

public class FragmentForgotPassword extends BottomSheetDialogFragment {

    TextInputLayout et_email_forgot;
    Button btn_login_forgot;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_forgot_password, container, false);

        et_email_forgot = v.findViewById(R.id.et_email_forgot);
        btn_login_forgot = v.findViewById(R.id.btn_login_forgot);

        auth = FirebaseAuth.getInstance();



        btn_login_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email_forgot.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter your e-mail", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(et_email_forgot.getEditText().getText().toString()).matches()) {
                    et_email_forgot.getEditText().setError("Please provide valid email!");
                    et_email_forgot.getEditText().requestFocus();
                } else {
                    auth.sendPasswordResetEmail(et_email_forgot.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Check your email to reset your password", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "Try again! Something getting wrong", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });


        return v;
    }
}