package com.krishna.team_olive;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddedItemDetailFilling_2 extends AppCompatActivity {
ImageView iv_imageselected;
Button btn_camera,btn_device,btn_next;
    String currentPhotoPath;
    String extension=null;
    Uri imageUri;
    ActivityResultLauncher<Intent> startForResult=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result!=null&&result.getResultCode()==RESULT_OK)
            {
                if(result.getData()!=null)
                {
                    File f=new File(currentPhotoPath);
                    iv_imageselected.setImageURI(Uri.fromFile(f));
                    imageUri=Uri.fromFile(f);
                    extension=getExtension(Uri.fromFile(f));
                }
            }
        }
    });
    ActivityResultLauncher<Intent> startForResult2=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result!=null&&result.getResultCode()==RESULT_OK)
            {
                if(result.getData()!=null)
                {
                imageUri=result.getData().getData();

                    Picasso.get().load(imageUri).into(iv_imageselected);

                    extension=getExtension(imageUri);
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


                dispatchTakePictureIntent();

            }
        });
        btn_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");//type of choices you have to show the user(MIME)
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startForResult2.launch(intent);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(extension==null)
{
    Toast.makeText(AddedItemDetailFilling_2.this, "please first select a image!!", Toast.LENGTH_SHORT).show();
}
else
{
    model.setExtension(extension);
    Intent intent=new Intent(AddedItemDetailFilling_2.this,AddedItemDetailFilling_3.class);
    intent.putExtra("model",model);
    intent.putExtra("uriImage",new UriContainer(imageUri));
    startActivity(intent);
}
            }
        });

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File  storageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    static final int REQUEST_IMAGE_CAPTURE=1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startForResult.launch(takePictureIntent);
            }
        }
    }
    public String getExtension(Uri uri)//this function returns the extension like JPEG
    {
        if(uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cr.getType(uri));
        }
        else
        {
           return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
    }
}