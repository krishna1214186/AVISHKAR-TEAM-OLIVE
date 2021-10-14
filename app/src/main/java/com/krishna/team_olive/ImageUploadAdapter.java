package com.krishna.team_olive;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.VeiwHolder> {

    private Context context;
    private List<Uri> list;

    public ImageUploadAdapter(Context context, List<Uri> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageUploadAdapter.VeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_upload_layout,parent,false);
        return new ImageUploadAdapter.VeiwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageUploadAdapter.VeiwHolder holder, int position) {

        Uri uri = list.get(position);

        holder.iv_upload_image.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VeiwHolder extends RecyclerView.ViewHolder {
        ImageView iv_upload_image;
        public VeiwHolder(@NonNull View itemView) {
            super(itemView);
            iv_upload_image = itemView.findViewById(R.id.iv_upload_img);
        }
    }
}
