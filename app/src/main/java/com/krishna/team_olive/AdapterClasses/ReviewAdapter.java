package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishna.team_olive.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>
{
    ArrayList<String> list;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_review = itemView.findViewById(R.id.tv_review_prof);
        }
    }


    public ReviewAdapter(Context context,ArrayList<String> list)
    {
        this.context = context;
        this.list=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String val = list.get(position);
        if(!val.isEmpty())
            holder.tv_review.setText(val);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}