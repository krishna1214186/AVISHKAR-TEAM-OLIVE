package com.krishna.team_olive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Exchange_history_Adapter  extends RecyclerView.Adapter<Exchange_history_Adapter.ViewHolder>{
    Context context;
    List<AddedItemDescriptionModel> list;

    public Exchange_history_Adapter(Context context,List<AddedItemDescriptionModel> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_element,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_productName.setText(list.get(position).getName());
        holder.tv_thisUser.setText(list.get(position).getExchangeCateogary());
        holder.tv_oppUser.setText(list.get(position).getCateogary());
        String uid=list.get(position).getUid();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,Ratings.class);
                intent.putExtra("ud",uid);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_image;
        TextView tv_productName;
        TextView tv_thisUser;
        TextView tv_oppUser;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image=itemView.findViewById(R.id.iv_image);
            tv_productName=itemView.findViewById(R.id.name);
            tv_thisUser=itemView.findViewById(R.id.this_user);
            tv_oppUser=itemView.findViewById(R.id.opp_user);

        }
    }
}