package com.krishna.team_olive;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.krishna.team_olive.SendNotificationPack.Token;
import com.mapbox.services.android.navigation.ui.v5.NavigationButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FragmentHome extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    int count_try;

    int CAT_VS_POSTS = 1;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView iv_navigationDrawer;
    LinearLayout contentView;

    private Dialog dialog;

    static final float END_SCALE = 0.7f;

    FloatingActionButton f_donate, f_exchange;
    FloatingActionsMenu  fab_parent;

    private RecyclerView recyclerView_posts;
    private PostAdapter postAdapter;
    public static ArrayList<AddedItemDescriptionModel> addedItemDescriptionModelArrayList;
    public static ArrayList<AddedItemDescriptionModel> addedItemDescriptionModelArrayList2;
    String isNGO ;
    LinearLayout post_click;

    Context context;
    private ImageView iv_profileButton;
    LinearLayout cat_car, cat_mobile, cat_cycle, cat_bike, cat_eleitems, cat_tv, cat_laptop, cat_furniture, cat_books, cat_clothes, cat_others;

    ShimmerFrameLayout shimmerFrameLayout;
    /*@Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }*/

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context=view.getContext();

        UpdateToken();

        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.navigation_view);
        iv_navigationDrawer = view.findViewById(R.id.iv_navigationDrawer);
        contentView = view.findViewById(R.id.content);

        navigationDrawer();

        iv_profileButton = view.findViewById(R.id.iv_profileButton);

        fab_parent = view.findViewById(R.id.fam_home);
        f_donate = view.findViewById(R.id.floatingActionButton2);
        f_exchange = view.findViewById(R.id.floatingActionButton3);

        dialog = new Dialog(getContext());

        post_click = view.findViewById(R.id.post_click);

        cat_car = view.findViewById(R.id.cat_car);
        cat_mobile = view.findViewById(R.id.cat_mobile);
        cat_cycle = view.findViewById(R.id.cat_cycle);
        cat_bike = view.findViewById(R.id.cat_bike);
        cat_eleitems= view.findViewById(R.id.cat_eleitems);
        cat_tv = view.findViewById(R.id.cat_tv);
        cat_laptop = view.findViewById(R.id.cat_laptop);
        cat_furniture = view.findViewById(R.id.cat_furniture);
        cat_books = view.findViewById(R.id.cat_books);
        cat_clothes = view.findViewById(R.id.cat_accecories);
        cat_others = view.findViewById(R.id.cat_others);

        recyclerView_posts = view.findViewById(R.id.recyclerview_posts);
        recyclerView_posts.setHasFixedSize(true);

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);

        shimmerFrameLayout.startShimmerAnimation();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView_posts.setLayoutManager(linearLayoutManager);

        addedItemDescriptionModelArrayList = new ArrayList<>();
        addedItemDescriptionModelArrayList2= new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        postAdapter = new PostAdapter(recyclerView_posts,getContext(),addedItemDescriptionModelArrayList2);
        recyclerView_posts.setAdapter(postAdapter);

        fab_parent.setVisibility(View.INVISIBLE);

        f_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddedItemDetailFilling_0.class);
                intent.putExtra("donate","N");
                startActivity(intent);
            }
        });

        f_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddedItemDetailFilling_0.class);
                intent.putExtra("donate","Y");
                startActivity(intent);
            }
        });

        iv_profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserProfileActivity.class));
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

