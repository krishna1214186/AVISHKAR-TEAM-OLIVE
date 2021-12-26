package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishna.team_olive.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder > {

    ArrayList<String> list;
    Context context;

    public SliderAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String detail = list.get(position);


//        Picasso.get().load(detail).into(holder.iv_postimg);
//
//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.listener(new Picasso.Listener() {
//
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                Toast.makeText(context, "video", Toast.LENGTH_SHORT).show();
//                holder.iv_postimg.setVisibility(View.GONE);
//                holder.vv_postvdo.setVisibility(View.VISIBLE);
//                holder.vv_postvdo.setVideoURI(Uri.parse(detail));
//                holder.vv_postvdo.seekTo(1);
//                holder.vv_postvdo.start();
//            }
//        });
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.indicatorsEnabled(true);
        builder.loggingEnabled(true);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                holder.iv_postimg.setVisibility(View.GONE);
                holder.vv_postvdo.setVisibility(View.VISIBLE);
                holder.vv_postvdo.setVideoURI(Uri.parse(detail));
                holder.vv_postvdo.seekTo(1);
                holder.vv_postvdo.start();

            }
        });

        holder.vv_postvdo.setVisibility(View.GONE);

        builder.build().load(detail)
                .fit()
                .centerCrop()
                .into(holder.iv_postimg /*, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
//                        holder.iv_postimg.setVisibility(View.GONE);
//                        holder.vv_postvdo.setVisibility(View.VISIBLE);
//                        holder.vv_postvdo.setVideoURI(Uri.parse(detail));
//                        holder.vv_postvdo.seekTo(1);
//                        holder.vv_postvdo.start();
                    }
                }*/);




//        if(holder.iv_postimg.getDrawable()==null){
//            Toast.makeText(context, "video", Toast.LENGTH_SHORT).show();
//            holder.iv_postimg.setVisibility(View.GONE);
//            holder.vv_postvdo.setVisibility(View.VISIBLE);
//            holder.vv_postvdo.setVideoURI(Uri.parse(detail));
//            holder.vv_postvdo.seekTo(1);
//            holder.vv_postvdo.start();
//        }


//        URI uri = null;
//        try {
//            uri = new URI(detail);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_postimg;
        VideoView vv_postvdo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_postimg = itemView.findViewById(R.id.iv_postimg);
            vv_postvdo = itemView.findViewById(R.id.vv_postvdo);
        }
    }

}