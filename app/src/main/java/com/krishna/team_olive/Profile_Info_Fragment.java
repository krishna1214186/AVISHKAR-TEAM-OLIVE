package com.krishna.team_olive;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.krishna.team_olive.SendNotificationPack.Data;

public class Profile_Info_Fragment extends Fragment {

    TextView tv_profile_name;
    TextView tv_profile_address;
    TextView tv_profile_phone;
    TextView tv_profile_email;
    TextView tv_prof_totpost;
    TextView tv_prof_rating;
    Button btn_edit_prof, btn_done_prof;


    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_profile__info_, container, false);

        tv_profile_address = view.findViewById(R.id.tv_address_prof);
        tv_profile_name = view.findViewById(R.id.tv_name_prof);
        tv_profile_phone = view.findViewById(R.id.tv_phone_prof);
        tv_profile_email = view.findViewById(R.id.tv_email_prof);
        tv_prof_rating = view.findViewById(R.id.tv_prof_rating);
        tv_prof_totpost = view.findViewById(R.id.tv_prof_totpost);
        btn_edit_prof = view.findViewById(R.id.btn_edit_prof);
        btn_done_prof = view.findViewById(R.id.btn_done_prof);

        btn_done_prof.setVisibility(View.INVISIBLE);
        btn_edit_prof.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();

        tv_profile_address.setFocusableInTouchMode(false);
        tv_profile_address.setCursorVisible(false);

        tv_profile_email.setFocusableInTouchMode(false);
        tv_profile_email.setCursorVisible(false);
        tv_profile_email.setFocusable(true);
        tv_profile_email.setEnabled(true);

        tv_profile_phone.setFocusableInTouchMode(false);
        tv_profile_phone.setCursorVisible(false);

        tv_profile_name.setFocusableInTouchMode(false);
        tv_profile_name.setCursorVisible(false);



        String uid = auth.getCurrentUser().getUid().toString();



        database.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid().toString()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        tv_profile_name.setText(users.getName());
                        tv_profile_address.setText(users.getLocation());
                        tv_profile_email.setText(users.getEmail());
                        tv_profile_phone.setText(users.getPhone());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );


        database.getInstance().getReference().child("mypostswithuser").child(auth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_prof_totpost.setText(String.valueOf((int) snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid().toString()).child("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_prof_rating.setText(String.valueOf((long) snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = database.getInstance().getReference().child("users").child(uid);



        btn_edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_done_prof.setVisibility(View.VISIBLE);
                btn_edit_prof.setVisibility(View.INVISIBLE);

                tv_profile_address.setFocusableInTouchMode(true);
                tv_profile_address.setCursorVisible(true);

                tv_profile_email.setFocusableInTouchMode(false);
                tv_profile_email.setCursorVisible(false);
                tv_profile_email.setFocusable(false);
                tv_profile_email.setEnabled(false);


                tv_profile_phone.setFocusableInTouchMode(true);
                tv_profile_phone.setCursorVisible(true);

                tv_profile_name.setFocusableInTouchMode(true);
                tv_profile_name.setCursorVisible(true);

            }
        });

        btn_done_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_done_prof.setVisibility(View.INVISIBLE);
                btn_edit_prof.setVisibility(View.VISIBLE);

                tv_profile_email.setFocusableInTouchMode(false);
                tv_profile_email.setCursorVisible(false);
                tv_profile_email.setFocusable(true);
                tv_profile_email.setEnabled(true);

                tv_profile_address.setFocusableInTouchMode(false);
                tv_profile_address.setCursorVisible(false);

                tv_profile_name.setFocusableInTouchMode(false);
                tv_profile_name.setCursorVisible(false);

                tv_profile_phone.setFocusableInTouchMode(false);
                tv_profile_phone.setCursorVisible(false);


                reference.child("location").setValue(tv_profile_address.getText().toString());
                reference.child("name").setValue(tv_profile_name.getText().toString());
                reference.child("phone").setValue(tv_profile_phone.getText().toString());

            }
        });





        return view;
    }
}