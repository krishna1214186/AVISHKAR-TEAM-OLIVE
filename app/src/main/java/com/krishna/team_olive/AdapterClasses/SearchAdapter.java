package com.krishna.team_olive.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krishna.team_olive.HomePage.ItemDetailActivity;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.R;

import java.util.List;

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

        holder.iv_arrow_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FirebaseDatabase.getInstance().getReference().child("history").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int number=snapshot.child(object_search.getCateogary()).child("number").getValue(Integer.class);

                        number+=1;
                        Toast.makeText(context, ""+number, Toast.LENGTH_SHORT).show();
                        HashMap<String,Object> hm=new HashMap<>();
                        hm.put("number",number);
                        FirebaseDatabase.getInstance().getReference().child("history").child(FirebaseAuth.getInstance().getUid()).child(object_search.getCateogary()).updateChildren(hm);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
                FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int number=snapshot.child(object_search.getCateogary()).getValue(Integer.class);

                        number+=1;
                       FirebaseDatabase.getInstance().getReference().child("histo").child(FirebaseAuth.getInstance().getUid()).child(object_search.getCateogary()).setValue(number);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("model", object_search);
                intent.putExtra("check",1);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_search;
        public TextView tv_item_name_search, tv_category_search, search_ratings;
        ImageView iv_arrow_search;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_search = itemView.findViewById(R.id.search_image);
            tv_category_search = itemView.findViewById(R.id.tv_category_search);
            tv_item_name_search = itemView.findViewById(R.id.tv_item_name_search);
            iv_arrow_search = itemView.findViewById(R.id.iv_arrow_search);
           // search_ratings = itemView.findViewById(R.id.search_rating);
        }
    }
}
