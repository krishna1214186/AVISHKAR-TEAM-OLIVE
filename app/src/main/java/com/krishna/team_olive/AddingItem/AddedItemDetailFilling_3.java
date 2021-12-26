
package com.krishna.team_olive.AddingItem;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.MainActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.ModelClasses.NotifExchangeModel;
import com.krishna.team_olive.ModelClasses.NotificationsModel;
import com.krishna.team_olive.R;
import com.krishna.team_olive.SendNotificationPack.APIService;
import com.krishna.team_olive.SendNotificationPack.Client;
import com.krishna.team_olive.SendNotificationPack.Data;
import com.krishna.team_olive.SendNotificationPack.MyResponse;
import com.krishna.team_olive.SendNotificationPack.NotificationSender;
import com.krishna.team_olive.AdapterClasses.SpinnerItemAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Third activity for addition of item/post in which we select the category of product that we want in return;

public class AddedItemDetailFilling_3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_ok;
    private String text = "";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dataRefrence;
    private String postid;
    private FirebaseAuth firebaseAuth;
    private AddedItemDescriptionModel addedItemDescriptionModel;
    private APIService apiService;
    private Dialog dialog;
    private Button btn_done;
    private SpinnerItemAdapter spinnerItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_item_detail_filling3);

        Spinner mySpinner = findViewById(R.id.spinner_1);

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setVisibility(View.GONE);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.congrats_donate);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        btn_done = dialog.findViewById(R.id.btn_done);

        postid = getIntent().getStringExtra("postid");
        addedItemDescriptionModel = (AddedItemDescriptionModel) getIntent().getSerializableExtra("model");


        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRefrence = firebaseDatabase.getReference();
        firebaseAuth =FirebaseAuth.getInstance();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        if(addedItemDescriptionModel.getTypeOfExchange().equals("Y")){
            uploadData(addedItemDescriptionModel);
            alertMessage();
        }

        mySpinner.setOnItemSelectedListener(AddedItemDetailFilling_3.this);
        spinnerItemAdapter = new SpinnerItemAdapter(AddedItemDetailFilling_3.this, com.krishna.team_olive.AddingItem.Spinner.Data.getItemList());
        mySpinner.setAdapter(spinnerItemAdapter);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddedItemDetailFilling_3.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void alertMessage() {
        dialog.show();
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddedItemDetailFilling_3.this,MainActivity.class));
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text=parent.getItemAtPosition(position).toString();
        addedItemDescriptionModel.setExchangeCateogary(text);
        uploadData(addedItemDescriptionModel);
        btn_ok.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ExchangeNotif").child(addedItemDescriptionModel.getCateogary()+ addedItemDescriptionModel.getExchangeCateogary()).push();
        NotifExchangeModel notifExchangeModel = new NotifExchangeModel(postid,FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.setValue(notifExchangeModel);

        FirebaseDatabase.getInstance().getReference().child("ExchangeNotif").child(addedItemDescriptionModel.getExchangeCateogary()+ addedItemDescriptionModel.getCateogary()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotifExchangeModel notifExchangeModel1 = dataSnapshot.getValue(NotifExchangeModel.class);

                    NotificationsModel notificationsModel = new NotificationsModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),"Has same item", postid,3);
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Notifications").child(notifExchangeModel1.getUser_id()).push();
                    databaseReference1.setValue(notificationsModel);

                    NotificationsModel notificationsModel1 = new NotificationsModel(notifExchangeModel1.getUser_id(),"Has same item",notifExchangeModel1.getPost_id(),3);
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
                    databaseReference2.setValue(notificationsModel1);

                    String user_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String client_uid = notifExchangeModel1.getUser_id();

                    forNotification(user_uid, client_uid);
                    Toast.makeText(AddedItemDetailFilling_3.this, notifExchangeModel1.getUser_id(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dataRefrence.child("allpostswithoutuser").child(postid).setValue(addedItemDescriptionModel);
        dataRefrence.child("mypostswithuser").child(firebaseAuth.getCurrentUser().getUid()).child(postid).setValue(addedItemDescriptionModel);

        if(addedItemDescriptionModel.getTypeOfExchange().equals("Y"))
            dataRefrence.child("NGOposts").child(postid).setValue(addedItemDescriptionModel);
        else
            dataRefrence.child("nonNGOposts").child(postid).setValue(addedItemDescriptionModel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void uploadData(AddedItemDescriptionModel m) {
        m.setExtension(addedItemDescriptionModel.getName().toLowerCase());
        addedItemDescriptionModel.setExtension(addedItemDescriptionModel.getName().toLowerCase());
        dataRefrence.child("allpostswithoutuser").child(postid).setValue(m);
        dataRefrence.child("mypostswithuser").child(firebaseAuth.getCurrentUser().getUid()).child(postid).setValue(m);

        if(addedItemDescriptionModel.getTypeOfExchange().equals("Y"))
            dataRefrence.child("NGOposts").child(postid).setValue(addedItemDescriptionModel);
        else
            dataRefrence.child("nonNGOposts").child(postid).setValue(addedItemDescriptionModel);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(AddedItemDetailFilling_3.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    public void forNotification(String client_uid, String user_uid){
        FirebaseDatabase.getInstance().getReference().child("users").child(user_uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);

                FirebaseDatabase.getInstance().getReference().child("Tokens").child(client_uid).child("token").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String token = snapshot.getValue(String.class);
                        sendNotifications(token, "Exchange item matched", name +" has same item for exchange");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}