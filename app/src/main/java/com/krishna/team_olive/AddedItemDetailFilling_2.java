package com.krishna.team_olive;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AddedItemDetailFilling_2 extends AppCompatActivity {
ImageView iv_imageselected;
Button btn_camera,btn_device,btn_next;
    ActivityResultLauncher<Intent> startForResult=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result!=null&&result.getResultCode()==RESULT_OK)
            {
                if(result.getData()!=null)
                {
                    Bitmap captureImage= (Bitmap) result.getData().getExtras().get("data");
                    iv_imageselected.setImageBitmap(captureImage);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_item_detail_filling2);
        AddedItemDescriptionModel model=getIntent().getParcelableExtra("model");
        iv_imageselected=findViewById(R.id.iv_imageselected);
        btn_camera=findViewById(R.id.btn_camera);
        btn_device=findViewById(R.id.btn_select_folder);
        btn_next=findViewById(R.id.btn_next);
        if(ContextCompat.checkSelfPermission(AddedItemDetailFilling_2.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(AddedItemDetailFilling_2.this,new String[]
                    {
                            Manifest.permission.CAMERA
                    },100);
        }
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startForResult.launch(intent);

            }
        });

    }
}