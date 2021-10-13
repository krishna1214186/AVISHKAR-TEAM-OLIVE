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
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<AddedItemDescriptionModel> list;
    private boolean isFragment;
    private FirebaseUser firebaseUser;

    public SearchAdapter(Context context, List<AddedItemDescriptionModel> list, boolean isFragment) {
        this.context = context;
        this.list = list;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_item,parent,false);
        return new SearchAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        AddedItemDescriptionModel object_search = list.get(position);
        holder.tv_item_name_search.setText(object_search.getName());
        holder.tv_category_search.setText(object_search.getCateogary());
        holder.search_ratings.setText(object_search.getRatings());
        Picasso.get().load(object_search.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.iv_search);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView iv_search;
        public TextView tv_item_name_search, tv_category_search, search_ratings;
        ImageView iv_arrow_search;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_search = itemView.findViewById(R.id.search_image);
            tv_category_search = itemView.findViewById(R.id.tv_category_search);
            tv_item_name_search = itemView.findViewById(R.id.tv_item_name_search);
            iv_arrow_search = itemView.findViewById(R.id.iv_arrow_search);
            search_ratings = itemView.findViewById(R.id.search_rating);
        }
    }


}
