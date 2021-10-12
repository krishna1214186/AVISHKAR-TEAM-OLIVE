package com.krishna.team_olive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity implements FragmentAdd.ItemSelected, FragmentHome.searchselected {
    MeowBottomNavigation meowBottomNavigation;
    int SEARCH_RETURN = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        meowBottomNavigation=findViewById(R.id.bottom_navigation);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.drawable_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.drawable_chat));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.drawable_add));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.drawable_notification));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.drawable_profile));
       meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment=null;
                switch(item.getId()) {
                    case 1:
                        fragment = new FragmentHome();
                        break;
                    case 2:
                        fragment = new FragmentChat();
                        break;
                    case 3:
                        fragment = new FragmentAdd();
                        break;
                    case 4:
                        fragment = new FragmentNotification();
                        break;
                    case 5:
                        fragment = new FragmentProfile();
                }
loadFragment(fragment);

                }
           });
        meowBottomNavigation.show(3,true);
        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(),"you clicked"+item.getId(),Toast.LENGTH_SHORT).show();
            }
        });
       meowBottomNavigation .setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(),"you reselected"+item.getId(),Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
    }

    @Override
    public void onItemSelected(String str) {
        Intent intent=new Intent(this,AddedItemDetailFilling_1.class);
        intent.putExtra("cateogary_name",str);
        startActivity(intent);
    }

    @Override
    public void onsearchselected() {
        Intent intent = new Intent(this,SearchBar.class);
        startActivityForResult(intent,SEARCH_RETURN);

    }


}