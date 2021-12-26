package com.krishna.team_olive.LoginAndSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.krishna.team_olive.R;

public class Splash extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ImageView iv_logo;
    TextView tv_name,tv_slogan;
    private static int X=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        iv_logo=findViewById(R.id.imageView);
        tv_name=findViewById(R.id.textView);
        tv_slogan=findViewById(R.id.textView2);
        iv_logo.setAnimation(topAnim);
        tv_name.setAnimation(bottomAnim);
        tv_slogan.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        },X);

    }
}