package com.krishna.team_olive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {
Button btn_save;
EditText et_feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        btn_save=findViewById(R.id.btn_Save);
        et_feedback=findViewById(R.id.et_feedback);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback=et_feedback.getText().toString();
                if(feedback.isEmpty())
                {
                    Toast.makeText(Feedback.this, "please provide some feedback", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Feedback.this, "Your feedback is saved succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Feedback.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}