package com.atrio.donateblood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.atrio.donateblood.adapter.RecycleviewAdapter;
import com.atrio.donateblood.model.RecipientDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<RecipientDetail> arrayList;
    DatabaseReference rootRef;
    ArrayList<String> arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rc_notify);
        LinearLayoutManager lLayout = new LinearLayoutManager(NotificationActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);

        rootRef = FirebaseDatabase.getInstance().getReference();

        Query query_catlist = rootRef.child("Notification").child("Recipient").orderByKey();

        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {
//                    arr = new ArrayList<String>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        RecipientDetail r_detail=dataSnapshot1.getValue(RecipientDetail.class);
                       r_detail.setBody(r_detail.body);
//                        Log.i("array7712555",""+r_detail);
                        arrayList.add(r_detail);

                    }
//                    Log.i("array7712555",""+arrayList);

                    RecycleviewAdapter rcAdapter = new RecycleviewAdapter(NotificationActivity.this, arrayList);
                    recyclerView.setAdapter(rcAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }

}
