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
    String rec_phn,msg_id;


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

        Query query_donoractivity= rootRef.child("Notifications").child("Donor").orderByKey();
        query_donoractivity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()!=0){
                    /*for ( DataSnapshot data : dataSnapshot.getChildren()){
                        if (data.getKey().equals(donor_phn)){
                            Toast.makeText(NotificationActivity.this, ""+data.getKey(), Toast.LENGTH_SHORT).show();
                            for (DataSnapshot data_info :data.getChildren()){
                                RecipientDetail recipientDetail = data_info.getValue(RecipientDetail.class);
                                 rec_phn = recipientDetail.getRec_phn();
                                 msg_id = recipientDetail.getMsg_id();
                                Log.i("re_phn77",""+rec_phn);
                                Log.i("Msg_id77",""+msg_id);

                                getDonorActivity(rec_phn,msg_id);

                                //Log.i("re_phn77",""+recipientDetail.getRec_phn());

                            }
                        }
                    }*/
                    //Log.i("donateblood11", "" + dataSnapshot.getKey());
                    for (DataSnapshot data_info :dataSnapshot.getChildren()) {

                        if (data_info.getKey().equals(donor_phn)) {
                            Log.i("donateblood11", "" + data_info.getKey());
                            RecipientDetail recipientDetail = data_info.getValue(RecipientDetail.class);
                            rec_phn = recipientDetail.getRec_phn();
                            msg_id = recipientDetail.getMsg_id();
                            Log.i("re_phn77", "" + rec_phn);
                            Log.i("Msg_id77", "" + msg_id);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query query_catlist = rootRef.child("Notifications").child("Recipient").child(city_donor).child(blood_group_donor).orderByKey().limitToLast(10);

        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {
                   // Toast.makeText(NotificationActivity.this,""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
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

    private void getDonorActivity(String rec_phn, final String msg_id) {

        Query query_recipient= rootRef.child("RecipientNotification").child("Recipient").child("cuttack").child("B+").orderByKey().equalTo(rec_phn);
        query_recipient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount()!=0){
                    for (DataSnapshot data1 : dataSnapshot.getChildren()){
                        for (DataSnapshot data2 :data1.getChildren()){


                            if (data2.getKey().equals(msg_id)){
                                Log.i("key987",data2.getKey());
                                Log.i("key987",""+data2.getValue());


                            }

                        }


                    }



                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
