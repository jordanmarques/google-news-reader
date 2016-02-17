package com.jojo.googlenewsreader.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.brodcastReceiver.NetworkChangeReceiver;
import com.jojo.googlenewsreader.utils.NetworkUtil;


public class ParentActivity extends Activity {
    private static TextView label;
    private BroadcastReceiver networkChangeReceive;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_title_bar);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title_bar);

        label = (TextView) findViewById(R.id.networkLabel);

        if(NetworkUtil.getConnectivityStatusBoolean(this)){
            label.setVisibility(View.INVISIBLE);
        } else {
            label.setVisibility(View.VISIBLE);
        }

        networkChangeReceive = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceive, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Parent", "On Resume");
        if(NetworkUtil.getConnectivityStatusBoolean(this)){
            label.setVisibility(View.INVISIBLE);
        } else {
            label.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Parent", "On Start");
        if(NetworkUtil.getConnectivityStatusBoolean(this)){
            label.setVisibility(View.INVISIBLE);
        } else {
            label.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Parent", "On Pause");
        unregisterReceiver(networkChangeReceive);
    }

    public static void onNetworkChange(Boolean state){
        if(null != label){
            if(state){
                label.setVisibility(View.INVISIBLE);
            } else {
                label.setVisibility(View.VISIBLE);
            }
        }
    }
}
