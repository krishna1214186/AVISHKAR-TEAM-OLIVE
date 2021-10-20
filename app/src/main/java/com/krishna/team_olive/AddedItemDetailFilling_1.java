package com.krishna.team_olive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class AddedItemDetailFilling_1 extends AppCompatActivity {
    EditText et_name,et_age,et_description,et_adress,et_landmark,et_pincode;
    Button btn_next;
    DatabaseReference dataRefrence;
    FirebaseAuth auth;
    String postid2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_item_detail_filling1);
        et_name=findViewById(R.id.et_name);
        et_age=findViewById(R.id.et_yearold);
        et_description=findViewById(R.id.et_description);
        et_adress=findViewById(R.id.et_adress);
        et_landmark=findViewById(R.id.et_landmark);
        et_pincode=findViewById(R.id.et_pincode);
        btn_next=findViewById(R.id.btn_next);

        auth = FirebaseAuth.getInstance();
        dataRefrence= FirebaseDatabase.getInstance().getReference();


        String cateogary=getIntent().getStringExtra("cateogary_name");

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_name.getText().toString();
                String age=et_age.getText().toString();
                String description=et_description.getText().toString();
                String adress=et_adress.getText().toString();
                String landmark=et_landmark.getText().toString();
                String pincode=et_pincode.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(age)||TextUtils.isEmpty(description)||TextUtils.isEmpty(adress)
                        ||TextUtils.isEmpty(landmark)||TextUtils.isEmpty(pincode))
                {
                    Toast.makeText(AddedItemDetailFilling_1.this, "Please enter all fields!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AddedItemDescriptionModel model = new AddedItemDescriptionModel("",cateogary, name, age, description, adress, landmark, "", pincode,
                            "","","2","",auth.getCurrentUser().getUid());

                    uploadData(model);

                    Intent intent = new Intent(AddedItemDetailFilling_1.this, AddedItemDetailFilling_2.class);
                    intent.putExtra("postid",postid2);
                    intent.putExtra("model", model);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }


    public void uploadData(AddedItemDescriptionModel m)
    {
        DatabaseReference dataRefrence2 = dataRefrence.child("allpostswithoutuser").push();
        dataRefrence2.setValue(m);
        postid2 = dataRefrence2.getKey().toString();
        dataRefrence2.child("postid").setValue(postid2);
        m.setPostid(postid2);

        dataRefrence.child("postidwirhuserid").child(m.getPostid()).push().setValue(auth.getCurrentUser().getUid());

    }


}