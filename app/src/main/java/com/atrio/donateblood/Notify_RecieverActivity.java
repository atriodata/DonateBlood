package com.atrio.donateblood;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import dmax.dialog.SpotsDialog;

public class Notify_RecieverActivity extends AppCompatActivity {
    String body;
    TextView tv_body;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify__reciever);
        tv_body = (TextView)findViewById(R.id.tv_confm);
        dialog = new SpotsDialog(Notify_RecieverActivity.this, R.style.Custom);
        dialog.show();
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (getIntent().getExtras() != null) {
                body= getIntent().getExtras().getString("body");
                dialog.dismiss();
                tv_body.setText(body);
            }
        }


    }
}
