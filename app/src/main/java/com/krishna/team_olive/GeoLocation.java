package com.krishna.team_olive;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

public class GeoLocation {

    public  static  void getAddress(String loc_add, Context context, Handler handler){
        Thread thread = new Thread(){
            @Override
            public  void run(){
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String res = null;
                try{
                    List addressList = geocoder.getFromLocationName(loc_add,1);
                    if(addressList != null && addressList.size() > 0){
                        Address address = (Address)addressList.get(0);

                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
    }
}
