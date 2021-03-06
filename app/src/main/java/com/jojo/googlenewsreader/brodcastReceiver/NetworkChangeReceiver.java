package com.jojo.googlenewsreader.brodcastReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jojo.googlenewsreader.activities.ArticleDetail;
import com.jojo.googlenewsreader.activities.MainActivity;
import com.jojo.googlenewsreader.utils.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Boolean status = NetworkUtil.getConnectivityStatusBoolean(context);
        MainActivity.onNetworkChange(status);
        ArticleDetail.onNetworkChange(status);
    }
}
