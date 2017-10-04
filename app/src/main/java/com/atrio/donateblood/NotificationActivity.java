package com.atrio.donateblood;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.atrio.donateblood.adapter.RecycleviewAdapter;
import com.atrio.donateblood.model.RecipientDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import dmax.dialog.SpotsDialog;


public class NotificationActivity extends AppCompatActivity {
    RecyclerView rc_donor;
    Button tv_donor,tv_recipient;
    ArrayList<RecipientDetail> arrayList,donoractivityList;
    DatabaseReference rootRef;
    ArrayList<String> arry_bloolist;
    String city_donor,blood_group_donor,donor_phn,noti_bloodGroup,noti_bloodGroup1,noti_bloodGroup2,noti_bloodGroup3,noti_bloodGroup4,noti_bloodGroup5
            ,noti_bloodGroup6,noti_bloodGroup7,req_date;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    RecycleviewAdapter rcAdapter;
    SharedPreferences sharedpreferences;
    private SpotsDialog dialog;
    int compare;
    Date now,parsed;
    SimpleDateFormat sdf;
    public static final String MyPREFERENCES = "BloodDonate" ;
    public static final String city = "cityKey";
    public static final String state = "stateKey";
    public static final String blood_group = "blood_groupKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        donoractivityList = new ArrayList<>();
        rc_donor = (RecyclerView) findViewById(R.id.rc_donor);
        tv_donor=(Button) findViewById(R.id.tv_donor);
        tv_recipient=(Button) findViewById(R.id.tv_recipient);
        dialog = new SpotsDialog(NotificationActivity.this, R.style.Custom);
        dialog.show();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        now = new Date(System.currentTimeMillis());
//        now = new Date(System.currentTimeMillis());

        LinearLayoutManager lLayoutd = new LinearLayoutManager(NotificationActivity.this);

        rc_donor.setHasFixedSize(true);
        rc_donor.setLayoutManager(lLayoutd);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        donor_phn = user.getPhoneNumber();
        arry_bloolist = new ArrayList<>();
        arrayList = new ArrayList<>();

