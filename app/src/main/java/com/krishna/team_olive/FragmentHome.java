package com.krishna.team_olive;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FragmentHome extends Fragment {

    List<String> count_try;

    private RecyclerView recyclerView_posts;
    private PostAdapter postAdapter;
    public static ArrayList<AddedItemDescriptionModel> addedItemDescriptionModelArrayList;
    public static ArrayList<AddedItemDescriptionModel> addedItemDescriptionModelArrayList2;
    RelativeLayout search_bar;
    String isNGO ;
    LinearLayout post_click;
    FirebaseAuth auth;
    Context context;
    LinearLayout cat_car, cat_mobile, cat_cycle, cat_bike, cat_eleitems, cat_tv, cat_laptop, cat_furniture, cat_books, cat_clothes, cat_others;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context=view.getContext();

        count_try = new ArrayList<>();

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);

        recyclerView_posts.setLayoutManager(linearLayoutManager);

        addedItemDescriptionModelArrayList = new ArrayList<>();
        addedItemDescriptionModelArrayList2= new ArrayList<>();

        search_bar = (RelativeLayout) view.findViewById(R.id.rl_search_bar);
        auth = FirebaseAuth.getInstance();
/*
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                if(users.getIsNGO().equals("Y")){
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


 */
        countTotal();
        startPosts();
        //shownonNGOPosts();

        postAdapter = new PostAdapter(recyclerView_posts,getContext(),addedItemDescriptionModelArrayList2);
        recyclerView_posts.setAdapter(postAdapter);

        postAdapter.setiLoadMore(new ILoadMore() {
            @Override
            public void LoadMore() {

                if(addedItemDescriptionModelArrayList2.size() <= count_try.size()){
                    addedItemDescriptionModelArrayList2.add(null);
                    postAdapter.notifyItemInserted(addedItemDescriptionModelArrayList2.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addedItemDescriptionModelArrayList2.remove(addedItemDescriptionModelArrayList2.size() - 1);
                            postAdapter.notifyItemRemoved(addedItemDescriptionModelArrayList2.size());

                            //Random more data
                            shownonNGOPosts();
                        }
                    },5000);
                }else{
                    Toast.makeText(getContext(), "Complete data loaded!!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

        cat_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("OTHERS");
                }else{
                    showcatlistnonNGO("OTHERS");
                }
            }
        });

        cat_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("CAR");
                }else{
                    showcatlistnonNGO("CAR");
                }
            }
        });

        cat_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("CLOTHES");
                }else{
                    showcatlistnonNGO("CLOTHES");
                }
            }
        });

        cat_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("CYCLES");
                }else{
                    showcatlistnonNGO("CYCLES");
                }
            }
        });

        cat_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("BOOKS");
                }else{
                    showcatlistnonNGO("BOOKS");
                }
            }
        });

        cat_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("FURNITURE");
                }else{
                    showcatlistnonNGO("FURNITURE");
                }
            }
        });

        cat_eleitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("ELECTRONIC ITEMS");
                }else{
                    showcatlistnonNGO("ELECTRONIC ITEMS");
                };
            }
        });

        cat_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("MOBILES");
                }else{
                    showcatlistnonNGO("MOBILES");
                }
            }
        });

        cat_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("CYCLES");
                }else{
                    showcatlistnonNGO("CYCLES");
                }
            }
        });

        cat_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("BIKES");
                }else{
                    showcatlistnonNGO("BIKES");
                }
            }
        });

        cat_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("TV");
                }else{
                    showcatlistnonNGO("TV");
                }
            }
        });

        cat_laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNGO == "Y"){
                    showcatlistNGO("LAPTOP");
                }else{
                    showcatlistnonNGO("LAPTOP");
                }
            }
        });

        return view;
    }

    private void showcatlistnonNGO(String cate){
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object ;

                    object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    if(object.getCateogary().equals(cate)){
                        addedItemDescriptionModelArrayList.add(object);
                        if(addedItemDescriptionModelArrayList.size()==0){
                            Toast.makeText(getContext(), "Sorry no item available !!", Toast.LENGTH_SHORT).show();
                        }
                    }
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
                addedItemDescriptionModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object ;

                    object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    if(object.getCateogary().equals(cate)){
                        addedItemDescriptionModelArrayList.add(object);
                        if(addedItemDescriptionModelArrayList.size()==0){
                            Toast.makeText(getContext(), "Sorry no item available !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countTotal(){
        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count_try.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object ;
                    object = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                    count_try.add(object.getPostid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startPosts(){

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
                List<String> cat=new ArrayList<>();

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    cat.add(dataSnapshot.getKey());

                }

                // Toast.makeText(context, ""+cat.size(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("NGOposts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        addedItemDescriptionModelArrayList.clear();
                        for(DataSnapshot dataSnapshot2 : snapshot2.getChildren()){
                            AddedItemDescriptionModel object ;
                            object = dataSnapshot2.getValue(AddedItemDescriptionModel.class);
                            addedItemDescriptionModelArrayList.add(object);
                        }
                        addedItemDescriptionModelArrayList2.clear();
                        for(int i=0;i<cat.size();i++)
                        {
                            String x=cat.get(i);
                            // Toast.makeText(context, x, Toast.LENGTH_SHORT).show();
                            //
                            for(int j=0;j<addedItemDescriptionModelArrayList.size();j++)
                            {
                                if(addedItemDescriptionModelArrayList.get(j).getCateogary().equals(x))
                                {

                                    addedItemDescriptionModelArrayList2.add(addedItemDescriptionModelArrayList.get(j));
                                }
                            }
                        }
                        postAdapter.notifyDataSetChanged();
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



       /* FirebaseDatabase.getInstance().getReference().child("NGOposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addedItemDescriptionModelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AddedItemDescriptionModel object = snapshot.getValue(AddedItemDescriptionModel.class);
                    addedItemDescriptionModelArrayList.add(object);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
}