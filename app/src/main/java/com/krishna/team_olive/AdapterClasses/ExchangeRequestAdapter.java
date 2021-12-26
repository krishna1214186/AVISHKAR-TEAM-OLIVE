package com.krishna.team_olive.AdapterClasses;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.ModelClasses.ExchangeModel;
import com.krishna.team_olive.ModelClasses.NotificationsModel;
import com.krishna.team_olive.R;
import com.krishna.team_olive.SendNotificationPack.APIService;
import com.krishna.team_olive.SendNotificationPack.Client;
import com.krishna.team_olive.SendNotificationPack.Data;
import com.krishna.team_olive.SendNotificationPack.MyResponse;
import com.krishna.team_olive.SendNotificationPack.NotificationSender;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Adapter for the list that appears in exchange request activity of the user.

public class ExchangeRequestAdapter extends RecyclerView.Adapter<ExchangeRequestAdapter.ViewHolder>{

    private Context context;
    private List<ExchangeModel> exchangeModelList;
    private APIService apiService;
    private Dialog dialog;

    public ExchangeRequestAdapter(Context context, List<ExchangeModel> exchangeModelList) {
        this.context = context;
        this.exchangeModelList = exchangeModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_customer_name, tv_item_name;
        private Button btn_accept_req, btn_decline_req;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_customer_name = itemView.findViewById(R.id.tv_custumer_name);
            tv_item_name = itemView.findViewById(R.id.tv_item_name_exchange_req);
            btn_accept_req = itemView.findViewById(R.id.btn_accept_exchange_req);
            btn_decline_req = itemView.findViewById(R.id.btn_decline_exchange_req);
        }
    }

    @NonNull
    @Override
    public ExchangeRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.exchange_req_item,parent,false);
        return new ExchangeRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeRequestAdapter.ViewHolder holder, int position) {
        ExchangeModel object_exc_req = exchangeModelList.get(position);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        holder.tv_item_name.setText(object_exc_req.getItem_name());
        holder.tv_customer_name.setText(object_exc_req.getClient_name());


        dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirm_exchange_alert_dialouge);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_no = dialog.findViewById(R.id.et_no_exchange_req);
        TextView tv_yes = dialog.findViewById(R.id.et_yes_exchange_req);

        holder.btn_accept_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();

                        DatabaseReference firebaseDatabase2 = FirebaseDatabase.getInstance().getReference().child("Notifications").child(object_exc_req.getClient_uid());
                        NotificationsModel notificationsModel = new NotificationsModel(object_exc_req.getUser_uid(), "accepted your request", object_exc_req.getPostid(), 2);
                        firebaseDatabase2.push().setValue(notificationsModel);

                        FirebaseDatabase.getInstance().getReference().child("Exchange Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(object_exc_req.getRequest_uid()).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("allpostswithout").child(object_exc_req.getPostid()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("nonNGOposts").child(object_exc_req.getPostid()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("NGOposts").child(object_exc_req.getPostid()).removeValue();

                        String client_uid = object_exc_req.getClient_uid();

                        DatabaseReference firebaseDatabase1 = FirebaseDatabase.getInstance().getReference().child("Myexchanges").child(object_exc_req.getClient_uid()).push();
                        firebaseDatabase1.setValue(object_exc_req);
                        DatabaseReference firebaseDatabase3 = FirebaseDatabase.getInstance().getReference().child("Myexchanges").child(object_exc_req.getUser_uid()).push();
                        firebaseDatabase3.setValue(object_exc_req);

                        FirebaseDatabase.getInstance().getReference().child("MyLikes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    if (snapshot1.getValue(String.class).equals(object_exc_req.getPostid())) {
                                        FirebaseDatabase.getInstance().getReference().child("MyLikes").child(snapshot.toString())
                                                .child(object_exc_req.getPostid()).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(client_uid).child("token").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String token = snapshot.getValue(String.class);
                                sendNotifications(token, "Exchange request accepted", "Your exchange request for "+object_exc_req.getItem_name()+" has been accepted");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                });
            }
        });


        holder.btn_decline_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("Exchange Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(object_exc_req.getRequest_uid()).removeValue();
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return exchangeModelList.size();
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
