package com.krishna.team_olive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ImageView iv_logo;
    TextView tv_name,tv_slogan;
    private static int X=1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        iv_logo=findViewById(R.id.imageView);
        tv_name=findViewById(R.id.textView);
//        Shader shader = new LinearGradient(0,0,0,tv_name.getLineHeight(),
//                new int[]{
//                        Color.parseColor("#FFD700"),
//                        Color.parseColor("#FF793B")
//                        }, null, Shader.TileMode.REPEAT);
//        tv_name.getPaint().setShader(shader);
        tv_slogan=findViewById(R.id.textView2);
        iv_logo.setAnimation(topAnim);
        tv_name.setAnimation(bottomAnim);
        tv_slogan.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this,LoginActivity.class);
//                Pair[] pairs=new Pair[2];
//                pairs[0]=new Pair<View,String>(iv_logo,"logo_image");
//                pairs[1]=new Pair<View,String>(tv_name,"logo_text");
//
//                if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP)
//                {
//                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Splash.this,pairs);
//                    startActivity(intent,options.toBundle());
//                }
                startActivity(intent);

                finish();

            }
        },X);

    }
}