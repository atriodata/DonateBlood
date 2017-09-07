package com.atrio.donateblood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Notify_RecieverActivity extends AppCompatActivity {
    String body;
    TextView tv_body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify__reciever);
        tv_body = (TextView)findViewById(R.id.tv_confm);

        if (getIntent().getExtras() != null) {
           /* for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.i("data88", "Key: " + key + " Value: " + value);

            }*/
            body= getIntent().getExtras().getString("body");
            Log.i("other_detail2",""+body);
            tv_body.setText(body);



        }
    }
}
