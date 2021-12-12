package com.krishna.team_olive;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class FragmentAdd extends Fragment {

CardView cars,mobiles,cycles,bikes,electronicItems,tv,laptop,furniture,books,clothes,others;
ItemSelected activity;
public interface ItemSelected
{
    void onItemSelected(String str);
}

    public FragmentAdd() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=(ItemSelected) context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_add, container, false);
        cars=v.findViewById(R.id.car);
        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
activity.onItemSelected("CARS");
            }
        });
        cycles=v.findViewById(R.id.CYCLES);
        cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("CYCLES");
            }
        });
       mobiles =v.findViewById(R.id.mobiles);
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("MOBILES");
            }
        });
        bikes=v.findViewById(R.id.BIKES);
        bikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("BIKES");
            }
        });
        electronicItems=v.findViewById(R.id.ELECTRONIC_ITEMS);
        electronicItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("ELECTRONICITEMS");
            }
        });
        tv=v.findViewById(R.id.TV);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("TV");
            }
        });
        laptop=v.findViewById(R.id.LAPTOP);
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("LAPTOP");
            }
        });
        furniture=v.findViewById(R.id.FURNITURE);
        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("FURNITURE");
            }
        });
        books=v.findViewById(R.id.BOOKS);
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("BOOKS");
            }
        });
        clothes=v.findViewById(R.id.CLOTHES);
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("CLOTHES");
            }
        });
        others=v.findViewById(R.id.OTHERS);
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemSelected("OTHERS");
            }
        });


        return v;
    }



}