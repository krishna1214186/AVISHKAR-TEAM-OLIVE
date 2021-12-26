package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.krishna.team_olive.R;
import com.krishna.team_olive.chat.ChatList;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<ChatList> list;
    Context context;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<ChatList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE)
        {
            View v=LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return  new SenderViewholder(v);
        }
        else
        {
            View v=LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return  new ReceiverViewholder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(list.get(position).getId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
            return RECEIVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatList cl=list.get(position);
        if(holder.getClass()==SenderViewholder.class)
        {
            ((SenderViewholder)holder).SenderMsg.setText(list.get(position).getMessage());
        }
        else
        {
            ((ReceiverViewholder)holder).ReceiverMsg.setText(list.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReceiverViewholder extends RecyclerView.ViewHolder {
        TextView ReceiverMsg,ReceiverTime;
        public ReceiverViewholder(@NonNull View itemView) {
            super(itemView);
            ReceiverMsg=itemView.findViewById(R.id.oppoMessage);
            ReceiverTime=itemView.findViewById(R.id.oppoMsgTime);

        }
    }

    public class SenderViewholder extends RecyclerView.ViewHolder {
        TextView SenderMsg,SenderTime;
        public SenderViewholder(@NonNull View itemView) {
            super(itemView);
            SenderMsg=itemView.findViewById(R.id.myMessage);
            SenderTime=itemView.findViewById(R.id.myMsgTime);

        }
    }
}