package com.atrio.donateblood;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dmax.dialog.SpotsDialog;

public class Notify_RecieverActivity extends AppCompatActivity {
    String body,call_no;
    TextView tv_body;
    Button btn_call;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify__reciever);
        tv_body = (TextView)findViewById(R.id.tv_confm);
        btn_call = (Button) findViewById(R.id.btn_call);
        dialog = new SpotsDialog(Notify_RecieverActivity.this, R.style.Custom);
        dialog.show();
        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.i("data88", "Key: " + key + " Value: " + value);
            }
            body= getIntent().getExtras().getString("body");
            dialog.dismiss();
            tv_body.setText(body);
            call_no=body.substring(body.lastIndexOf("+"),body.length());

            Log.i("call_no",call_no);
        }

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + call_no));
                Log.i("tittlec44", "" + call_no);

                if (ActivityCompat.checkSelfPermission(Notify_RecieverActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

            }
        });
    }
}
