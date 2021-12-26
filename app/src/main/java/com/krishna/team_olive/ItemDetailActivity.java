package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.SendNotificationPack.APIService;
import com.krishna.team_olive.SendNotificationPack.Client;
import com.krishna.team_olive.SendNotificationPack.Data;
import com.krishna.team_olive.SendNotificationPack.MyResponse;
import com.krishna.team_olive.SendNotificationPack.NotificationSender;
import com.krishna.team_olive.chat.Chat;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity implements Animation.AnimationListener {

    VideoView vv_postvdo;

    FirebaseDatabase database;
    FirebaseAuth auth;

    PostPhotoAdapter postPhotoAdapter;
    RecyclerView rv_post_photo;
    //MapView mapView;
    ArrayList<String> list_photo;


    ArrayList<String> post_photo;

    TextView tv_title, tv_age, tv_description, tv_location, tv_exchange_cate;
    Button btn_chat,btn_exchange;
    TextView tv_detail_rating, tv_u_may_like;
    ImageView iv_post_poster;

    RecyclerView rv_user_review_detail;
    TextView tv_user_name;
    ImageView iv_user_review_detail;
    CircleImageView iv_user_img_detail;

    FloatingActionButton fab_vdo_play, fab_vdo_pause;
    ImageView iv_fullscreen, iv_vv_gradient, iv_ex_cate;

    String user_name, client_name, user_uid, client_uid, item_name;

    SupportMapFragment fragment_map;
    GoogleMap map;

    String post_id;
    int check;

    private APIService apiService;

    String location;
    String ct;

    ArrayList<AddedItemDescriptionModel> sim_model_list;
    RecyclerView rv_similar_post;

    private MediaController mediaController;

    int curr = 0;

    Animation Togetheraniamtion;
    int isClose = 0;

    AddedItemDescriptionModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_item_detail);

//mapView =findViewById(R.id.mapView);
        // mapView.onCreate(savedInstanceState);
        fragment_map= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentmap);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
////mapView.getMapAsync(new OnMapReadyCallback() {
//    @Override
//    public void onMapReady(@NonNull MapboxMap mapboxMap) {
//        MarkerOptions options=new MarkerOptions();
//        options.position(new LatLng(26.850000,80.949997));
//        mapboxMap.addMarker(options);
//    }
        //});
        list_photo = new ArrayList<>();
        sim_model_list = new ArrayList<>();

        post_id = getIntent().getStringExtra("postid");

        vv_postvdo = findViewById(R.id.vv_post_video);
        tv_title = findViewById(R.id.tv_item_title);
        tv_age = findViewById(R.id.tv_detail_age);
        tv_description = findViewById(R.id.tv_detail_description);
        rv_post_photo=findViewById(R.id.rv_photo);
        rv_similar_post = findViewById(R.id.rv_similar_post);
        tv_location = findViewById(R.id.tv_location);
        btn_chat = findViewById(R.id.btn_chat);
        btn_exchange = findViewById(R.id.btn_exchange);
        tv_exchange_cate = findViewById(R.id.tv_exchange_cate);
        iv_post_poster = findViewById(R.id.iv_post_poster);
//        iv_category = findViewById(R.id.iv_category);
//        iv_ex_category =findViewById(R.id.iv_exchange_category);
        tv_detail_rating = findViewById(R.id.tv_detail_rating);
        fab_vdo_play = findViewById(R.id.fab_vdo_play);
        fab_vdo_pause = findViewById(R.id.fab_vdo_pause);
        iv_fullscreen = findViewById(R.id.iv_enter_fullscreen);
        iv_vv_gradient = findViewById(R.id.iv_vv_gradient);
        tv_u_may_like = findViewById(R.id.tv_u_may_like_detail);

        rv_user_review_detail = findViewById(R.id.rv_user_review_detail);
        iv_user_img_detail = findViewById(R.id.iv_user_img_detail);
        tv_user_name = findViewById(R.id.tv_user_name);
        iv_user_review_detail = findViewById(R.id.iv_user_review_detail);
        iv_ex_cate = findViewById(R.id.iv_exchange_cate);


        post_photo = new ArrayList<>();

        fragment_map= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentmap);


        fab_vdo_play.setVisibility(View.INVISIBLE);
        fab_vdo_pause.setVisibility(View.INVISIBLE);
        iv_fullscreen.setVisibility(View.INVISIBLE);
        iv_vv_gradient.setVisibility(View.INVISIBLE);


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        post_id = getIntent().getStringExtra("postid");

        check = getIntent().getIntExtra("check",0);
        model = (AddedItemDescriptionModel) getIntent().getSerializableExtra("model");

        FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddedItemDescriptionModel object = snapshot.getValue(AddedItemDescriptionModel.class);

                item_name = object.getName();

