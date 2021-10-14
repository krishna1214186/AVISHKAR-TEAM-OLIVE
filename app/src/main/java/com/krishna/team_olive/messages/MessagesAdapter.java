package com.krishna.team_olive.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.krishna.team_olive.FragmentChat;
import com.krishna.team_olive.R;
import com.krishna.team_olive.chat.Chat;
import com.krishna.team_olive.chat.ChatList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

     ArrayList<MessagesList> messagesLists;
     Context context;

    Long timestp;


    public MessagesAdapter(ArrayList<MessagesList> messagesLists,Context context) {
        this.messagesLists = messagesLists;
        this.context=context;

    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {

        MessagesList list2 = messagesLists.get(position);



        holder.name.setText(list2.getName());


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timestp=   snapshot.child("lastconnected").child("timestamp").getValue(Long.class);
               // Toast.makeText(context, timestp+"", Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+messagesLists.get(position).getUid())
                .orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int unseenmessage=0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    ChatList cl=dataSnapshot.getValue(ChatList.class);
                   // Toast.makeText(context, ""+cl.getTimeStamp(), Toast.LENGTH_SHORT).show();
                    if(cl.getTimeStamp()>timestp)
                    {
                        unseenmessage++;

                    }
                }
                if(unseenmessage == 0){
                    holder.unseenMessages.setVisibility(View.GONE);
                    holder.lastMessage.setTextColor(Color.parseColor("#959595"));

                }
                else{
                    holder.unseenMessages.setVisibility(View.VISIBLE);
                    holder.unseenMessages.setText(unseenmessage+"");
                    holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //holder.lastMessage.setText(list2.getLastMessage());

        ;

        holder.lastMessage.setText(list2.getLastMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(context, Chat.class);
                intent.putExtra("name", list2.getName());
                intent.putExtra("uid",list2.getUid());


                context.startActivity(intent);

            }
        });
       FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+
                messagesLists.get(position).getUid()).orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren())
                {
                    for(DataSnapshot datasnapshot: snapshot.getChildren())
                    {

                        holder.lastMessage.setText(datasnapshot.child("message").getValue(String.class));




                    }


                }

            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       /* FirebaseDatabase.getInstance().getReference().child("lastMessage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               holder.lastMessage.setText( snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+list2.getUid()).child("lastmessage").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }



    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessages;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage  = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);

        }
    }
}




