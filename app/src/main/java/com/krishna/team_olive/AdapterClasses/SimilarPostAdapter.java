package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.krishna.team_olive.HomePage.ItemDetailActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimilarPostAdapter extends RecyclerView.Adapter<SimilarPostAdapter.ViewHolder>
{
    ArrayList<AddedItemDescriptionModel> list;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_sim_post_img;
        TextView tv_item_title, tv_exchange_cate;
        CardView cv_sim_post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_sim_post_img= itemView.findViewById(R.id.iv_sim_post_img);
            tv_exchange_cate= itemView.findViewById(R.id.tv_exchange_cate);
            tv_item_title = itemView.findViewById(R.id.tv_exchange_cate);
            cv_sim_post = itemView.findViewById(R.id.cv_sim_post);
        }
    }


    public SimilarPostAdapter(Context context,ArrayList<AddedItemDescriptionModel> list)
    {
        this.context = context;
        this.list=list;

    }


    @NonNull
    @Override
    public SimilarPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_posts_layout,parent, false);
        return new SimilarPostAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarPostAdapter.ViewHolder holder, int position) {

        AddedItemDescriptionModel model = list.get(position);
        if(model.getImageurl()!=null && !(model.getImageurl().equals("")) )
            Picasso.get().load(model.getImageurl()).placeholder(R.drawable.ic_ads).into(holder.iv_sim_post_img);
        else holder.iv_sim_post_img.setImageResource(R.drawable.ic_ads);

            holder.tv_item_title.setText(model.getName());
        holder.tv_exchange_cate.setText(model.getExchangeCateogary());

        holder.cv_sim_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("postid", model.getPostid());
                intent.putExtra("model", model);
                context.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return list.size();
    }



}