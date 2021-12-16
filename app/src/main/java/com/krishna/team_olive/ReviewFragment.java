package com.krishna.team_olive;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ReviewFragment extends Fragment {

    TextView tv_rating_prof;
    TextView tv_totrev_prof;
    RecyclerView rv_review;

    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<String> list;

    ReviewAdapter adapter;

    Context context;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        tv_rating_prof = view.findViewById(R.id.tv_prof_rating);
        tv_totrev_prof = view.findViewById(R.id.tv_prof_review);
        rv_review = view.findViewById(R.id.rv_review);

        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid().toString()).child("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_rating_prof.setText(String.valueOf((long) snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid().toString()).child("review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_totrev_prof.setText(String.valueOf( snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        context = getActivity().getApplicationContext();

        database.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid().toString()).child("review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    list.add(snapshot1.getValue(String.class));
                }
                rv_review.hasFixedSize();
                adapter =new ReviewAdapter(context,list);
                rv_review.setAdapter(adapter);

                LinearLayoutManager layoutmanager=new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                rv_review.setLayoutManager(layoutmanager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return view;
    }
}