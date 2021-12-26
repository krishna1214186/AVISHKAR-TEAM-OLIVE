package com.krishna.team_olive.NotificationDrawerBottom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.krishna.team_olive.LoginAndSignUp.LoginActivity;
import com.krishna.team_olive.MainActivity;
import com.krishna.team_olive.R;

import java.util.Locale;

public class Settings extends AppCompatActivity {


    private Dialog dialog;
    private Button  eng, hindi;
    private ImageView iv_arrow, iv_arrpw_logout;

    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });

        iv_arrpw_logout = findViewById(R.id.iv_arrow_logout);
        iv_arrpw_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Settings.this, LoginActivity.class));
                finish();
            }
        });

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialouge_language);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        eng = dialog.findViewById(R.id.english);
        hindi = dialog.findViewById(R.id.hindi);
        iv_arrow = findViewById(R.id.iv_arrow_lang);

        iv_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog();
            }
        });
    }

    private void alertdialog() {
        dialog.show();
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(Settings.this,"en");
            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(Settings.this,"hi");
            }
        });

    }

    public void setLanguage(Activity activity, String language){
        Locale locale = new Locale(language);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config,resources.getDisplayMetrics());
        dialog.dismiss();
    }
}