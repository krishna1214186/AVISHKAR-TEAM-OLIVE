package com.krishna.team_olive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddedItemDetailFilling_0 extends AppCompatActivity {

    private CardView cars,mobiles,cycles,bikes,electronicItems,tv,laptop,furniture,books,clothes,others;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_item_detail_filling0);

        String donate = getIntent().getStringExtra("donate");

        cars = findViewById(R.id.car);
        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "CARS";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        cycles=findViewById(R.id.CYCLES);
        cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "CYCLES";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        mobiles =findViewById(R.id.mobiles);
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "MOBILES";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        bikes=findViewById(R.id.BIKES);
        bikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "BIKES";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        electronicItems=findViewById(R.id.ELECTRONIC_ITEMS);
        electronicItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "ELECTRONICITEMS";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        tv=findViewById(R.id.TV);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "TV";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        laptop=findViewById(R.id.LAPTOP);
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "LAPTOP";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        furniture=findViewById(R.id.FURNITURE);
        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "FURNITURE";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        books=findViewById(R.id.BOOKS);
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "BOOKS";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        clothes=findViewById(R.id.CLOTHES);
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "CLOTHES";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
        others=findViewById(R.id.OTHERS);
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "OTHERS";
                Intent intent=new Intent(AddedItemDetailFilling_0.this,AddedItemDetailFilling_1.class);
                intent.putExtra("cateogary_name",str);
                intent.putExtra("donate",donate);
                startActivity(intent);
            }
        });
    }
}