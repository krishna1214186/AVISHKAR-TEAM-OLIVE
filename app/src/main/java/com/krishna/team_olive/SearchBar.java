package com.krishna.team_olive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchBar extends AppCompatActivity {


    AutoCompleteTextView et_search;
    ImageView iv_search;

    ArrayList<String> search_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        et_search = findViewById(R.id.et_Search);
        iv_search = findViewById(R.id.ic_search);

        search_list = new ArrayList<>();
        for(int i=0;i<FragmentHome.addedItemDescriptionModelArrayList.size();i++){
            search_list.add(FragmentHome.addedItemDescriptionModelArrayList.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.search_autocomplete_element, search_list);
        et_search.setThreshold(1);
        et_search.setAdapter(adapter);

        iv_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String s = et_search.getText().toString();
                int t = 0;

                for (int j = 0; j < FragmentHome.addedItemDescriptionModelArrayList.size(); j++) {

                    if (s.equals(FragmentHome.addedItemDescriptionModelArrayList.get(j).getName())) {

                        t = 1;
                        Intent searchIntent = new Intent(SearchBar.this, MainActivity.class);
                        searchIntent.putExtra("postname", FragmentHome.addedItemDescriptionModelArrayList.get(j).getName());
                        startActivity(searchIntent);
                        break;
                    }
                }

                if(t==0)
                {
                    Toast.makeText(SearchBar.this, "sorry! there is no such event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}