//                Toast.makeText(getActivity().getApplicationContext(), users.getIsNGO(), Toast.LENGTH_SHORT).show();

                if(users.getIsNGO().equals("Yes")){
                    Menu menu = navigationView.getMenu();
                    menu.findItem(R.id.nav_myAdds).setVisible(false);
                    menu.findItem(R.id.nav_exchangeRequests).setVisible(false);
                    countTotal();
                    startPostsNGO();
                }
                else{
                    fab_parent.setVisibility(View.VISIBLE);
                    countTotal();
                    startPostsnonNGO();
                }
            }
            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        postAdapter.setiLoadMore(new ILoadMore() {
            @Override
            public void LoadMore() {

                if(addedItemDescriptionModelArrayList2.size() <= count_try){
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    addedItemDescriptionModelArrayList2.add(null);
                    postAdapter.notifyItemInserted(addedItemDescriptionModelArrayList2.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addedItemDescriptionModelArrayList2.remove(addedItemDescriptionModelArrayList2.size() - 1);
                            postAdapter.notifyItemRemoved(addedItemDescriptionModelArrayList2.size());

                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange( DataSnapshot snapshot) {
                                    Users users = snapshot.getValue(Users.class);

                                    if(users.getIsNGO().equals("Yes")){
                                        showNGOposts();
                                    }else{
                                        shownonNGOPosts();
                                    }
                                }
                                @Override
                                public void onCancelled( DatabaseError error) {
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    },4000);
                }else{
                    Toast.makeText(getContext(), "Complete data loaded!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cat_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("OTHERS");
                        }else{
                            showcatlistnonNGO("OTHERS");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("CAR");
                        }else{
                            showcatlistnonNGO("CAR");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("CLOTHES");
                        }else{
                            showcatlistnonNGO("CLOTHES");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("CYCLES");
                        }else{
                            showcatlistnonNGO("CYCLES");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("BOOKS");
                        }else{
                            showcatlistnonNGO("BOOKS");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("FURNITURE");
                        }else{
                            showcatlistnonNGO("FURNITURE");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_eleitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("ELECTRONIC ITEMS");
                        }else{
                            showcatlistnonNGO("ELECTRONIC ITEMS");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("MOBILES");
                        }else{
                            showcatlistnonNGO("MOBILES");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("BIKES");
                        }else{
                            showcatlistnonNGO("BIKES");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("TV");
                        }else{
                            showcatlistnonNGO("TV");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cat_laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CAT_VS_POSTS = 2;
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if(users.getIsNGO().equals("Y")){
                            showcatlistNGO("LAPTOP");
                        }else{
                            showcatlistnonNGO("LAPTOP");
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }


    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        iv_navigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setCheckedItem(R.id.nav_home);
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        //drawerLayout.setScrimColor(getResources().getColor(R.color.gold));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);

            }
        });
    }
    /*
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentHome()).commit();
                break;
            case R.id.nav_myAdds:
                startActivity(new Intent(getContext(),MyAdds.class));
                break;
            case R.id.nav_history:
                startActivity(new Intent(getContext(),History.class));
                break;
            case R.id.nav_exchangeRequests:
                startActivity(new Intent(getContext(),ExchangeRequest.class));
                break;
            case R.id.nav_savedPosts:
                startActivity(new Intent(getContext(),MyFavorite.class));
                break;
            case R.id.nav_help:
                startActivity(new Intent(getContext(),Help.class));
                break;
            case R.id.nav_feedback:
                startActivity(new Intent(getContext(),Feedback.class));
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(getContext(),AboutUs.class));
                break;
            case R.id.nav_logOut:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        dialog.setContentView(R.layout.logoout_alert_dialouge);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_no = dialog.findViewById(R.id.et_noLogout);
        TextView tv_yes = dialog.findViewById(R.id.et_yesLogout);
        dialog.show();
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void countTotal(){
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count_try = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    count_try++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showcatlistnonNGO(String cate){
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelArrayList2.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object ;

                    object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    if(object.getCateogary().equals(cate)){
                        addedItemDescriptionModelArrayList2.add(object);
                    }
                }
                if(addedItemDescriptionModelArrayList2.size()==0){
                    Toast.makeText(getContext(), "Sorry no item available !!", Toast.LENGTH_SHORT).show();
                    addedItemDescriptionModelArrayList2.add(null);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showcatlistNGO(String cate){
        FirebaseDatabase.getInstance().getReference().child("NGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelArrayList2.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object ;

                    object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    if(object.getCateogary().equals(cate)){
                        addedItemDescriptionModelArrayList2.add(object);
                    }
                }
                if(addedItemDescriptionModelArrayList2.size()==0){
                    Toast.makeText(getContext(), "Sorry no item available !!", Toast.LENGTH_SHORT).show();
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startPostsnonNGO(){

        FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByValue().limitToLast(11).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> cat = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cat.add(dataSnapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        int end = 1;
                        int i=0;
                        addedItemDescriptionModelArrayList.clear();
                        for (DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {
                            AddedItemDescriptionModel object;
                            object = dataSnapshot2.getValue(AddedItemDescriptionModel.class);
                            addedItemDescriptionModelArrayList.add(object);
                        }
                        addedItemDescriptionModelArrayList2.clear();
                        for (int a = 0; a < cat.size(); a++) {
                            String x = cat.get(a);

                            for (int j = 0; j < addedItemDescriptionModelArrayList.size(); j++) {
                                if (addedItemDescriptionModelArrayList.get(j).getCateogary().equals(x)) {
                                    if(i == end) {
                                        break;
                                    }
                                    i++;
                                    addedItemDescriptionModelArrayList2.add(addedItemDescriptionModelArrayList.get(j));
                                }
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                        postAdapter.setLoaded();
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

        /*
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                addedItemDescriptionModelArrayList2.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(i==4)
                        break;
                    AddedItemDescriptionModel object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    addedItemDescriptionModelArrayList2.add(object);
                    i++;
                }
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */
    }

    private void startPostsNGO(){

        FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByValue().limitToLast(11).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> cat = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cat.add(dataSnapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("NGOposts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        int i=0;
                        addedItemDescriptionModelArrayList.clear();
                        for (DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {
                            AddedItemDescriptionModel object;
                            object = dataSnapshot2.getValue(AddedItemDescriptionModel.class);
                            addedItemDescriptionModelArrayList.add(object);
                        }
                        addedItemDescriptionModelArrayList2.clear();
                        for (int a = 0; a < cat.size(); a++) {
                            String x = cat.get(a);

                            for (int j = 0; j < addedItemDescriptionModelArrayList.size(); j++) {
                                if (addedItemDescriptionModelArrayList.get(j).getCateogary().equals(x)) {
                                    if(i == 1) {
                                        break;
                                    }
                                    i++;
                                    addedItemDescriptionModelArrayList2.add(addedItemDescriptionModelArrayList.get(j));
                                }
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                        postAdapter.setLoaded();
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

        /*
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                addedItemDescriptionModelArrayList2.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(i==4)
                        break;
                    AddedItemDescriptionModel object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    addedItemDescriptionModelArrayList2.add(object);
                    i++;
                }
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */
    }

    private void shownonNGOPosts() {
        FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByValue().limitToLast(11).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> cat = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cat.add(dataSnapshot.getKey());

                }

                FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        int i=0;
                        int start = addedItemDescriptionModelArrayList2.size();
                        int end = start + 4;
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        addedItemDescriptionModelArrayList.clear();
                        for (DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {
                            AddedItemDescriptionModel object;
                            object = dataSnapshot2.getValue(AddedItemDescriptionModel.class);

                            addedItemDescriptionModelArrayList.add(object);
                        }
                        for (int a = 0; a < cat.size(); a++) {
                            String x = cat.get(a);

                            for (int j = 0; j < addedItemDescriptionModelArrayList.size(); j++) {
                                if (addedItemDescriptionModelArrayList.get(j).getCateogary().equals(x)) {
                                    if(i == end){
                                        break;
                                    }
                                    if(i < start){
                                        i++;
                                        continue;
                                    }
                                    i++;
                                    addedItemDescriptionModelArrayList2.add(addedItemDescriptionModelArrayList.get(j));

                                }
                            }
                        }

                        postAdapter.notifyDataSetChanged();
                        postAdapter.setLoaded();
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

    private void showNGOposts() {
        FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByValue().limitToLast(11).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> cat = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cat.add(dataSnapshot.getKey());

                }

                FirebaseDatabase.getInstance().getReference().child("NGOposts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        int i=0;
                        int start = addedItemDescriptionModelArrayList2.size();
                        int end = start + 4;
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        addedItemDescriptionModelArrayList.clear();
                        for (DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {
                            AddedItemDescriptionModel object;
                            object = dataSnapshot2.getValue(AddedItemDescriptionModel.class);

                            addedItemDescriptionModelArrayList.add(object);
                        }
                        for (int a = 0; a < cat.size(); a++) {
                            String x = cat.get(a);
                            for (int j = 0; j < addedItemDescriptionModelArrayList.size(); j++) {
                                if (addedItemDescriptionModelArrayList.get(j).getCateogary().equals(x)) {
                                    if(i == end){
                                        break;
                                    }
                                    if(i < start){
                                        i++;
                                        continue;
                                    }
                                    i++;
                                    addedItemDescriptionModelArrayList2.add(addedItemDescriptionModelArrayList.get(j));
                                }
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                        postAdapter.setLoaded();
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

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

    }
}


