package com.krishna.team_olive;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FragmentAdd.ItemSelected {
    MeowBottomNavigation meowBottomNavigation;
    int SEARCH_RETURN = 01;
    Fragment fragment=null;


FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=FirebaseDatabase.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        meowBottomNavigation=findViewById(R.id.bottom_navigation);
        //manageConnections();
       // manageConnections2();
       /* HashMap<String,Object> State=new HashMap<>();
        Long timeTamp=new Date().getTime();
        State.put("timestamp",timeTamp);

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("lastconnected").onDisconnect()
                .updateChildren(State);*/

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.drawable_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.drawable_chat));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.drawable_add));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.drawable_notification));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.drawable_profile));
       meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
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
        meowBottomNavigation.show(1,true);
        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });
       meowBottomNavigation .setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
     /*  DatabaseReference databaseReference=db.getReference(".info/lastconnected");
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Long key=snapshot.child("time").getValue(Long.class);
               Toast.makeText(MainActivity.this,""+key, Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });*/

       /* FirebaseDatabase.getInstance().getReference().child("lastConnected")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    Long key = childSnapshot.child("time").getValue(Long.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

   /* private void manageConnections2() {
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference().child(uid).child("connections");

// Stores the timestamp of my last disconnect (the last time I was seen online)
        final DatabaseReference lastOnlineRef = database.getReference().child(uid).child("lastconnected");

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    DatabaseReference con = myConnectionsRef.push();

                    // When this device disconnects, remove it
                    con.onDisconnect().removeValue();

                    // When I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);

                    // Add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    con.setValue(Boolean.TRUE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }*/

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
    }

    @Override
    public void onItemSelected(String str) {
        Intent intent=new Intent(this,AddedItemDetailFilling_1.class);
        intent.putExtra("cateogary_name",str);
        startActivity(intent);
    }
/*
public void manageConnections()
{
    DatabaseReference connectionRefrence=db.getReference().child("connections");
  //  DatabaseReference lastConnected=db.getReference().child("lastConnected").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("time");
    DatabaseReference infoLconnected=db.getReference(".info/lastconnected");
    DatabaseReference infoConnected=db.getReference(".info/connected");
    infoConnected.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           // boolean connected=snapshot.getValue(Boolean.class);


          DatabaseReference con=connectionRefrence.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
         // con.setValue(ServerValue.TIMESTAMP);
            con.setValue(true);
          con.onDisconnect().setValue(false);




         // lastConnected.onDisconnect().setValue(ServerValue.TIMESTAMP);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

}

 */

private void updateUserStatus(String state)
{
    String saveCurrentTime,saveCurrentDate;
    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat currenDate=new SimpleDateFormat("MMM dd, yyyy");
    saveCurrentDate=currenDate.format(calendar.getTime());
    SimpleDateFormat currenTime=new SimpleDateFormat("hh:mm a");
    saveCurrentTime=currenTime.format(calendar.getTime());
    HashMap<String,Object> onLineState=new HashMap<>();
    onLineState.put("time",saveCurrentTime);
    onLineState.put("date",saveCurrentDate);
    onLineState.put("state",state);


    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("userstate").
            updateChildren(onLineState);


}

  /* @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            updateUserStatus("online");
        }
    }*/

   /* @Override
    protected void onStop() {
        super.onStop();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            updateUserStatus("offline");

        }

    }*/

   /* @Override
    protected void onPause() {
        super.onPause();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            updateUserStatus("online");

        }
    }*/

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus("offline");


    }*/
}