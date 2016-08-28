package com.tencent.qchat.activity;

import android.app.Application;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.qchat.constant.Config;

/**
 * Created by hiwang on 16/8/28.
 */
public class App extends Application {

    //微信
    public static IWXAPI mWxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mWxApi = WXAPIFactory.createWXAPI(getApplicationContext(), Config.WECHAT_APP_ID, true);
        mWxApi.registerApp(Config.WECHAT_APP_ID);
    }
}
