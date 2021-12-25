package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ProfileActivity extends AppCompatActivity {
    ImageView iv_edit_prof, iv_profile_img;
Button iv_logout;
    float v = 0;

    String profile_link;

    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference strref;
    FirebaseDatabase database;
    DatabaseReference reference;
    Uri imageUri;
    TextView tv_profile_name;
    TextView tv_profile_name2;

    TextView tv_profile_address;
    TextView tv_profile_phone;
    TextView tv_profile_email;
    TextView tv_prof_totpost;
    TextView tv_prof_rating;
    Button btn_edit_prof, btn_done_prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_profile);
        iv_edit_prof = findViewById(R.id.iv_profile_edit);
        iv_profile_img = findViewById(R.id.iv_profile_img);
        iv_logout = findViewById(R.id.iv_profile_logout);
        tv_profile_name = findViewById(R.id.tv_name_prof2);
        tv_profile_address = findViewById(R.id.tv_address_prof);
        tv_profile_name2 = findViewById(R.id.tv_name_prof);
        tv_profile_phone = findViewById(R.id.tv_phone_prof);
        tv_profile_email = findViewById(R.id.tv_email_prof);
        tv_prof_rating = findViewById(R.id.tv_prof_rating);
        tv_prof_totpost = findViewById(R.id.tv_prof_totpost);
        tv_profile_address.setFocusableInTouchMode(false);
        tv_profile_address.setCursorVisible(false);
        auth = FirebaseAuth.getInstance();
        tv_profile_email.setFocusableInTouchMode(false);
        tv_profile_email.setCursorVisible(false);
        tv_profile_email.setFocusable(true);
        tv_profile_email.setEnabled(true);

        tv_profile_phone.setFocusableInTouchMode(false);
        tv_profile_phone.setCursorVisible(false);
        btn_edit_prof = findViewById(R.id.btn_edit_prof);
        btn_done_prof = findViewById(R.id.btn_done_prof);
        btn_done_prof.setVisibility(View.INVISIBLE);
        btn_edit_prof.setVisibility(View.VISIBLE);
        tv_profile_name.setFocusableInTouchMode(false);
        tv_profile_name.setCursorVisible(false);
        String uid = auth.getCurrentUser().getUid().toString();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        strref = storage.getReference().child("User profile");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                tv_profile_name.setText(users.getName());
                tv_profile_name2.setText(users.getName());
                tv_profile_address.setText(users.getLocation());
                tv_profile_email.setText(users.getEmail());
                tv_profile_phone.setText(users.getPhone());
                if (!users.getProfileimg().isEmpty()) {
                    Picasso.get().load(users.getProfileimg()).into(iv_profile_img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getInstance().getReference().child("mypostswithuser").child(auth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_prof_totpost.setText(String.valueOf((int) snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getInstance().getReference().child("Ratings").child(auth.getCurrentUser().getUid().toString()).child("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()==null){
                    tv_prof_rating.setText("Not rated");
                }else{
                    tv_prof_rating.setText(String.valueOf((long) snapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference = database.getInstance().getReference().child("users").child(uid);
        btn_edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btn_done_prof.setVisibility(View.VISIBLE);
                btn_edit_prof.setVisibility(View.INVISIBLE);

                tv_profile_address.setFocusableInTouchMode(true);
                tv_profile_address.setCursorVisible(true);

                tv_profile_email.setFocusableInTouchMode(false);
                tv_profile_email.setCursorVisible(false);
                tv_profile_email.setFocusable(false);
                tv_profile_email.setEnabled(false);


                tv_profile_phone.setFocusableInTouchMode(true);
                tv_profile_phone.setCursorVisible(true);

                tv_profile_name.setFocusableInTouchMode(true);
                tv_profile_name.setCursorVisible(true);

            }
        });
        btn_done_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_done_prof.setVisibility(View.INVISIBLE);
                btn_edit_prof.setVisibility(View.VISIBLE);

                tv_profile_email.setFocusableInTouchMode(false);
                tv_profile_email.setCursorVisible(false);
                tv_profile_email.setFocusable(true);
                tv_profile_email.setEnabled(true);

                tv_profile_address.setFocusableInTouchMode(false);
                tv_profile_address.setCursorVisible(false);

                tv_profile_name.setFocusableInTouchMode(false);
                tv_profile_name.setCursorVisible(false);

                tv_profile_phone.setFocusableInTouchMode(false);
                tv_profile_phone.setCursorVisible(false);


                reference.child("location").setValue(tv_profile_address.getText().toString());
                reference.child("name").setValue(tv_profile_name.getText().toString());
                reference.child("phone").setValue(tv_profile_phone.getText().toString());

            }
        });


        iv_edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 32);
            }
        });
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(ProfileActivity.this, "logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 32 && resultCode == RESULT_OK) {
            if (data.getData() != null) {

                imageUri = data.getData();
                //
                Picasso.get().load(imageUri.toString()).into(iv_profile_img);
                final StorageReference ref = storage.getReference().child("user profile").child(auth.getCurrentUser().getUid());
                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profile_link = uri.toString();
                                reference.child("profileimg").setValue(profile_link);

                                Toast.makeText(ProfileActivity.this, "IMAGE SUCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "IMAGE NOT ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }
    }
}