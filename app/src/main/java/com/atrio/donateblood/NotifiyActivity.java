package com.atrio.donateblood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotifiyActivity extends AppCompatActivity {
    TextView rec_tv;
    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiy);
        rec_tv=(TextView)findViewById(R.id.tv_detail);
        btn_ok=(Button) findViewById(R.id.bt_ok);

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
                ".\n\n\nDetails of Recipient:\n\nEmail-Id:"+email_Id+"\nPhone No: "+phoneNo+"\nOther Details: "+other_detail;

        rec_tv.setText(message);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
