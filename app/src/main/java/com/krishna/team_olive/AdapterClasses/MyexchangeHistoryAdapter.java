package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishna.team_olive.ModelClasses.MyexchangeHistoryModel;
import com.krishna.team_olive.R;

import java.util.ArrayList;

public class MyexchangeHistoryAdapter extends RecyclerView.Adapter<MyexchangeHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<MyexchangeHistoryModel> list;

    public MyexchangeHistoryAdapter(Context context, ArrayList<MyexchangeHistoryModel> list) {
        this.context = context;
        this.list = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_product_photo;
        TextView tv_product_name;
        TextView tv_product_cateogary;
        TextView tv_exchanged_cateogary;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_product_name=itemView.findViewById(R.id.tv_product_name);
            tv_product_cateogary=itemView.findViewById(R.id.tv_cateogary_of_product);
            tv_exchanged_cateogary=itemView.findViewById(R.id.tv_cateogary_exchanged);

        }
    }


    @NonNull
    @Override
    public MyexchangeHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_exchange_history_item,parent,false);
        return new MyexchangeHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyexchangeHistoryAdapter.ViewHolder holder, int position) {
holder.tv_product_name.setText(list.get(position).getName());
holder.tv_product_cateogary.setText(list.get(position).getCateogaryOfProduct());
holder.tv_exchanged_cateogary.setText(list.get(position).getCayteogaryExchanged());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
