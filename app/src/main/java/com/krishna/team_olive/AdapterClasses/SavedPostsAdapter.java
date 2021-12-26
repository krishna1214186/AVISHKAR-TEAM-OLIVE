package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishna.team_olive.HomePage.ItemDetailActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.R;
import com.squareup.picasso.Picasso;

import java.util.List;

//Adapter for saved posts that a user can save for future use.

public class SavedPostsAdapter extends RecyclerView.Adapter<SavedPostsAdapter.ViewHolder> {

    private List<AddedItemDescriptionModel> addedItemDescriptionModelList;
    private Context context;

    public SavedPostsAdapter(List<AddedItemDescriptionModel> addedItemDescriptionModelList, Context context) {
        this.addedItemDescriptionModelList = addedItemDescriptionModelList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_postimage;
        public TextView  tv_exchange02, tv_itemname;
        public Button btn_open;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_postimage = itemView.findViewById(R.id.iv_postimg);
            tv_exchange02 = itemView.findViewById(R.id.tv_exchange02);
            tv_itemname = itemView.findViewById(R.id.tv_postitemname);
            btn_open=itemView.findViewById(R.id.btn_postOpen);
        }
    }

    @Override
    public SavedPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item,parent, false);
        return new SavedPostsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPostsAdapter.ViewHolder holder, int position) {
        AddedItemDescriptionModel obj = addedItemDescriptionModelList.get(position);
        holder.tv_exchange02.setText(obj.getExchangeCateogary());
        holder.tv_itemname.setText(obj.getName());

        holder.iv_postimage.setImageResource(R.drawable.ic_ads);
        if(obj.getImageurl()!=null  && !(obj.getImageurl().equals("")))
            Picasso.get().load(obj.getImageurl()).placeholder(R.drawable.ic_ads).into(holder.iv_postimage);

        holder.btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("postid", obj.getPostid());
                    intent.putExtra("model", obj);
                    intent.putExtra("check",1);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addedItemDescriptionModelList.size();
    }
}
