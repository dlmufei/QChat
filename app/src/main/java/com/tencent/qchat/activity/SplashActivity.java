package com.tencent.qchat.activity;

import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.qchat.R;
import com.tencent.qchat.constant.Config;
import com.tencent.qchat.utils.UserUtil;
import com.tencent.qchat.wxapi.WXEntryActivity;

/**
 * Created by hiwang on 16/8/21.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public int initLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {
        setFullScreen();
        Fresco.initialize(getApplicationContext());
        App.mWxApi = WXAPIFactory.createWXAPI(getApplicationContext(), Config.WECHAT_APP_ID, true);
        App.mWxApi.registerApp(Config.WECHAT_APP_ID);
        if (UserUtil.isLogin(this)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, WXEntryActivity.class));
        }
        finish();
    }
}
