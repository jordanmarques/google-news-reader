package com.jojo.googlenewsreader.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.brodcastReceiver.NetworkChangeReceiver;


public class ParentActivity extends Activity {

    private BroadcastReceiver networkChangeReceive;
    private IntentFilter filter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_title_bar);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title_bar);

        networkChangeReceive = new NetworkChangeReceiver();
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceive, filter);

        Toast.makeText(ParentActivity.this, "create", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceive, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceive);
    }
}
