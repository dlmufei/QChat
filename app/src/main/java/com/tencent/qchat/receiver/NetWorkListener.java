package com.tencent.qchat.receiver;

/**
 * 监听网络变化
 *
 * Created by cliffyan on 2016/9/3.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.tencent.qchat.utils.NetworkChecker;

public class NetWorkListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = NetworkChecker.IsNetworkAvailable(context);
            if (!isConnected){
                Toast.makeText(context,"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        }
    }

}