package com.krishna.team_olive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private Context context;
    private List<AddedItemDescriptionModel> list;

    FirebaseUser firebaseUser;

    public PostAdapter(Context mcontext, List<AddedItemDescriptionModel> mlist){
        context = mcontext;
        list = mlist;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        
        public ImageView iv_postimage;
        public ImageView iv_like;
        public TextView tv_exchange01, tv_exchange02, tv_itemname, tv_rating, tv_no_of_likes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_postimage = itemView.findViewById(R.id.iv_postimage);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_exchange01 = itemView.findViewById(R.id.tv_exchange01);
            tv_exchange02 = itemView.findViewById(R.id.tv_exchange02);
            tv_itemname = itemView.findViewById(R.id.tv_postitemname);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_no_of_likes = itemView.findViewById(R.id.tv_no_of_likes);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        
        AddedItemDescriptionModel postsData = list.get(position);

       // Picasso.get().load(postsData.getImageurl()).into(holder.iv_postimage);
        if(postsData.getRatings() == ""){
            postsData.setRatings("0");
        }
        holder.tv_rating.setText(postsData.getRatings());
        holder.tv_itemname.setText(postsData.getName());
        holder.tv_exchange02.setText(postsData.getExchangeCateogary());
        holder.tv_exchange01.setText(postsData.getCateogary());

        isliked(postsData.getPostid(),holder.iv_like);
        nooflikes(postsData.getPostid(),holder.tv_no_of_likes);

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.iv_like.getTag().equals("Like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postsData.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postsData.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.tv_itemname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("postid", postsData.getPostid());
                context.startActivity(intent);
            }
        });
            //startforresult.launch(intent);
    }
    private void isliked(String postid, ImageView iv){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    iv.setImageResource(R.drawable.ic_liked);
                    iv.setTag("Liked");
                }else{
                    iv.setImageResource(R.drawable.ic_like);
                    iv.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void nooflikes(String postid, final TextView text){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text.setText(snapshot.getChildrenCount() + " Like");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}