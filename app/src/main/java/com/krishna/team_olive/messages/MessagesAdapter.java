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
    int unseen;
    Long timestp;


    public MessagesAdapter(ArrayList<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;

    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.message_adapter_layout, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MessagesList list2 = messagesLists.get(position);


        holder.itemView.setTag(list2);
        holder.name.setText(list2.getName());
        //     Toast.makeText(context, list2.getName(), Toast.LENGTH_SHORT).show();

/*FirebaseDatabase.getInstance().getReference().child("lastconnected").addValueEventListener(new ValueEventListener() {
                                                                                                       @Override
                                                                                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                           if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid())) {
                                                                                                               timestp = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid()).getValue(Long.class);
                                                                                                              // Toast.makeText(context, timestp+"", Toast.LENGTH_SHORT).show();

                                                                                                           }
                                                                                                          FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid())
                                                                                                                   .orderByChild("timeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                               @Override
                                                                                                               public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                                                                                   int unseenmessage = 0;
                                                                                                                  // Toast.makeText(context, "came in this function", Toast.LENGTH_SHORT).show();
                                                                                                                   if (snapshot2.hasChildren()) {
                                                                                                                       for (DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {

                                                                                                                           ChatList cl = dataSnapshot2.getValue(ChatList.class);
                                                                                                                         //  Toast.makeText(context, ""+timestp, Toast.LENGTH_SHORT).show();
                                                                                                                           if (cl.getTimeStamp() > timestp) {

                                                                                                                               unseenmessage++;


                                                                                                                           }
                                                                                                                       }
                                                                                                                   }
                                                                                                                   if (unseenmessage == 0) {
                                                                                                                       holder.unseenMessages.setVisibility(View.GONE);
                                                                                                                       //holder.lastMessage.setTextColor(Color.parseColor("#959595"));


                                                                                                                   } else {
                                                                                                                       holder.unseenMessages.setVisibility(View.VISIBLE);
                                                                                                                       holder.unseenMessages.setText(unseenmessage + "");

                                                                                                                      // holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));

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
                                                                                                   });*/

       /*FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timestp = snapshot.child("lastconnected").child("timestamp").getValue(Long.class);
                Toast.makeText(context, "" + timestp, Toast.LENGTH_SHORT).show();
                // Toast.makeText(context, timestp+"", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

       /* FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ChatList chat=dataSnapshot.getValue(ChatList.class);
                    if(chat.getIsSeen().equals("0"))
                    {
                        unseen++;
                    }

                }
                if(unseen == 0){
                    holder.unseenMessages.setVisibility(View.GONE);
                    holder.lastMessage.setTextColor(Color.parseColor("#959595"));

                }
                else{
                    holder.unseenMessages.setVisibility(View.VISIBLE);
                    holder.unseenMessages.setText(unseen+"");
                    holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })*/
        unseen = 0;
        FirebaseDatabase.getInstance().getReference().child("unseen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid()).hasChild("count")) {
                    unseen = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid()).child("count").getValue(Integer.class);
                    if (unseen == 0) {
                        holder.unseenMessages.setVisibility(View.GONE);
                        // holder.lastMessage.setTextColor(Color.parseColor("#959595"));

                    } else {
                        holder.unseenMessages.setVisibility(View.VISIBLE);
                        holder.unseenMessages.setText(unseen + "");
                        // holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));

                    }
                } else {
                    holder.unseenMessages.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      /* FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+messagesLists.get(position).getUid())
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        //holder.lastMessage.setText(list2.getLastMessage());

        ;


        FirebaseDatabase.getInstance().getReference().child("lastMessage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid())) {
                    holder.lastMessage.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + list2.getUid()).child("lastmessage").getValue(String.class));

                }
                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = messagesLists.indexOf(itemView.getTag());
                    Intent intent = new Intent(context, Chat.class);
                    intent.putExtra("name", messagesLists.get(p).getName());
                    intent.putExtra("uid", messagesLists.get(p).getUid());

                    FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + messagesLists.get(p).getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                // ChatList temp=dataSnapshot.getValue(ChatList.class);

                                FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + messagesLists.get(p).getUid()).child(dataSnapshot.getKey()).child("isSeen").setValue("1");
                                //Toast.makeText(, "updated", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    context.startActivity(intent);

                }
            });

        }
    }
}