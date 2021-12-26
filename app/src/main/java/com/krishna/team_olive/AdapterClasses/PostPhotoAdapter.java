package com.krishna.team_olive.AdapterClasses;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishna.team_olive.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostPhotoAdapter extends RecyclerView.Adapter<PostPhotoAdapter.ViewHolder>
{
    ArrayList<String> list;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_post_photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_post_photo = itemView.findViewById(R.id.iv_post_photo);
        }
    }


    public PostPhotoAdapter(Context context,ArrayList<String> list)
    {
        this.context = context;
        this.list=list;

    }


    @NonNull
    @Override
    public PostPhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_photo_layout,parent, false);
        return new PostPhotoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostPhotoAdapter.ViewHolder holder, int position) {

        String val = list.get(position);
        if(val!=null)
            Picasso.get().load(val).placeholder(R.drawable.ic_others).into(holder.iv_post_photo);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }



}

