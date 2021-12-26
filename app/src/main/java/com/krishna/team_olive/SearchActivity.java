package com.krishna.team_olive;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
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
    private List<AddedItemDescriptionModel> search_list, distance_sort;
    private SearchAdapter searchAdapter;
    private FirebaseAuth auth;
    private String isNGO;
    private LocationRequest locationRequest;
    private double my_latitude, my_longitude, add_latitude, add_longitude;
    String temp_add;

    Geocoder gc ;
    List<Address> list ;

    CheckBox check_ratings, check_distance;

    TreeMap<Double,String> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        gc = new Geocoder(SearchActivity.this);
        list = null;
        map = new TreeMap<>();

        auth = FirebaseAuth.getInstance();

        recyclerView_search = findViewById(R.id.recyclerview_search);
        searchbar2 = findViewById(R.id.search_bar2);
        check_distance = findViewById(R.id.check_distance);
        check_ratings = findViewById(R.id.check_ratings);

        recyclerView_search.setHasFixedSize(true);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        search_list = new ArrayList<>();
        distance_sort = new ArrayList<>();

        searchAdapter = new SearchAdapter(SearchActivity.this,search_list,true);
        recyclerView_search.setAdapter(searchAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("isNGO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                isNGO = snapshot.getValue().toString();
                if(isNGO.equals("Yes")){
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
                FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("isNGO").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        isNGO = snapshot.getValue().toString();
                        if(isNGO.equals("Yes")){
                            searchdetailNGO(s.toString().toLowerCase());
                        }else{
                            searchdetailnonNGO(s.toString().toLowerCase());
                        }
                    }
                    @Override
                    public void onCancelled( DatabaseError error) {
                        Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
                for(int i=0; i<search_list.size();i++){
                    AddedItemDescriptionModel objc = search_list.get(i);
                    temp_add = objc.getAdress1() + ", " + objc.getAdress2();

                    if(gc.isPresent()) {
                        try {
                            list = gc.getFromLocationName(temp_add, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!list.isEmpty()) {
                            Address address = list.get(0);
                            add_latitude = address.getLatitude();
                            add_longitude = address.getLongitude();
                            //Log.d(TAG, "Lat: " + String.valueOf(lat) + ", Lng: " + String.valueOf(lng));
                        }
                    }

                    double dist = distance(my_latitude, add_latitude, my_longitude, add_longitude,0.0,0.0);
                    //Toast.makeText(SearchActivity.this, Integer.toString((int)dist), Toast.LENGTH_SHORT).show();
                    map.put(dist,objc.getPostid());
                }


                distance_sort.clear();

                if(isNGO.equals("Yes")) {

                    FirebaseDatabase.getInstance().getReference().child("Current NGO posts").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AddedItemDescriptionModel add_obj = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                                distance_sort.add(add_obj);
                            }
                            search_list.clear();
                            for (Map.Entry<Double, String> entry : map.entrySet()) {
                                for (int i = 0; i < distance_sort.size(); i++) {
                                    if (distance_sort.get(i).getPostid().equals(entry.getValue())) {
                                        search_list.add(distance_sort.get(i));
                                    }
                                }
                            }
                            searchAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Current non NGO posts").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AddedItemDescriptionModel add_obj = dataSnapshot.getValue(AddedItemDescriptionModel.class);
                                distance_sort.add(add_obj);
                            }
                            search_list.clear();
                            for (Map.Entry<Double, String> entry : map.entrySet()) {
                                for (int i = 0; i < distance_sort.size(); i++) {
                                    if (distance_sort.get(i).getPostid().equals(entry.getValue())) {
                                        search_list.add(distance_sort.get(i));
                                    }
                                }
                            }
                            searchAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                for (Map.Entry<Double, String> entry : map.entrySet()) {
                    searchAdapter.notifyDataSetChanged();
                }
                //Toast.makeText(SearchActivity.this, Integer.toString(search_list.size()), Toast.LENGTH_SHORT).show();

            }
        });

    }
    ////////////////////////////////////////////////////////////     EXTRA METHODS   ///////////////////////////////////////////////////////////////////////////////////////


    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
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
        Query query = FirebaseDatabase.getInstance().getReference().child("Current non NGO posts").orderByChild("search_title").startAt(s).endAt(s + "\uf8ff");
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
        Query query = FirebaseDatabase.getInstance().getReference().child("Current NGO posts").orderByChild("search_title").startAt(s).endAt(s + "\uf8ff");
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Current non NGO posts");
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Current NGO posts");
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