//                tv_age.setText(object.getAgeOfProduct());
//                tv_title.setText(object.getName());
//                tv_location.setText(object.getAdress1()+", "+object.getAdress2());
//                tv_description.setText(object.getDescription());
//                tv_exchange_cate.setText(object.getExchangeCateogary());
//                rb_post.setRating(Float.parseFloat(object.getRatings()));
//                rb_post.setIsIndicator(true);
//                location = object.getAdress1()+", "+object.getAdress2();

                // setImageforCategory(object.getCateogary(), iv_category);
                // setImageforCategory(object.getExchangeCateogary(), iv_ex_category);
                List<Address>[] addressList=new List[]{new ArrayList<Address>()};
                fragment_map.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        //   mGoogleApiClient.connect();
                        map = googleMap;
//                        if(location!=null || !location.equals("")){
//                            Geocoder geocoder = new Geocoder(ItemDetailActivity.this);
//                            try {
//
//                                addressList[0] = geocoder.getFromLocationName(location, 1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                        //Address address = (Address) addressList[0].get(0);

                        // double latfield = address.getLatitude();
                        //  double longfieldc = address.getLongitude();
                        double latfield=26.850000;
                        double longfield=80.949997;

                        map.addMarker(new MarkerOptions().position(new LatLng(latfield, longfield)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latfield, longfield)));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(latfield, longfield)), 15.0f));


                        //  }

                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        database.getReference().child("post_files").child(post_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    String var = dataSnapshot.getValue(String.class);
////                    uri_list.add(var);
//                }
////                dots = new TextView[uri_list.size()];
//
//
////                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        database.getReference().child("post_files").child(post_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    String var = dataSnapshot.getValue(String.class);
//                    uri_list.add(var);
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//                tv_age.setText(model.getAgeOfProduct());
       /* tv_title.setText(model.getName());
        tv_location.setText(model.getAdress1()+", "+model.getAdress2());
        if(model.getDescription()!=null)
        tv_description.setText(model.getDescription());
        tv_exchange_cate.setText(model.getExchangeCateogary());

        tv_detail_rating.setText(model.getRatings()+ "/5");

        location = model.getAdress1()+", "+ model.getAdress2();
        if(!(model.getImageurl().equals("")))
            Picasso.get().load(model.getImageurl()).placeholder(R.drawable.ic_ads).into(iv_post_poster);*/

        database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("isNGO").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String isNGO = snapshot.getValue(String.class);
                        if(isNGO.equals("Yes")){
                            iv_ex_cate.setVisibility(View.INVISIBLE);
                            tv_exchange_cate.setVisibility(View.INVISIBLE);
                            tv_u_may_like.setVisibility(View.GONE);
                            rv_similar_post.setVisibility(View.GONE);
                        }
                        else{
                            shownonNGOposts();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        database.getReference().child("users").child(model.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users user = snapshot.getValue(Users.class);
                        if(user.getProfileimg()!=null){
                            Picasso.get().load(user.getProfileimg()).placeholder(R.drawable.user_icon).into(iv_user_img_detail);
                        }
                        tv_user_name.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );



        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check!=0) {
                    FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String x = dataSnapshot.child("postid").getValue(String.class);
                                if (x.equals(post_id)) {
                                    ct = dataSnapshot.child("cateogary").getValue(String.class);
                                    break;
                                }
                            }
                            FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int number = snapshot.child(ct).getValue(Integer.class);

                                    number += 1;
                                    FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getUid()).child(ct).setValue(number);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                client_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("users").child(client_uid).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        client_name = snapshot.getValue().toString();
                        FirebaseDatabase.getInstance().getReference().child("posts with uid").child(post_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    user_uid = dataSnapshot.getValue().toString();
                                    FirebaseDatabase.getInstance().getReference().child("Exchange Requests").child(user_uid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int i=1;
                                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                ExchangeModel exchangeModel = dataSnapshot.getValue(ExchangeModel.class);
                                                if (exchangeModel.getPostid().equals(post_id) && exchangeModel.getClient_uid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                    Toast.makeText(ItemDetailActivity.this, "You have already sent a exchange request !!", Toast.LENGTH_SHORT).show();
                                                    i=0;
                                                }
                                            }
                                            if(i==1){
                                                FirebaseDatabase.getInstance().getReference().child("users").child(user_uid).child("name").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        user_name = snapshot.getValue().toString();
                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Exchange Requests").child(user_uid).push();
                                                        ExchangeModel exchangeModel = new ExchangeModel(user_name, client_name, user_uid, client_uid, item_name, post_id, post_id);

                                                        FirebaseDatabase.getInstance().getReference().child("Tokens").
                                                                child(user_uid).child("token").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String token = snapshot.getValue(String.class);
                                                                sendNotifications(token, "You got an exchange request ", client_name +" has send exchange request for "+ item_name);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });





                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(model.getPostid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String uid=snapshot.child("uid").getValue(String.class);
                        FirebaseDatabase.getInstance().getReference().child("persons for chatting").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                int i=0;
                                for(DataSnapshot datasnapshot2:snapshot2.getChildren())
                                {

                                    if(datasnapshot2.child("uid").getValue(String.class).equals(uid))
                                    {

                                        i++;
                                        break;
                                    }
                                }
                                if(i==0)
                                {
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String name=snapshot.child("name").getValue(String.class);
                                            HashMap<String,Object> hm=new HashMap<>();
                                            hm.put("name",name);
                                            hm.put("uid",uid);

                                            FirebaseDatabase.getInstance().getReference().child("persons for chatting").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(hm);




                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                           /* FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users users=snapshot.getValue(Users.class);
                                    HashMap<String,Object> hm1=new HashMap<>();
                                    hm1.put("name",users.getName());
                                    hm1.put("uid",users.getUid());
                                    FirebaseDatabase.getInstance().getReference().child("persons for chatting").child(uid).push().setValue(hm1);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });*/
                                }

                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name=snapshot.child("name").getValue(String.class);
                                        Intent intent=new Intent(ItemDetailActivity.this, Chat.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("uid",uid);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(model.getPostid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String uid=snapshot.child("uid").getValue(String.class);
                        FirebaseDatabase.getInstance().getReference().child("persons for chatting").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                int i=0;
                                for(DataSnapshot datasnapshot2:snapshot2.getChildren())
                                {

                                    if(datasnapshot2.child("uid").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                    {

                                        i++;
                                        break;
                                    }
                                }
                                if(i==0)
                                {
                                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String name=snapshot.child("name").getValue(String.class);
                                            HashMap<String,Object> hm=new HashMap<>();
                                            hm.put("name",name);
                                            hm.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            FirebaseDatabase.getInstance().getReference().child("persons for chatting").child(uid).push().setValue(hm);




                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                           /* FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users users=snapshot.getValue(Users.class);
                                    HashMap<String,Object> hm1=new HashMap<>();
                                    hm1.put("name",users.getName());
                                    hm1.put("uid",users.getUid());
                                    FirebaseDatabase.getInstance().getReference().child("persons for chatting").child(uid).push().setValue(hm1);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });*/
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        Context context = ItemDetailActivity.this;

        database.getReference().child("post_files").child(model.getPostid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String val = snapshot1.getValue(String.class);
                    list_photo.add(val);
                }
                rv_post_photo.setHasFixedSize(true);
                postPhotoAdapter =new PostPhotoAdapter(context,list_photo);
                rv_post_photo.setAdapter(postPhotoAdapter);
//
                LinearLayoutManager layoutmanager=new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false);
                rv_post_photo.setLayoutManager(layoutmanager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("post_files").child(model.getPostid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String val = snapshot.getValue(String.class);
                vv_postvdo.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/team-olive-29eea.appspot.com/o/post_files%2F-MlzJQ18hTKP4UD0-YRD%2Fvideos%2F1?alt=media&token=702f6c77-940d-4d4e-b234-aff2d9304da9"));

                fab_vdo_play.setVisibility(View.VISIBLE);
                iv_fullscreen.setVisibility(View.VISIBLE);
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

                iv_fullscreen.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                iv_fullscreen.setVisibility(View.INVISIBLE);
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
                iv_fullscreen.setVisibility(View.VISIBLE);
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
                                    fab_vdo_play.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });

                    iv_fullscreen.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    iv_fullscreen.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {

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
                                    iv_vv_gradient.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {

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

                    iv_fullscreen.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    iv_fullscreen.setVisibility(View.INVISIBLE);
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

        iv_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, FullScreenVideoActivity.class);
                intent.putExtra("post_id", model.getPostid());
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });

//        database.getReference().child("allpostswithoutuser").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                    AddedItemDescriptionModel model1 = snapshot1.getValue(AddedItemDescriptionModel.class);
//                    if( ((model1.getCateogary().equals(model.getCateogary()) ) ||( model1.getCateogary().equals(model.getExchangeCateogary())) ||
//                            (model1.getExchangeCateogary() .equals( model.getCateogary())) || (model1.getExchangeCateogary().equals(model.getExchangeCateogary() ) ))
//                            && !(model1.getUid() .equals(model.getUid())) ){
//                        sim_model_list.add(model1);
//                    }
//                }
//
//                rv_similar_post.setHasFixedSize(true);
//                SimilarPostAdapter similarPostAdapter;
//                similarPostAdapter =new SimilarPostAdapter(context,sim_model_list);
//                rv_similar_post.setAdapter(similarPostAdapter);
//
//                LinearLayoutManager layoutmanager=new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false);
//                rv_similar_post.setLayoutManager(layoutmanager);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        List<Address>[] addressList=new List[]{new ArrayList<Address>()};
        fragment_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                //   mGoogleApiClient.connect();
                map = googleMap;
//                        if(location!=null || !location.equals("")){
//                            Geocoder geocoder = new Geocoder(ItemDetailActivity.this);
//                            try {
//
//                                addressList[0] = geocoder.getFromLocationName(location, 1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                //Address address = (Address) addressList[0].get(0);

                // double latfield = address.getLatitude();
                //  double longfieldc = address.getLongitude();
                double latfield=26.850000;
                double longfield=80.949997;

                map.addMarker(new MarkerOptions().position(new LatLng(latfield, longfield)));
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latfield, longfield)));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(latfield, longfield)), 15.0f));


                //  }

            }
        });

        iv_user_review_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClose%2==0) {
                    Togetheraniamtion = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.image_rotation);

                    Togetheraniamtion.setAnimationListener(ItemDetailActivity.this);
                    iv_user_review_detail.startAnimation(Togetheraniamtion);
                }
                else{
                    Togetheraniamtion = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.rev_image_rotation);

                    Togetheraniamtion.setAnimationListener(ItemDetailActivity.this);
                    iv_user_review_detail.startAnimation(Togetheraniamtion);
                }
            }
        });

        ArrayList<String> list = new ArrayList<>();

        database.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid().toString()).child("review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    list.add(snapshot1.getValue(String.class));
                }
                rv_user_review_detail.hasFixedSize();
                ReviewAdapter adapter =new ReviewAdapter(ItemDetailActivity.this,list);
                rv_user_review_detail.setAdapter(adapter);

                LinearLayoutManager layoutmanager=new LinearLayoutManager(ItemDetailActivity.this,RecyclerView.VERTICAL,false);
                rv_user_review_detail.setLayoutManager(layoutmanager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



//        Picasso.get().load(R.drawable.bike_img).into(iv_post_poster);






    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ItemDetailActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(isClose%2==0){
            isClose++;
            rv_user_review_detail.setVisibility(View.VISIBLE);
        }
        else{
            isClose++;
            rv_user_review_detail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void shownonNGOposts() {
        database.getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    AddedItemDescriptionModel model1 = snapshot1.getValue(AddedItemDescriptionModel.class);
                    if( ((model1.getCateogary().equals(model.getCateogary()) ) ||( model1.getCateogary().equals(model.getExchangeCateogary())) ||
                            (model1.getExchangeCateogary() .equals( model.getCateogary())) || (model1.getExchangeCateogary().equals(model.getExchangeCateogary() ) ))
                            && !(model1.getUid() .equals(model.getUid())) ){
                        sim_model_list.add(model1);
                    }
                }

                rv_similar_post.setHasFixedSize(true);
                SimilarPostAdapter similarPostAdapter;
                similarPostAdapter =new SimilarPostAdapter(ItemDetailActivity.this,sim_model_list);
                rv_similar_post.setAdapter(similarPostAdapter);

                LinearLayoutManager layoutmanager=new LinearLayoutManager(ItemDetailActivity.this, RecyclerView.HORIZONTAL,false);
                rv_similar_post.setLayoutManager(layoutmanager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        /*

        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                int start = addedItemDescriptionModelArrayList.size();
                int end = start + 4;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(i == end){
                        break;
                    }
                    if(i < start){
                        i++;
                        continue;
                    }
                    i++;
                    AddedItemDescriptionModel object ;
                    object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    addedItemDescriptionModelArrayList.add(object);

                }
               postAdapter.notifyDataSetChanged();
                postAdapter.setLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         */
}