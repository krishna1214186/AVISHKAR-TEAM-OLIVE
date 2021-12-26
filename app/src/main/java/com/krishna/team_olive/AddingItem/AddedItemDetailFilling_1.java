package com.krishna.team_olive.AddingItem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.krishna.team_olive.ModelClasses.AddedItemDescriptionModel;
import com.krishna.team_olive.R;

//Second activity for creating a post which includes taking details such as name, age, adress of the item.

public class AddedItemDetailFilling_1 extends AppCompatActivity {
    private TextInputLayout et_name, et_age, et_description, et_adress, et_landmark, et_pincode;
    private Button btn_next;
    private DatabaseReference dataRefrence;
    private FirebaseAuth auth;
    private String postid2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_item_detail_filling1);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_yearold);
        et_description = findViewById(R.id.et_description);
        et_adress = findViewById(R.id.et_adress);
        et_landmark = findViewById(R.id.et_landmark);
        et_pincode=findViewById(R.id.et_pincode);
        btn_next=findViewById(R.id.btn_next);

        auth = FirebaseAuth.getInstance();
        dataRefrence= FirebaseDatabase.getInstance().getReference();


        String cateogary=getIntent().getStringExtra("cateogary_name");
        String donate=getIntent().getStringExtra("donate");

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_name.getEditText().getText().toString();
                String age=et_age.getEditText().getText().toString();
                String description=et_description.getEditText().getText().toString();
                String adress=et_adress.getEditText().getText().toString();
                String landmark=et_landmark.getEditText().getText().toString();
                String pincode=et_pincode.getEditText().getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(age)||TextUtils.isEmpty(description)||TextUtils.isEmpty(adress)
                        ||TextUtils.isEmpty(landmark)||TextUtils.isEmpty(pincode))
                {
                    Toast.makeText(AddedItemDetailFilling_1.this, "Please enter all fields!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AddedItemDescriptionModel model = new AddedItemDescriptionModel("",cateogary, name, age, description, adress, landmark, "", pincode,
                            donate,"","2","",auth.getCurrentUser().getUid());

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