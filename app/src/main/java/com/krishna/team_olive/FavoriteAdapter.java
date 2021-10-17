package com.krishna.team_olive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<AddedItemDescriptionModel> addedItemDescriptionModelList;
    private Context context;

    public FavoriteAdapter(List<AddedItemDescriptionModel> addedItemDescriptionModelList, Context context) {
        this.addedItemDescriptionModelList = addedItemDescriptionModelList;
        this.context = context;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item,parent, false);

        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        AddedItemDescriptionModel obj = addedItemDescriptionModelList.get(position);
        holder.tv_exchange01.setText(obj.getCateogary());
        holder.tv_exchange02.setText(obj.getExchangeCateogary());
        holder.tv_itemname.setText(obj.getName());
        //Picasso.get().load(obj.getImageurl()).into(holder.iv_postimage);
    }

    @Override
    public int getItemCount() {
        return addedItemDescriptionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_postimage;
        public TextView tv_exchange01, tv_exchange02, tv_itemname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_postimage = itemView.findViewById(R.id.iv_postimg_fav);
            tv_exchange01 = itemView.findViewById(R.id.tv_exchange01_fav);
            tv_exchange02 = itemView.findViewById(R.id.tv_exchange02_fav);
            tv_itemname = itemView.findViewById(R.id.tv_postitemname_fav);
        }
    }
}
