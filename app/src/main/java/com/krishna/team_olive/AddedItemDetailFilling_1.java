package com.krishna.team_olive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddedItemDetailFilling_1 extends AppCompatActivity {
EditText et_name,et_age,et_description,et_adress,et_landmark,et_pincode;
Button btn_next;
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
                    Toast.makeText(AddedItemDetailFilling_1.this, "PLease enter all fiels!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(AddedItemDetailFilling_1.this, AddedItemDetailFilling_2.class);
                    AddedItemDescriptionModel model = new AddedItemDescriptionModel(cateogary, name, age, description, adress, landmark, null, pincode,null,null,null,null);
                    intent.putExtra("model", model);
                    startActivity(intent);
                }

            }
        });


    }
}