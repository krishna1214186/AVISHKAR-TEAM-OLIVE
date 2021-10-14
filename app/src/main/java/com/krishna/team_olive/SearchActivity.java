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
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity {


    private RecyclerView recyclerView_search;
    private EditText searchbar2;
    private List<AddedItemDescriptionModel> search_list;
    private SearchAdapter searchAdapter;
    private FirebaseAuth auth;
    private String isNGO;
    private LocationRequest locationRequest;
    private double latitude, longitude;

    CheckBox check_ratings, check_distance;

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

        search_list = new ArrayList<>();
        searchAdapter = new SearchAdapter(SearchActivity.this,search_list,true);
        recyclerView_search.setAdapter(searchAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(snapshot.getKey() == "isNGO"){
                        isNGO = snapshot.getValue().toString();
                    }
                }
            }
            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(isNGO == "Y"){
            searchNGOposts();
        }else{
            searchnonNGOposts();
        }

        searchbar2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isNGO == "Y"){
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
            }
        });

    }
        //Extra Methods

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
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();
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