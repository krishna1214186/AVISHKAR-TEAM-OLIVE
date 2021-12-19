package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    TabLayout tl_profile;
    ViewPager vp_profile;
    ImageView iv_edit_prof, iv_profile_img, iv_logout;

    float v =0;

    String profile_link;

    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference strref;
    DatabaseReference reference;
    CollapsingToolbarLayout collapsingToolbarLayout;

    Uri imageUri;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tl_profile = findViewById(R.id.tl_profile);
        vp_profile = findViewById(R.id.vp_profile);
        iv_edit_prof = findViewById(R.id.iv_profile_edit);
        iv_profile_img = findViewById(R.id.iv_profile_img);
        iv_logout = findViewById(R.id.iv_profile_logout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        auth = FirebaseAuth.getInstance();



        tl_profile.addTab(tl_profile.newTab().setText("Info"));
        tl_profile.addTab(tl_profile.newTab().setText("My Reviews"));
        tl_profile.setTabGravity(TabLayout.GRAVITY_FILL);

        storage = FirebaseStorage.getInstance();
        strref = storage.getReference().child("User profile");

        Picasso.get().load(R.drawable.ic_baseline_person_24).placeholder(R.drawable.ic_baseline_person_24).into(iv_profile_img);

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                collapsingToolbarLayout.setTitle(users.getName());
                if(!users.getProfileimg().isEmpty()) {
                    Picasso.get().load(users.getProfileimg()).placeholder(R.drawable.ic_baseline_person_24).into(iv_profile_img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ProfileAdapter adapter = new ProfileAdapter(getSupportFragmentManager(), this, tl_profile.getTabCount());
        vp_profile.setAdapter(adapter);

        vp_profile.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_profile));

//        tl_profile.setTranslationY(300);
//
//        tl_profile.setAlpha(v);
//
//        tl_profile.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();



        iv_edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,32);
            }
        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfileActivity.this, "logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileActivity.this,LoginActivity.class);
                startActivity(intent);
//                finish();
            }
        });


//        collapsingToolbarLayout.setScrollY(1);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==32 && resultCode==RESULT_OK) {
            if (data.getData() != null) {

                imageUri = data.getData();
                //
                Picasso.get().load(imageUri.toString()).placeholder(R.drawable.ic_baseline_person_24).into(iv_profile_img);
                final StorageReference ref = storage.getReference().child("user profile").child(auth.getCurrentUser().getUid());
                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profile_link = uri.toString();
                                reference.child("profileimg").setValue(profile_link);

                                Toast.makeText(UserProfileActivity.this, "IMAGE SUCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserProfileActivity.this, "IMAGE NOT ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }


    }

}