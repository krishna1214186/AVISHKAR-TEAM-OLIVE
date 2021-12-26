package com.krishna.team_olive.DiscreteFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.HomePage.ItemDetailActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.R;

public class FullScreenVideoActivity extends AppCompatActivity {


    VideoView vv_postvdo;
    FloatingActionButton fab_vdo_pause, fab_vdo_play;
    ImageView iv_vv_gradient, iv_fullscreen_exit;

    FirebaseDatabase database;

    int curr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

////        requestWindowFeature(Window.FEATURE_NO_TITLE);
////        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
        iv_fullscreen_exit = findViewById(R.id.iv_exit_fullscreen);
        iv_vv_gradient= findViewById(R.id.iv_vv_gradient_fullscreen);
        vv_postvdo = findViewById(R.id.vv_vdo_fullscreen);
        fab_vdo_pause = findViewById(R.id.fab_vdo_pause_fullscreen);
        fab_vdo_play = findViewById(R.id.fab_vdo_play_fullscreen);

        fab_vdo_play.setVisibility(View.INVISIBLE);
        fab_vdo_pause.setVisibility(View.INVISIBLE);
        iv_fullscreen_exit.setVisibility(View.INVISIBLE);
        iv_vv_gradient.setVisibility(View.INVISIBLE);


        String postid = getIntent().getStringExtra("post_id");
        AddedItemDescriptionModel model = (AddedItemDescriptionModel) getIntent().getSerializableExtra("model");

        database = FirebaseDatabase.getInstance();

        database.getReference().child("post_files").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String val = snapshot.getValue(String.class);
                vv_postvdo.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/team-olive-29eea.appspot.com/o/post_files%2F-MlzJQ18hTKP4UD0-YRD%2Fvideos%2F1?alt=media&token=702f6c77-940d-4d4e-b234-aff2d9304da9"));

                fab_vdo_play.setVisibility(View.VISIBLE);
                iv_fullscreen_exit.setVisibility(View.VISIBLE);
                iv_vv_gradient.setVisibility(View.VISIBLE);

                vv_postvdo.seekTo(1);
                vv_postvdo.start();
                vv_postvdo.pause();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab_vdo_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_vdo_play.setVisibility(View.INVISIBLE);
                fab_vdo_pause.setVisibility(View.VISIBLE);

                fab_vdo_pause.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
//                                             fab_vdo_pause.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                fab_vdo_pause.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                iv_fullscreen_exit.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                iv_fullscreen_exit.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                iv_vv_gradient.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                iv_vv_gradient.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                vv_postvdo.start();

            }
        });

        vv_postvdo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                fab_vdo_play.setVisibility(View.VISIBLE);
                iv_fullscreen_exit.setVisibility(View.VISIBLE);
                iv_vv_gradient.setVisibility(View.VISIBLE);
            }
        });

        vv_postvdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((curr%2==0)) {
                    fab_vdo_play.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    fab_vdo_play.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });

                    iv_fullscreen_exit.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    iv_fullscreen_exit.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });

                    iv_vv_gradient.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    iv_vv_gradient.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                    curr++;
                    vv_postvdo.pause();
                }
                else{
                    fab_vdo_play.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    fab_vdo_play.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });

                    iv_fullscreen_exit.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    iv_fullscreen_exit.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });

                    iv_vv_gradient.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    iv_vv_gradient.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                    curr++;
                }
            }
        });

        iv_fullscreen_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoActivity.this, ItemDetailActivity.class);
                intent.putExtra("post_id", model.getPostid());
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });
    }
}