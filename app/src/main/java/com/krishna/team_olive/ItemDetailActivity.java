package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    LinearLayout dotsLayout;
    SliderAdapter adapter;
    ViewPager2 pager2;
    ArrayList<String> list;
    TextView[] dots;

    TextView tv_title, tv_age, tv_description, tv_location;
    Button btn_chat,btn_exchange;
    ImageView iv_category, iv_ex_category;
    RatingBar rb_post;

    SupportMapFragment fragmentmap;
    GoogleMap map;

    String postid;

    FirebaseDatabase database;
    FirebaseAuth auth;

    String location;


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
        AddedItemDescriptionModel model = (AddedItemDescriptionModel) getIntent().getSerializableExtra("model");

        list = new ArrayList<>();


        FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddedItemDescriptionModel object = snapshot.getValue(AddedItemDescriptionModel.class);


                tv_age.setText(object.getAgeOfProduct());
                tv_title.setText(object.getName());
                tv_location.setText(object.getAdress1()+", "+object.getAdress2());
                tv_description.setText(object.getDescription());
                rb_post.setRating(Float.parseFloat(object.getRatings()));
                rb_post.setIsIndicator(true);

                location = object.getAdress1()+", "+object.getAdress2();

                setImageforCategory(object.getCateogary(), iv_category);
                setImageforCategory(object.getExchangeCateogary(), iv_ex_category);

                list.add(object.getImageurl());


                dots = new TextView[list.size()];

                dotsIndicator();



                adapter = new SliderAdapter(list);
                pager2.setAdapter(adapter);

                pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {

                        seletedIndicator(position);
                        super.onPageSelected(position);
                    }


                });

                List<Address>[] addressList = new List[]{new ArrayList<Address>()};

                fragmentmap.getMapAsync(new OnMapReadyCallback() {
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
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                dots[i].setTextColor(getResources().getColor(R.color.browser_actions_bg_grey));
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
        else if(s.equals("CLOTHERS")) {
            iv.setImageResource(R.drawable.clothes);
        }
        else{
            iv.setImageResource(R.drawable.ic_gallery);
        }
    }
}