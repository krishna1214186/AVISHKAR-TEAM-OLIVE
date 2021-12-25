package com.krishna.team_olive;

import android.content.Context;
import android.content.Intent;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.SendNotificationPack.APIService;
import com.krishna.team_olive.SendNotificationPack.Client;
import com.krishna.team_olive.SendNotificationPack.Data;
import com.krishna.team_olive.SendNotificationPack.MyResponse;
import com.krishna.team_olive.SendNotificationPack.NotificationSender;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoadingViewHolder extends RecyclerView.ViewHolder{

    ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);

        progressBar = itemView.findViewById(R.id.progressBar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{

    public ImageView iv_postimage;
    public ImageView iv_like;
    public TextView tv_exchange02, tv_itemname, tv_rating, tv_reviews, tv_location;
    public Button btn_post;


    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        iv_postimage = itemView.findViewById(R.id.iv_postimg);
        iv_like = itemView.findViewById(R.id.iv_like);
        tv_exchange02 = itemView.findViewById(R.id.tv_exchange02);
        tv_itemname = itemView.findViewById(R.id.tv_postitemname);
        tv_rating = itemView.findViewById(R.id.tv_rating);
        tv_reviews = itemView.findViewById(R.id.tv_reviews);
        btn_post = itemView.findViewById(R.id.btn_postOpen);
        tv_location = itemView.findViewById(R.id.tv_location);
    }
}


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<AddedItemDescriptionModel> list;

    private final int VIEW_TYPE_LOADING =1, VIEW_TYPE_ITEM =0;

    ILoadMore iLoadMore;
    Boolean isLoading = false;

    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    FirebaseUser firebaseUser;
    private APIService apiService;

    public PostAdapter(RecyclerView recyclerView,Context mcontext, List<AddedItemDescriptionModel> mlist){
        context = mcontext;
        list = mlist;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                    if(iLoadMore != null){
                        iLoadMore.LoadMore();
                    }
                    isLoading = true;
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setiLoadMore(ILoadMore iLoadMore) {
        this.iLoadMore = iLoadMore;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
            return new ItemViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(context).inflate(R.layout.post_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            AddedItemDescriptionModel postsData = list.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            if(postsData.getRatings() == ""){
                postsData.setRatings("2");
            }
            itemViewHolder.tv_itemname.setText(postsData.getName());
            itemViewHolder.tv_exchange02.setText(postsData.getExchangeCateogary());

            itemViewHolder.tv_location.setText(postsData.getAdress2());

            apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

            itemViewHolder.btn_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra("postid", postsData.getPostid());
                    intent.putExtra("model", postsData);
                    intent.putExtra("check",1);
                    context.startActivity(intent);
                }
            });

            isliked(postsData.getPostid(),itemViewHolder.iv_like);

            FirebaseDatabase.getInstance().getReference().child("Ratings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemViewHolder.tv_reviews.setText(snapshot.getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("Ratings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rating").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemViewHolder.tv_rating.setText(snapshot.getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            itemViewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemViewHolder.iv_like.getTag().equals("Like")){
                        FirebaseDatabase.getInstance().getReference().child("Likes").child(postsData.getPostid()).child(firebaseUser.getUid()).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("MyLikes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postsData.getPostid()).setValue(true);
                    /*    FirebaseDatabase.getInstance().getReference().child("postidwirhuserid").child(postsData.getPostid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                                    FirebaseDatabase.getInstance().getReference().child("Tokens").
                                            child(postsData.getUid()).child("token").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String token = snapshot.getValue(String.class);
                                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child("name").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String name = snapshot.getValue(String.class);
                                                    sendNotifications(token, "Your post is getting liked", name + " has liked your post" );
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    */
                    }else{
                        FirebaseDatabase.getInstance().getReference().child("Likes").child(postsData.getPostid()).child(firebaseUser.getUid()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("MyLikes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postsData.getPostid()).removeValue();
                    }
                }
            });

            itemViewHolder.tv_itemname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra("postid", postsData.getPostid());
                    intent.putExtra("model", postsData);
                    intent.putExtra("check",1);
                    context.startActivity(intent);
                }
            });

        }else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    private void isliked(String postid, ImageView iv){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    iv.setImageResource(R.drawable.bookmark1);
                    iv.setTag("Liked");
                }else{
                    iv.setImageResource(R.drawable.bookmark);
                    iv.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendNotifications(String usertoken, String title, String message) {

        Data data = new Data(title,message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}