        rootRef = FirebaseDatabase.getInstance().getReference();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        city_donor = sharedpreferences.getString(city, "");
        blood_group_donor = sharedpreferences.getString(blood_group, "");
        Calendar c = Calendar.getInstance();

//        Log.i("Current_io " ,""+ c.getTime());
        /*Log.i("city_donor44", "" + city_donor);*/
        /*Log.i("blood_group_donor44", "" + blood_group_donor);*/

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
                noti_bloodGroup3 = "AB-";
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
        tv_donor.setBackgroundResource(R.drawable.ripple_effect);
        tv_recipient.setBackgroundResource(R.drawable.disable_btn);
//        Log.i("blood_donor111",""+donoractivityList.size());
//        Log.i("blood_donor112",""+arrayList.size());
        arrayList.clear();
        donoractivityList.clear();
        dialog.dismiss();
        rcAdapter = new RecycleviewAdapter(NotificationActivity.this, donoractivityList);
        rc_donor.setAdapter(rcAdapter);
        dialog.show();
        Query query_donoractivity = rootRef.child("Notifications").child("Donor").orderByKey().limitToLast(10);
        query_donoractivity.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getChildrenCount() != 0) {
//                    for (DataSnapshot data_info : dataSnapshot.getChildren()) {
//                        Log.i("donateblood11", "" + dataSnapshot.getKey());
                        /*Log.i("donateblood11", "" + donor_phn);*/

                        if (dataSnapshot.getKey().equals(donor_phn)) {

                            /*Log.i("donateif", "" + donor_phn);*/
                            for (DataSnapshot data_info1 : dataSnapshot.getChildren()) {
                                RecipientDetail recipientDetail = data_info1.getValue(RecipientDetail.class);
                                recipientDetail.setType("donorwilling");
                                recipientDetail.setPhoneno(recipientDetail.phoneno);
                                recipientDetail.setBloodgroup(recipientDetail.bloodgroup);
                                donoractivityList.add(recipientDetail);
                            }

                        } else {
                            dialog.dismiss();
                        }
//                    }
                    dialog.dismiss();
                    Collections.reverse(donoractivityList);
                    rcAdapter = new RecycleviewAdapter(NotificationActivity.this, donoractivityList);
                    rc_donor.setAdapter(rcAdapter);
                } else {
                    dialog.dismiss();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.dismiss();
        tv_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_donor.setBackgroundResource(R.drawable.ripple_effect);
                tv_recipient.setBackgroundResource(R.drawable.disable_btn);
//                Log.i("blood_donor113",""+donoractivityList.size());
//                Log.i("blood_donor114",""+arrayList.size());

                donoractivityList.clear();
                arrayList.clear();
                rcAdapter = new RecycleviewAdapter(NotificationActivity.this, donoractivityList);
                rc_donor.setAdapter(rcAdapter);
//                Log.i("blood_donor115",""+donoractivityList.size());
//                Log.i("blood_donor116",""+arrayList.size());
                dialog.show();
                Query query_donoractivity = rootRef.child("Notifications").child("Donor").orderByKey().limitToLast(10);
                query_donoractivity.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                           // for (DataSnapshot data_info : dataSnapshot.getChildren()) {
//                                Log.i("donateblood116", "" + donoractivityList.size());
                              /**/  /*Log.i("donateblood11", "" + donor_phn);*/

                                if (dataSnapshot.getKey().equals(donor_phn)) {

                                    /*Log.i("donateif", "" + donor_phn);*/
                                    for (DataSnapshot data_info1 : dataSnapshot.getChildren()) {
                                        RecipientDetail recipientDetail = data_info1.getValue(RecipientDetail.class);
                                        recipientDetail.setType("donorwilling");
                                        recipientDetail.setPhoneno(recipientDetail.phoneno);
                                        recipientDetail.setBloodgroup(recipientDetail.bloodgroup);
                                        donoractivityList.add(recipientDetail);
                                    }

                                } else {
                                    dialog.dismiss();
                                }
//                            }
                            dialog.dismiss();
                            Collections.reverse(donoractivityList);
                            rcAdapter = new RecycleviewAdapter(NotificationActivity.this, donoractivityList);
                            rc_donor.setAdapter(rcAdapter);
                        } else {
                            dialog.dismiss();

                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();

            }
        });
        tv_recipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_recipient.setBackgroundResource(R.drawable.ripple_effect);
                tv_donor.setBackgroundResource(R.drawable.disable_btn);

                donoractivityList.clear();
                arrayList.clear();
                rcAdapter = new RecycleviewAdapter(NotificationActivity.this, arrayList);
                rc_donor.setAdapter(rcAdapter);


//                Log.i("blood_donor119",""+donoractivityList.size());
//                Log.i("blood_donor110",""+arrayList.size());
                dialog.show();
                for (int i = 0; i < arry_bloolist.size(); i++) {
                    blood_group_donor = arry_bloolist.get(i);
//                    Log.i("blood_donor110135",""+arry_bloolist);
//                    Log.i("blood_donor11013",""+arrayList.size());

                    Query query_catlist = rootRef.child("Notifications").child("Recipient").child(city_donor).child(blood_group_donor).orderByKey().limitToLast(5);

                    query_catlist.addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            Log.i("blood_donor1101",""+arrayList.size());

                            if (dataSnapshot.getChildrenCount() != 0) {
//                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                    Log.i("blood_donor1102",""+arrayList.size());
                                    RecipientDetail r_detail = dataSnapshot.getValue(RecipientDetail.class);

//                                    Log.i("r_detail.body11",""+r_detail.body);
                                    r_detail.setBody(r_detail.body);
                                    r_detail.setType("recipientwilling");
                                    req_date = r_detail.getReq_date();

                                    try {
                                        parsed = sdf.parse(req_date);
//                                        Log.i("r_detail_com12",""+parsed);
//                                        Log.i("r_detail_com13",""+now);

                                        compare = parsed.compareTo(now);
//                                        Log.i("r_detail_com1",""+compare);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (compare !=-1) {
//                                        Log.i("r_detail.body114",""+r_detail.body);

                                        arrayList.add(r_detail);
                                    }
//                                }
                                dialog.dismiss();
                                Collections.reverse(arrayList);
                                rcAdapter = new RecycleviewAdapter(NotificationActivity.this, arrayList);
                                rc_donor.setAdapter(rcAdapter);

                            } else {
                                dialog.dismiss();

                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                    dialog.dismiss();

                }
                dialog.dismiss();
            }
        });

    }




}
