package com.krishna.team_olive;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragmentNotification extends Fragment {

    private ImageView iv_arrow_exchange, iv_arrow_fav;
    private RecyclerView recyclerview_notification;
    private NotificationAdapter notificationAdapter;
    private List<NotificationsModel> notificationsModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        iv_arrow_exchange = v.findViewById(R.id.iv_arrow_exchange_req);
        iv_arrow_fav = v.findViewById(R.id.iv_arrow_myfav);

        recyclerview_notification = v.findViewById(R.id.recyclerview_notifs);
        recyclerview_notification.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_notification.setLayoutManager(linearLayoutManager);
        notificationsModelList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationsModelList, getContext());

        iv_arrow_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ExchangeRequest.class);
                startActivity(intent);
            }
        });

        iv_arrow_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyFavorite.class);
                startActivity(intent);
            }
        });



        return v;
    }

    private void  readnotifs(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotificationsModel notificationsModel = snapshot.getValue(NotificationsModel.class);
                    notificationsModelList.add(notificationsModel);
                }
                Collections.reverse(notificationsModelList);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}