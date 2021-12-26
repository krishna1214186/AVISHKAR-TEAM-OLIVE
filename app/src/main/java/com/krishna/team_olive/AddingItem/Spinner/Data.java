package com.krishna.team_olive.AddingItem.Spinner;

//import com.example.customspinners.R;

import com.krishna.team_olive.R;

import java.util.ArrayList;
import java.util.List;

//This class contains layout of items for spinner present in AddedItemDetailFilling_3

public class Data {

    public static List<Spinner> getItemList() {
        List<Spinner> itemList = new ArrayList<>();

        Spinner Car = new Spinner();
        Car.setName("Car");
        Car.setImage(R.drawable.car2);
        itemList.add(Car);

        Spinner MotorBike = new Spinner();
        MotorBike.setName("Motor Bike");
        MotorBike.setImage(R.drawable.motorcycle);
        itemList.add(MotorBike);

        Spinner Cycle = new Spinner();
        Cycle.setName("Cycle");
        Cycle.setImage(R.drawable.bicycle);
        itemList.add(Cycle);

        Spinner Mobile = new Spinner();
        Mobile.setName("Mobile");
        Mobile.setImage(R.drawable.mobile1);
        itemList.add(Mobile);

        Spinner EleItem = new Spinner();
        EleItem.setName("Electronics Item");
        EleItem.setImage(R.drawable.electronicitems);
        itemList.add(EleItem);

        Spinner Tv = new Spinner();
        Tv.setName("Tv");
        Tv.setImage(R.drawable.tv1);
        itemList.add(Tv);

        Spinner Laptop = new Spinner();
        Laptop.setName("Laptop");
        Laptop.setImage(R.drawable.laptop);
        itemList.add(Laptop);

        Spinner Furniture = new Spinner();
        Furniture.setName("Furniture");
        Furniture.setImage(R.drawable.furniture1);
        itemList.add(Furniture);

        Spinner Books = new Spinner();
        Books.setName("Books");
        Books.setImage(R.drawable.books);
        itemList.add(Books);

        Spinner Clothes = new Spinner();
        Clothes.setName("Clothes");
        Clothes.setImage(R.drawable.clothes1);
        itemList.add(Clothes);

        return itemList;
    }

}