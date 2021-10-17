package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResponse;
//import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SearchActivity extends AppCompatActivity {


    private RecyclerView recyclerView_search;
    private EditText searchbar2;
    private List<AddedItemDescriptionModel> search_list;
    private SearchAdapter searchAdapter;
    private FirebaseAuth auth;
    private String isNGO;
    private LocationRequest locationRequest;
    private double my_latitude, my_longitude, add_latitude, add_longitude;

    List<Address>[] addressList ;

    CheckBox check_ratings, check_distance;

    TreeMap<Double,String> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        auth = FirebaseAuth.getInstance();

        recyclerView_search = findViewById(R.id.recyclerview_search);
        searchbar2 = findViewById(R.id.search_bar2);
        check_distance = findViewById(R.id.check_distance);
        check_ratings = findViewById(R.id.check_ratings);

        recyclerView_search.setHasFixedSize(true);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        map = new TreeMap<>();

        addressList = new List[]{new ArrayList<Address>()};

        search_list = new ArrayList<>();
        searchAdapter = new SearchAdapter(SearchActivity.this,search_list,true);
        recyclerView_search.setAdapter(searchAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("isNGO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                        isNGO = snapshot.getValue().toString();
                        if(isNGO.equals("Y")){
                            searchNGOposts();
                        }else{
                            searchnonNGOposts();
                        }
                    }
            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchbar2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isNGO.equals("Y")){
                    searchdetailNGO(s.toString());
                }else{
                    searchdetailnonNGO(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        check_ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_ratings.isChecked()){
                    Collections.sort(search_list, new Comparator<AddedItemDescriptionModel>(){
                        public int compare(AddedItemDescriptionModel obj1, AddedItemDescriptionModel obj2) {
                            AddedItemDescriptionModel a1 = (AddedItemDescriptionModel) obj1;
                            AddedItemDescriptionModel a2 = (AddedItemDescriptionModel) obj2;
                            return  Float.compare(Float.parseFloat(a2.getRatings()),Float.parseFloat(a1.getRatings()));
                        }
                    });
                    searchAdapter.notifyDataSetChanged();
                }else{
                    Collections.sort(search_list, new Comparator<AddedItemDescriptionModel>(){
                        public int compare(AddedItemDescriptionModel obj1, AddedItemDescriptionModel obj2) {
                            AddedItemDescriptionModel a1 = (AddedItemDescriptionModel) obj1;
                            AddedItemDescriptionModel a2 = (AddedItemDescriptionModel) obj2;
                            return  Float.compare(Float.parseFloat(a1.getRatings()),Float.parseFloat(a2.getRatings()));
                        }
                    });
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);




        check_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            AddedItemDescriptionModel objc = dataSnapshot.getValue(AddedItemDescriptionModel.class);

                            String temp_add = objc.getAdress1() + ", " + objc.getAdress2();

                            GeoCodingLocation locationAddress = new GeoCodingLocation();
                            locationAddress.getAddressFromLocation(temp_add, getApplicationContext(), new GeoCoderHandler());
                            double dist = distance(my_latitude, add_latitude, my_longitude, add_longitude);
                            map.put(dist,objc.getPostid());
                        }
                        if(map.size() == 1){
                            Toast.makeText(SearchActivity.this, "KLJKJK", Toast.LENGTH_SHORT).show();
                        }
                        search_list.clear();
                        for (Map.Entry<Double, String> entry : map.entrySet()) {
                            FirebaseDatabase.getInstance().getReference().child("allpostswithoutuser").child(entry.getValue()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    AddedItemDescriptionModel add_list = snapshot.getValue(AddedItemDescriptionModel.class);
                                    //Toast.makeText(SearchActivity.this, add_list.getName(), Toast.LENGTH_SHORT).show();
                                    search_list.add(add_list);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //if(search_list.size() == 0)
                            //Toast.makeText(SearchActivity.this, "KLJKJK", Toast.LENGTH_SHORT).show();
                        }
                        searchAdapter.notifyDataSetChanged();
                        //if(search_list.size() == 0)
                        //Toast.makeText(SearchActivity.this, "KLJKJK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
        //Extra Methods
        private class GeoCoderHandler extends Handler {
            @Override
            public void handleMessage(Message message) {
                String locationAddress;
                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        add_latitude = bundle.getDouble("lati");
                        add_longitude = bundle.getDouble("longi");
                        //if(add_longitude != 0.0 && add_latitude != 0.0)
                        //Toast.makeText(SearchActivity.this, "Helllllll", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        locationAddress = null;
                }
                //textViewLatLong.setText(locationAddress);
            }
        }


/*
      void getAddress(String loc_add){
        Thread thread = new Thread(){
            @Override
            public  void run(){
                Geocoder geocoder = new Geocoder(SearchActivity.this, Locale.getDefault());
                String res = null;
                try{
                    List addressList = geocoder.getFromLocationName(loc_add,1);
                    Toast.makeText(SearchActivity.this, "MINUGaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                    if(addressList != null && addressList.size() > 0){
                        Address address = (Address)addressList.get(0);
                        add_latitude = address.getLatitude();
                        add_longitude = address.getLongitude();
                        Toast.makeText(SearchActivity.this, "MINUGsssssss", Toast.LENGTH_SHORT).show();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
    }

 */
/*
    public GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address = new ArrayList<>() ;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            add_latitude = location.getLatitude();
            add_longitude = location.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

 */

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(SearchActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(SearchActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        my_latitude = locationResult.getLocations().get(index).getLatitude();
                                        my_longitude = locationResult.getLocations().get(index).getLongitude();
                                        ///////////////////////////
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(SearchActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(SearchActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }


        private void searchdetailnonNGO(String s){
            Query query = FirebaseDatabase.getInstance().getReference().child("nonNGOposts").orderByChild("name").startAt(s).endAt(s + "\uf8ff");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    search_list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        AddedItemDescriptionModel objes = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                        search_list.add(objes);
                    }
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void searchdetailNGO(String s){
            Query query = FirebaseDatabase.getInstance().getReference().child("NGOposts").orderByChild("name").startAt(s).endAt(s + "\uf8ff");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    search_list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        AddedItemDescriptionModel objes = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                        search_list.add(objes);
                    }
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void searchnonNGOposts() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("nonNGOposts");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    search_list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        AddedItemDescriptionModel obje = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                        search_list.add(obje);
                    }
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void searchNGOposts() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NGOposts");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    search_list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        AddedItemDescriptionModel obje = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                        search_list.add(obje);
                    }
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
}