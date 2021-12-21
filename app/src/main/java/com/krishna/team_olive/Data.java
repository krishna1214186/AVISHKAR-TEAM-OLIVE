package com.krishna.team_olive;

//import com.example.customspinners.R;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public static List<Spinner> getFruitList() {
        List<Spinner> fruitList = new ArrayList<>();

        Spinner Car = new Spinner();
        Car.setName("Car");
        Car.setImage(R.drawable.car2);
        fruitList.add(Car);

        Spinner MotorBike = new Spinner();
        MotorBike.setName("Motor Bike");
        MotorBike.setImage(R.drawable.motorcycle);
        fruitList.add(MotorBike);

        Spinner Cycle = new Spinner();
        Cycle.setName("Cycle");
        Cycle.setImage(R.drawable.bicycle);
        fruitList.add(Cycle);

        Spinner Mobile = new Spinner();
        Mobile.setName("Mobile");
        Mobile.setImage(R.drawable.mobile1);
        fruitList.add(Mobile);

        Spinner EleItem = new Spinner();
        EleItem.setName("Electronics Item");
        EleItem.setImage(R.drawable.electronicitems);
        fruitList.add(EleItem);

        Spinner Tv = new Spinner();
        Tv.setName("Tv");
        Tv.setImage(R.drawable.tv1);
        fruitList.add(Tv);

        Spinner Laptop = new Spinner();
        Laptop.setName("Laptop");
        Laptop.setImage(R.drawable.laptop);
        fruitList.add(Laptop);

        Spinner Furniture = new Spinner();
        Furniture.setName("Furniture");
        Furniture.setImage(R.drawable.furniture1);
        fruitList.add(Furniture);

        Spinner Books = new Spinner();
        Books.setName("Books");
        Books.setImage(R.drawable.books);
        fruitList.add(Books);

        Spinner Clothes = new Spinner();
        Clothes.setName("Clothes");
        Clothes.setImage(R.drawable.clothes1);
        fruitList.add(Clothes);

        return fruitList;
    }

}