package com.atrio.donateblood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class NotifiyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiy);

        Intent intent = getIntent();
        String email_Id = intent.getStringExtra("emailId");
        String phoneNo = intent.getStringExtra("phoneNo");
        String bloodData = intent.getStringExtra("bloodData");
        String cityData = intent.getStringExtra("cityData");
        String dateRequired = intent.getStringExtra("dateRequired");

        String other_detail = intent.getStringExtra("other_detail");

        Log.i("EmailId88",""+email_Id);
        Log.i("phoneNo88",""+phoneNo);
        Log.i("bloodData88",""+bloodData);
        Log.i("cityData88",""+cityData);
        Log.i("other_detail",""+other_detail);

        String message = "There is requirement of blood group " + bloodData + " in "+cityData+ " on "+dateRequired+
                ".Details of Recipient:Email-Id:"+email_Id+"Phone No: "+phoneNo+"\nOther Details: "+other_detail;


    }
}
