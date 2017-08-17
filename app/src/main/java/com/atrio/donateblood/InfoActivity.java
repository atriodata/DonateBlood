package com.atrio.donateblood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class InfoActivity extends AppCompatActivity {

    WebView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        info = (WebView) findViewById(R.id.info_web);
        info.loadUrl("file:///android_asset/info.html");
    }
}
