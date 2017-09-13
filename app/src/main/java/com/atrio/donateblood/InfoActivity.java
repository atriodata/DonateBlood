package com.atrio.donateblood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import dmax.dialog.SpotsDialog;

public class InfoActivity extends AppCompatActivity {
    private SpotsDialog dialog;

    WebView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        info = (WebView) findViewById(R.id.info_web);
        dialog = new SpotsDialog(InfoActivity.this, R.style.Custom);
        dialog.show();
        try {
            dialog.dismiss();
            info.loadUrl("file:///android_asset/information.html");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
