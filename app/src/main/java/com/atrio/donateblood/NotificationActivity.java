package com.atrio.donateblood;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.atrio.donateblood.adapter.RecycleviewAdapter;
import com.atrio.donateblood.model.RecipientDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<RecipientDetail> arrayList;
    DatabaseReference rootRef;
    ArrayList<String> arr;
    String city_donor,blood_group_donor,donor_phn;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    SharedPreferences sharedpreferences;


    public static final String MyPREFERENCES = "BloodDonate" ;
    public static final String city = "cityKey";
    public static final String state = "stateKey";
    public static final String blood_group = "blood_groupKey";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rc_notify);
        LinearLayoutManager lLayout = new LinearLayoutManager(NotificationActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        donor_phn = user.getPhoneNumber();

        rootRef = FirebaseDatabase.getInstance().getReference();
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        city_donor = sharedpreferences.getString(city,"");
        blood_group_donor = sharedpreferences.getString(blood_group,"");
        Log.i("city_donor44",""+city_donor);
        Log.i("blood_group_donor44",""+blood_group_donor);


        Query query_catlist = rootRef.child("Notifications").child("Recipient").child(city_donor).child(blood_group_donor).orderByKey().limitToLast(10);

        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {
                    Toast.makeText(NotificationActivity.this,""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
//                    arr = new ArrayList<String>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        RecipientDetail r_detail=dataSnapshot1.getValue(RecipientDetail.class);
                       r_detail.setBody(r_detail.body);
                       // r_detail.setType("Recipient");
//                        Log.i("array7712555",""+r_detail);
                        arrayList.add(r_detail);


                    }
//                    Log.i("array7712555",""+arrayList);

                    Collections.reverse(arrayList);
                    RecycleviewAdapter rcAdapter = new RecycleviewAdapter(NotificationActivity.this, arrayList);
                    recyclerView.setAdapter(rcAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

       /* Query query_donorlist = rootRef.child("Notification").child("Recipient").orderByKey().limitToLast(10);

        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {
//                    arr = new ArrayList<String>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        RecipientDetail r_detail=dataSnapshot1.getValue(RecipientDetail.class);
                        r_detail.setBody(r_detail.body);
                        r_detail.setType("Recipient");
//                        Log.i("array7712555",""+r_detail);
                        arrayList.add(r_detail);


                    }
//                    Log.i("array7712555",""+arrayList);

                    Collections.reverse(arrayList);
                    RecycleviewAdapter rcAdapter = new RecycleviewAdapter(NotificationActivity.this, arrayList);
                    recyclerView.setAdapter(rcAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });*/



    }

}
