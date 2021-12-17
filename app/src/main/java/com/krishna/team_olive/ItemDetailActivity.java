package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.chat.Chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    LinearLayout dotsLayout;
    SliderAdapter adapter;
    ViewPager2 pager2;
    ArrayList<String> uri_list;
    TextView[] dots;

    TextView tv_title, tv_age, tv_description, tv_location;
    Button btn_chat,btn_exchange;
    ImageView iv_category, iv_ex_category;
    RatingBar rb_post;

    String user_name, client_name, user_uid, client_uid, item_name;

    SupportMapFragment fragmentmap;
   // GoogleMap map;

    String postid;
    int check;

    FirebaseDatabase database;
    FirebaseAuth auth;

    String location;
    String ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        postid = getIntent().getStringExtra("postid");

        dotsLayout = findViewById(R.id.dots_container);
        pager2 = findViewById(R.id.vp_item_pic);
        tv_title = findViewById(R.id.tv_title);
        tv_age = findViewById(R.id.tv_age);
        tv_description = findViewById(R.id.tv_description);
        tv_location = findViewById(R.id.tv_location);
        btn_chat = findViewById(R.id.btn_chat);
        btn_exchange = findViewById(R.id.btn_exchange);
        iv_category = findViewById(R.id.iv_category);
        iv_ex_category = findViewById(R.id.iv_ex_category);
        rb_post = findViewById(R.id.rb_post);

        fragmentmap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        postid = getIntent().getStringExtra("postid");
        check = getIntent().getIntExtra("check",0);
        AddedItemDescriptionModel model = (AddedItemDescriptionModel) getIntent().getSerializableExtra("model");

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(postid).addListenerForSingleValueEvent(new ValueEventListener() {
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


                FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(postid).addListenerForSingleValueEvent(new ValueEventListener() {
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
        FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddedItemDescriptionModel object = snapshot.getValue(AddedItemDescriptionModel.class);

                item_name = object.getName();

                tv_age.setText(object.getAgeOfProduct());
                tv_title.setText(object.getName());
                tv_location.setText(object.getAdress1()+", "+object.getAdress2());
                tv_description.setText(object.getDescription());
                rb_post.setRating(Float.parseFloat(object.getRatings()));
                rb_post.setIsIndicator(true);

                location = object.getAdress1()+", "+object.getAdress2();

                setImageforCategory(object.getCateogary(), iv_category);
                setImageforCategory(object.getExchangeCateogary(), iv_ex_category);



                pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {

                        seletedIndicator(position);
                        super.onPageSelected(position);
                    }


                });

                List<Address>[] addressList = new List[]{new ArrayList<Address>()};

               /* fragmentmap.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        //   mGoogleApiClient.connect();
                        map = googleMap;
                        if(location!=null || !location.equals("")){
                            Geocoder geocoder = new Geocoder(ItemDetailActivity.this);
                            try {
                                addressList[0] = geocoder.getFromLocationName(location, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Address address = (Address) addressList[0].get(0);

                            double latfield = address.getLatitude();
                            double longfield = address.getLongitude();

                            map.addMarker(new MarkerOptions().position(new LatLng(latfield, longfield)));
                            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latfield, longfield)));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(latfield, longfield)), 15.0f));
                        }

                    }
                });*/

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uri_list = new ArrayList<>();

        database.getReference().child("post_files").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String var = dataSnapshot.getValue(String.class);
                    uri_list.add(var);
                }
                dots = new TextView[uri_list.size()];

                dotsIndicator();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check!=0) {
                    // Toast.makeText(ItemDetailActivity.this, postid, Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                // Toast.makeText(ItemDetailActivity.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                                String x = dataSnapshot.child("postid").getValue(String.class);
                                //Toast.makeText(ItemDetailActivity.this, x, Toast.LENGTH_SHORT).show();
                                // Toast.makeText(ItemDetailActivity.this, postid, Toast.LENGTH_SHORT).show();
                                if (x.equals(postid)) {

                                    ct = dataSnapshot.child("cateogary").getValue(String.class);
                                    Toast.makeText(ItemDetailActivity.this, ct, Toast.LENGTH_SHORT).show();
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
                        FirebaseDatabase.getInstance().getReference().child("postidwirhuserid").child(postid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    user_uid = dataSnapshot.getValue().toString();
                                    FirebaseDatabase.getInstance().getReference().child("users").child(user_uid).child("name").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            user_name = snapshot.getValue().toString();
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Exchange Requests").child(user_uid).push();
                                            ExchangeModel exchangeModel = new ExchangeModel(user_name, client_name, user_uid, client_uid, item_name, postid, "");
                                            String req_id = databaseReference.getKey().toString();
                                            exchangeModel.setRequest_uid(req_id);
                                            databaseReference.setValue(exchangeModel);
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




        adapter = new SliderAdapter(uri_list,this);
        pager2.setAdapter(adapter);

//       if(rb_post.getRating()<1.6){
//           rb_post.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
//       }
//       else if(rb_post.getRating()<3.6){
//           rb_post.getProgressDrawable().setColorFilter(getResources().getColor(R.color.gold), PorterDuff.Mode.SRC_ATOP);
//       }
//       else{
//           rb_post.getProgressDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
//       }

    }

    private void seletedIndicator(int position) {
        for(int i=0;i<dots.length;i++ ){
            if(i==position){
                dots[i].setTextColor(getResources().getColor(R.color.purple_500));
            }
            else{
                //dots[i].setTextColor(getResources().getColor(R.color.browser_actions_bg_grey));
            }
        }
    }

    private void dotsIndicator() {
        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#9679"));
            dots[i].setTextSize(18);
            dotsLayout.addView(dots[i]);
        }
    }

    public void setImageforCategory(String s, ImageView iv){
        if(s.equals("CAR")) {
            iv.setImageResource(R.drawable.img_car);
        }
        else if(s.equals("MOBILES")) {
            iv.setImageResource(R.drawable.mobiles_png);
        }
        else if(s.equals("CYCLES")) {
            iv.setImageResource(R.drawable.cycles_png);
        }
        else if(s.equals("BIKES")) {
            iv.setImageResource(R.drawable.bike_img);
        }
        else if(s.equals("ELECTRONIC ITEMS")) {
            iv.setImageResource(R.drawable.electronic);
        }
        else if(s.equals("TV")) {
            iv.setImageResource(R.drawable.tv_img);
        }
        else if(s.equals("LAPTOP")) {
            iv.setImageResource(R.drawable.laptop_img);
        }
        else if(s.equals("FURNITURE")) {
            iv.setImageResource(R.drawable.furniture_img);
        }
        else if(s.equals("BOOKS")) {
            iv.setImageResource(R.drawable.bike_img);
        }
        else if(s.equals("CLOTHES")) {
            iv.setImageResource(R.drawable.clothes);
        }
        else{
            iv.setImageResource(R.drawable.ic_gallery);
        }
    }
}