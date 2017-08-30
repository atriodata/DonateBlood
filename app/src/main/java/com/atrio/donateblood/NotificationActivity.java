package com.atrio.donateblood;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;


public class NotificationActivity extends AppCompatActivity {

    ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ListView listView = (ListView) findViewById(R.id.notification_list);
//        adapter = new ListViewAdapter(this, list);
//        listView.setAdapter(adapter);

        IntentFilter intentFilter= new IntentFilter("datamy");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, intentFilter);
        Log.i("EmailId858",""+intentFilter.toString());

    }
    private BroadcastReceiver onNotice= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("EmailId855558",""+intent.toString());

            // Update your RecyclerView here using notifyItemInserted(position);
        }
    };
}
