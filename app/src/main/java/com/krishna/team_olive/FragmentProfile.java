package com.krishna.team_olive;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentProfile extends Fragment {

    private static final int RESULT_OK = -1;
    public ArrayList<Users> detail;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference strref;
    DatabaseReference reference;

    String profile_link;

    ImageView iv_edit_name, iv_edit_phone, iv_edit_ads,iv_veiw_history,iv_edit_profilepic, iv_edit_location;
    TextView tv_name,tv_mail, tv_phone, tv_location;
    Button btn_logout;
    RatingBar rb_rating;
    CircleImageView iv_profile;
    AlertDialog dialog_name, dialog_phone, dialog_location;
    EditText et_phone, et_name, et_location ;


    String extension=null;
    Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);


        iv_edit_name = view.findViewById(R.id.iv_edit_name);
        iv_edit_phone = view.findViewById(R.id.iv_edit_phone);
        iv_edit_ads = view.findViewById(R.id.iv_ads);
        iv_edit_location = view.findViewById(R.id.iv_edit_location);
        iv_edit_profilepic = view.findViewById(R.id.iv_edit_profilepic);
        tv_mail = view.findViewById(R.id.tv_mail);
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        btn_logout = view.findViewById(R.id.btn_logout);
        iv_veiw_history = view.findViewById(R.id.iv_view_history);
        iv_profile = view.findViewById(R.id.iv_profile);
        rb_rating = view.findViewById(R.id.rb_user);
        tv_location = view.findViewById(R.id.tv_location);





        detail = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        strref = storage.getReference().child("User profile");



        reference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detail.clear();

                Users users = snapshot.getValue(Users.class);
               tv_name.setText(users.getName());
                tv_phone.setText(users.getPhone());
                tv_mail.setText(users.getEmail());
                tv_location.setText(users.getLocation());
              if(!users.getProfileimg().isEmpty()) {
                   Picasso.get().load(users.getProfileimg()).placeholder(R.drawable.ic_baseline_person_24).into(iv_profile);
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rb_rating.setRating(3.5F);
        rb_rating.setIsIndicator(true);

        et_location = new EditText(getContext());
        et_name = new EditText(getContext());
        et_phone = new EditText(getContext());

        dialog_phone = new AlertDialog.Builder(getContext()).create();
        dialog_phone.setTitle("Edit the phone number");
        dialog_phone.setView(et_phone);
        dialog_phone.setButton(DialogInterface.BUTTON_POSITIVE, "Save data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                tv_name.setText(editText.getText().toString());
                reference.child("phone").setValue(et_phone.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tv_phone.setText(et_phone.getText().toString());
                    }
                });
            }
        });
        iv_veiw_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ExchangeHistory.class);
                startActivity(intent);
            }
        });

        dialog_name = new AlertDialog.Builder(getContext()).create();
        dialog_name.setTitle("Edit the name");
        dialog_name.setView(et_name);
        dialog_name.setButton(DialogInterface.BUTTON_POSITIVE, "Save data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reference.child("name").setValue(et_name.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tv_name.setText(et_name.getText().toString());
                    }
                });
            }
        });

        dialog_location = new AlertDialog.Builder(getContext()).create();
        dialog_location.setTitle("Edit the location");
        dialog_location.setView(et_location);
        dialog_location.setButton(DialogInterface.BUTTON_POSITIVE, "Save data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                tv_name.setText(editText.getText().toString());
                reference.child("location").setValue(et_location.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tv_location.setText(et_location.getText().toString());
                    }
                });
            }
        });

        iv_edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_phone.setText(tv_phone.getText().toString());
                dialog_phone.show();
            }
        });

        iv_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_name.setText(tv_name.getText().toString());
                dialog_name.show();
            }
        });

        iv_edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_location.setText(tv_location.getText().toString());
                dialog_location.show();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(view.getContext(),LoginActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        iv_edit_profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,32);
            }
        });








        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==32 && resultCode==RESULT_OK) {
            if (data.getData() != null) {

                imageUri = data.getData();
                //

                final StorageReference ref = storage.getReference().child("user profile").child(auth.getCurrentUser().getUid());
                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profile_link = uri.toString();
                                reference.child("profileimg").setValue(profile_link);
                                Toast.makeText(getContext(), "IMAGE SUCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "IMAGE NOT ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }


    }
}