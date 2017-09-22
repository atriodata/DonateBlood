package com.atrio.donateblood;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import dmax.dialog.SpotsDialog;


public class NotificationActivity extends AppCompatActivity {
    RecyclerView rc_donor,rc_recipient;
    ArrayList<RecipientDetail> arrayList,donoractivityList;
    DatabaseReference rootRef;
    ArrayList<String> arry_bloolist;
    String city_donor,blood_group_donor,donor_phn;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    SharedPreferences sharedpreferences;
    private SpotsDialog dialog;
    String rec_phn,msg_id,noti_bloodGroup,noti_bloodGroup1,noti_bloodGroup2,noti_bloodGroup3,noti_bloodGroup4,noti_bloodGroup5
            ,noti_bloodGroup6,noti_bloodGroup7;
    public static final String MyPREFERENCES = "BloodDonate" ;
    public static final String city = "cityKey";
    public static final String state = "stateKey";
    public static final String blood_group = "blood_groupKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        arrayList = new ArrayList<>();
        donoractivityList = new ArrayList<>();
        rc_donor = (RecyclerView) findViewById(R.id.rc_donor);
        rc_recipient = (RecyclerView) findViewById(R.id.rc_recipient);
        dialog = new SpotsDialog(NotificationActivity.this, R.style.Custom);
        dialog.show();

        LinearLayoutManager lLayoutd = new LinearLayoutManager(NotificationActivity.this);
        LinearLayoutManager lLayoutr = new LinearLayoutManager(NotificationActivity.this);

        rc_donor.setHasFixedSize(true);
        rc_donor.setLayoutManager(lLayoutd);
        rc_recipient.setHasFixedSize(true);
        rc_recipient.setLayoutManager(lLayoutr);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        donor_phn = user.getPhoneNumber();
        arry_bloolist = new ArrayList<>();

        rootRef = FirebaseDatabase.getInstance().getReference();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        city_donor = sharedpreferences.getString(city, "");
        blood_group_donor = sharedpreferences.getString(blood_group, "");
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        Log.i("city_donor44", "" + city_donor);
        Log.i("blood_group_donor44", "" + blood_group_donor);

        switch (blood_group_donor) {
            case "A+":
                noti_bloodGroup = "A+";
                noti_bloodGroup1 = "AB+";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                break;
            case "O+":
                noti_bloodGroup = "O+";
                noti_bloodGroup1 = "A+";
                noti_bloodGroup2 = "B+";
                noti_bloodGroup3 = "AB+";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                arry_bloolist.add(noti_bloodGroup2);
                arry_bloolist.add(noti_bloodGroup3);
                break;
            case "B+":
                noti_bloodGroup = "B+";
                noti_bloodGroup1 = "AB+";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                break;
            case "AB+":
                noti_bloodGroup = "AB+";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                break;
            case "A-":
                noti_bloodGroup = "O+";
                noti_bloodGroup1 = "A+";
                noti_bloodGroup2 = "B+";
                noti_bloodGroup3 = "AB+";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                arry_bloolist.add(noti_bloodGroup2);
                arry_bloolist.add(noti_bloodGroup3);
                break;
            case "O-":
                noti_bloodGroup = "O+";
                noti_bloodGroup1 = "A+";
                noti_bloodGroup2 = "B+";
                noti_bloodGroup3 = "AB+";
                noti_bloodGroup4 = "O-";
                noti_bloodGroup5 = "A-";
                noti_bloodGroup6 = "B-";
                noti_bloodGroup7 = "AB-";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                arry_bloolist.add(noti_bloodGroup2);
                arry_bloolist.add(noti_bloodGroup3);
                arry_bloolist.add(noti_bloodGroup4);
                arry_bloolist.add(noti_bloodGroup5);
                arry_bloolist.add(noti_bloodGroup6);
                arry_bloolist.add(noti_bloodGroup7);
                break;
            case "B-":
                noti_bloodGroup = "B+";
                noti_bloodGroup1 = "B-";
                noti_bloodGroup2 = "AB+";
                noti_bloodGroup3 = "AB_";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                arry_bloolist.add(noti_bloodGroup2);
                arry_bloolist.add(noti_bloodGroup3);
                break;
            case "AB-":
                noti_bloodGroup = "AB+";
                noti_bloodGroup1 = "AB-";
                arry_bloolist.add(noti_bloodGroup);
                arry_bloolist.add(noti_bloodGroup1);
                break;


        }

        Query query_donoractivity = rootRef.child("Notifications").child("Donor").orderByKey().limitToLast(10);
        query_donoractivity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot data_info : dataSnapshot.getChildren()) {
                        Log.i("donateblood11", "" + data_info.getKey());
                        Log.i("donateblood11", "" + donor_phn);

                        if (data_info.getKey().equals(donor_phn)) {

                            Log.i("donateif", "" + donor_phn);
                            for (DataSnapshot data_info1 : data_info.getChildren()) {
                                RecipientDetail recipientDetail = data_info1.getValue(RecipientDetail.class);
                                recipientDetail.setType("donorwilling");
                                recipientDetail.setPhoneno(recipientDetail.phoneno);
                                recipientDetail.setBloodgroup(recipientDetail.bloodgroup);
                                donoractivityList.add(recipientDetail);
                            }

                        } else {
                            dialog.dismiss();
                        }
                    }
                    dialog.dismiss();
                    Collections.reverse(donoractivityList);
                    RecycleviewAdapter rcAdapter = new RecycleviewAdapter(NotificationActivity.this, donoractivityList);
                    rc_donor.setAdapter(rcAdapter);
                } else {
                    dialog.dismiss();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < arry_bloolist.size(); i++) {
            blood_group_donor = arry_bloolist.get(i);
            Log.i("blood_group_donor11",""+blood_group_donor);
            Query query_catlist = rootRef.child("Notifications").child("Recipient").child(city_donor).child(blood_group_donor).orderByKey().limitToLast(5);

            query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
//                    dialog.dismiss();
//                    Toast.makeText(NotificationActivity.this,""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
                        // Toast.makeText(NotificationActivity.this,""+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
//                    arr = new ArrayList<String>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            RecipientDetail r_detail = dataSnapshot1.getValue(RecipientDetail.class);

                            Log.i("r_detail.body11",""+r_detail.body);
                            r_detail.setBody(r_detail.body);
                            r_detail.setType("recipientwilling");
                            arrayList.add(r_detail);
                        }
                        dialog.dismiss();
                        Collections.reverse(arrayList);
                        RecycleviewAdapter rcAdapter = new RecycleviewAdapter(NotificationActivity.this, arrayList);
                        rc_recipient.setAdapter(rcAdapter);

                    } else {
                        dialog.dismiss();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


        }

    }




}
