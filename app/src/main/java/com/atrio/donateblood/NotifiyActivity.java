package com.atrio.donateblood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atrio.donateblood.model.RecipientDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NotifiyActivity extends AppCompatActivity {
    TextView rec_tv;
    Button btn_yes,btn_no;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    String state_data, blood_data, emailid, phoneno, date_req, city_data, other_detail, token_id,msg_id,imsg_id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiy);
        rec_tv=(TextView)findViewById(R.id.tv_detail);
        btn_yes=(Button) findViewById(R.id.btn_no);
        btn_no=(Button) findViewById(R.id.btn_yes);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.i("data88", "Key: " + key + " Value: " + value);

            }
            imsg_id= getIntent().getExtras().getString("msg_id");
                    Log.i("other_detail2",""+imsg_id);

            token_id= getIntent().getExtras().getString("token_id");
            Log.i("other_detail26",""+token_id);

        }
   /*     Intent intent = getIntent();
        imsg_id = intent.getStringExtra("msg_id");
        token_id = intent.getStringExtra("token_id");*/
        Log.i("other_detail2",""+imsg_id);
        Log.i("other_detail24",""+imsg_id);


            db_instance = FirebaseDatabase.getInstance();
            db_ref = db_instance.getReference();


            Query getnotifi=db_ref.child("Notification").child("Recipient").orderByChild("msg_id").equalTo(imsg_id);
            Log.i("other_query",""+getnotifi);


            getnotifi.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() !=0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Log.i("dataif",child.toString());
                            RecipientDetail recipientDetail = child.getValue(RecipientDetail.class);

                            date_req= recipientDetail.getReq_date();
                            emailid= recipientDetail.getEmailid();
                            phoneno=  recipientDetail.getPhoneno();
                            msg_id=  recipientDetail.getMsg_id();
                            other_detail=  recipientDetail.getOther_detail();
                            blood_data=   recipientDetail.getBloodgroup();
                            state_data=   recipientDetail.getState();
                            city_data=  recipientDetail.getCity();

                            String message = "There is requirement of blood group " + blood_data + " in "+city_data+ " on "+date_req+
                                    ".\n\n\nDetails of Recipient:\n\nEmail-Id:"+emailid+"\nPhone No: "+phoneno+"\nOther Details: "+other_detail;

                            rec_tv.setText(message);
//                        dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//        Log.i("other_detail",""+other_detail);



        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }
}
