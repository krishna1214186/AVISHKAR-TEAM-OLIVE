package com.krishna.team_olive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_postimage;
        public TextView  tv_exchange02, tv_itemname,tv_username;
        public Button btn_open;
        LinearLayout ll_postClickFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_postimage = itemView.findViewById(R.id.iv_postimg);
            tv_exchange02 = itemView.findViewById(R.id.tv_exchange02);
            tv_itemname = itemView.findViewById(R.id.tv_postitemname);
            ll_postClickFav = itemView.findViewById(R.id.post_click);
            tv_username=itemView.findViewById(R.id.tv_username);
            btn_open=itemView.findViewById(R.id.btn_postOpen);
        }
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item,parent, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        AddedItemDescriptionModel obj = addedItemDescriptionModelList.get(position);
        holder.tv_exchange02.setText(obj.getExchangeCateogary());
        holder.tv_itemname.setText(obj.getName());
        //Picasso.get().load(obj.getImageurl()).into(holder.iv_postimage);

FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.hasChild(obj.getUid())) {
            Users user = snapshot.child(obj.getUid()).getValue(Users.class);
            holder.tv_username.setText(user.getName());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
        holder.btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("postid", obj.getPostid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addedItemDescriptionModelList.size();
    }


}
