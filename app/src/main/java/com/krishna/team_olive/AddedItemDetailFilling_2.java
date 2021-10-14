package com.krishna.team_olive;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddedItemDetailFilling_2 extends AppCompatActivity {

    VideoView vv_upload;
    RecyclerView rv_img;
    FloatingActionButton fac_img;
    AddedItemDescriptionModel model;
    Button btn_next;

    Uri vdo_uri;
    String postid;

    private List<Uri> list;

    FirebaseDatabase database;
    FirebaseStorage storage;
    private ImageUploadAdapter imageUploadAdapter;
    Uri imguri1;
    int i = 0,j=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_item_detail_filling2);

        model = (AddedItemDescriptionModel) getIntent().getSerializableExtra("model");
        postid = model.getPostid();

        fac_img = findViewById(R.id.fac_add);
        rv_img = findViewById(R.id.rv_img);
        vv_upload = findViewById(R.id.vv_upload);
        btn_next = findViewById(R.id.btn_next);


        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();





        rv_img = findViewById(R.id.rv_img);
        rv_img.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(AddedItemDetailFilling_2.this, 2);
        rv_img.setLayoutManager(layoutManager);

//            vv_upload.setVideoURI(video_list.get(0));
//            vv_upload.seekTo(1);
//

        list = new ArrayList<>();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddedItemDetailFilling_2.this, AddedItemDetailFilling_3.class);
                intent.putExtra("model",model);
                intent.putExtra("postid",postid);
                startActivity(intent);
                finish();
            }
        });

        fac_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alert = new AlertDialog.Builder(AddedItemDetailFilling_2.this);
                View alertView = getLayoutInflater().inflate(R.layout.custom_alert_layout, null);



                //Set the view
                alert.setView(alertView);
                //Show alert
                final AlertDialog alertDialog = alert.show();
                //Can not close the alert by touching outside.
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView closeButton = (ImageView) alertView.findViewById(R.id.closeButton);
                ImageView iv_gallery_upload = (ImageView) alertView.findViewById(R.id.iv_gallery_uploado);
                ImageView iv_vdo_upload = (ImageView) alertView.findViewById(R.id.iv_vdo_upload);
                ImageView iv_camera_upload = (ImageView) alertView.findViewById(R.id.iv_camera_upload);

                if(j>0)
                    iv_vdo_upload.setVisibility(View.GONE);

                iv_gallery_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 30);
                        alertDialog.dismiss();
                    }
                });

                iv_vdo_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        startActivityForResult(intent, 32);
                        alertDialog.dismiss();
                    }
                });

                iv_camera_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 34);
                        alertDialog.dismiss();
                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

//        fac_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent();
////                intent.setAction(Intent.ACTION_GET_CONTENT);
////                intent.setType("image/*");
////                startActivityForResult(intent, 30);
//
////
//
//
//            }
//        });

//        vv_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("video/*");
//                startActivityForResult(intent, 32);
//            }
//        });

//                FragmentFileUpload fragmentFileUpload = new FragmentFileUpload();
//                fragmentFileUpload.show(getSupportFragmentManager(), fragmentFileUpload.getTag());

        imageUploadAdapter = new ImageUploadAdapter(this, list);
        rv_img.setAdapter(imageUploadAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30 && resultCode == RESULT_OK) {
            if (data.getData() != null) {

                imguri1 = data.getData();

                list.add(imguri1);
                imageUploadAdapter.notifyDataSetChanged();

                final StorageReference ref = storage.getReference().child("post_files").child(postid).child("images").child(i+"");
                ref.putFile(imguri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                i++;
                                database.getReference().child("post_files").child(postid).child("images").push().setValue(uri.toString());
                                Toast.makeText(AddedItemDetailFilling_2.this, "IMAGE SUCESSFULLY ADDED", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddedItemDetailFilling_2.this, "IMAGE NOT ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }

        if (requestCode == 32 && resultCode == RESULT_OK) {
            if (data.getData() != null) {

                vdo_uri = data.getData();
//                video_list.add(vdo_uri);
                vv_upload.setVideoURI(vdo_uri);
                vv_upload.seekTo(1);
                j++;

                final StorageReference ref = storage.getReference().child("post_files").child(postid).child("videos").child(j+"");
                ref.putFile(vdo_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                database.getReference().child("post_files").child(postid).child("videos").push().setValue(uri.toString());
                                Toast.makeText(AddedItemDetailFilling_2.this, "VIDEO SUCESSFULLY ADDED", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddedItemDetailFilling_2.this, "IMAGE NOT ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }

        if (requestCode == 34 && resultCode == RESULT_OK) {
            if (data.getData() != null) {

                Uri uri = data.getData();
                Toast.makeText(AddedItemDetailFilling_2.this, uri.toString(), Toast.LENGTH_LONG).show();

                list.add(uri);
                imageUploadAdapter.notifyDataSetChanged();

                final StorageReference ref = storage.getReference().child("post_files").child(postid).child("images").child(i+"");
                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                i++;
                                database.getReference().child("post_files").child(postid).child("images").push().setValue(uri.toString());
                                Toast.makeText(AddedItemDetailFilling_2.this, "IMAGE SUCESSFULLY ADDED", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddedItemDetailFilling_2.this, "IMAGE NOT ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }


    }


}