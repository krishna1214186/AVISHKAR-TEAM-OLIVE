package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.krishna.team_olive.SendNotificationPack.APIService;
import com.krishna.team_olive.SendNotificationPack.Client;
import com.krishna.team_olive.SendNotificationPack.Data;
import com.krishna.team_olive.SendNotificationPack.MyResponse;
import com.krishna.team_olive.SendNotificationPack.NotificationSender;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddedItemDetailFilling_3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btn_yes, btn_no, btn_ok;
    TextView tv;
    String text = "";
    FirebaseDatabase database;
    private DatabaseReference dataRefrence;
    private StorageTask st;
    String postid2;
    FirebaseAuth auth;
    UriContainer uri;
    int taskfinished = 1;
    AddedItemDescriptionModel model;
    List<NotifExchangeModel> notifExchangeModelList_1, notifExchangeModelList_2;
    private APIService apiService;
    private Dialog dialog;
    private Button btn_done;

    LinearLayout ll_complete, ll_btnDone;

    private SpinnerItemAdapter spinnerItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_item_detail_filling3);

        Spinner mySpinner = findViewById(R.id.spinner_1);

        btn_ok = findViewById(R.id.btn_ok);
        tv = findViewById(R.id.tv_cateogary);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.congrats_donate);

        ll_complete = findViewById(R.id.ll_complete);

        btn_done = dialog.findViewById(R.id.btn_done);
        ll_btnDone = dialog.findViewById(R.id.ll_btnDone);

        postid2 = getIntent().getStringExtra("postid");
        model = (AddedItemDescriptionModel) getIntent().getSerializableExtra("model");


        database = FirebaseDatabase.getInstance();
        dataRefrence = database.getReference();
        auth=FirebaseAuth.getInstance();

        notifExchangeModelList_1 = new ArrayList<>();
        notifExchangeModelList_2 = new ArrayList<>();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        if(model.getTypeOfExchange().equals("Y")){
            uploadData(model);
            ll_complete.setVisibility(View.GONE);
            alertMessage();
        }

        mySpinner.setOnItemSelectedListener(AddedItemDetailFilling_3.this);
        spinnerItemAdapter = new SpinnerItemAdapter(AddedItemDetailFilling_3.this, com.krishna.team_olive.Data.getFruitList());
        mySpinner.setAdapter(spinnerItemAdapter);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddedItemDetailFilling_3.this,MainActivity.class);
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
        model.setExchangeCateogary(text);
        model.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        uploadData(model);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ExchangeNotif").child(model.getCateogary()+model.getExchangeCateogary()).push();
        NotifExchangeModel notifExchangeModel = new NotifExchangeModel(postid2,FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.setValue(notifExchangeModel);

        FirebaseDatabase.getInstance().getReference().child("ExchangeNotif").child(model.getExchangeCateogary()+model.getCateogary()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotifExchangeModel notifExchangeModel1 = dataSnapshot.getValue(NotifExchangeModel.class);

                    NotificationsModel notificationsModel = new NotificationsModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),"Has same item",postid2,3);
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
        /*
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("NeedInExchange").child(model.getExchangeCateogary());
        NotifExchangeModel notifExchangeModel = new NotifExchangeModel(postid2,FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.push().setValue(notifExchangeModel);

        FirebaseDatabase.getInstance().getReference().child("ExchangeCategory").child(model.getExchangeCateogary()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifExchangeModelList_1.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotifExchangeModel notifExchangeModel1 = dataSnapshot.getValue(NotifExchangeModel.class);
                    notifExchangeModelList_1.add(notifExchangeModel1);
                }
                Toast.makeText(AddedItemDetailFilling_3.this, Integer.toString(notifExchangeModelList_1.size()), Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("NeedInExchange").child(model.getCateogary()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                             NotifExchangeModel notifExchangeModel1 = dataSnapshot.getValue(NotifExchangeModel.class);
                             notifExchangeModelList_2.add(notifExchangeModel1);
                         }
                        Toast.makeText(AddedItemDetailFilling_3.this, Integer.toString(notifExchangeModelList_2.size()), Toast.LENGTH_SHORT).show();
                         for(int i=0;i<notifExchangeModelList_1.size(); i++){
                             for(int j=0;j<notifExchangeModelList_2.size();j++){
                                 if(notifExchangeModelList_1.get(i).getPost_id().equals(notifExchangeModelList_2.get(j).getPost_id())){
                                     NotificationsModel notificationsModel = new NotificationsModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),"has same exchange",postid2,3);
                                     DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Notifications").child(notifExchangeModelList_1.get(i).getUser_id()).push();
                                     databaseReference1.setValue(notificationsModel);
                                 }
                             }
                         }
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
        */


        dataRefrence.child("posts with uid").child(auth.getCurrentUser().getUid()).child(postid2).setValue(model);

        if(model.getTypeOfExchange().equals("Y"))
            dataRefrence.child("Current non NGO posts").child(postid2).setValue(model);
        else
            dataRefrence.child("Current NGO posts").child(postid2).setValue(model);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void uploadData(AddedItemDescriptionModel m) {
//        dataRefrence.child("Current NGO posts").child(postid2).setValue(m);
        dataRefrence.child("posts with uid").child(auth.getCurrentUser().getUid()).child(postid2).setValue(m);
        dataRefrence.child("allpostswithoutuser").child(postid2).setValue(m);


        if(model.getTypeOfExchange().equals("Y"))
            dataRefrence.child("Current non NGO posts").child(postid2).setValue(model);
        else
            dataRefrence.child("Current NGO posts").child(postid2).setValue(model);
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
/*
 DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ExchangeCategory").child(model.getExchangeCateogary());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String user_id = dataSnapshot.toString();
                            //NotificationsModel notificationsModel = new NotificationsModel(user_id,"has same item as yours",);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
 */