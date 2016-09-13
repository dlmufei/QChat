package com.tencent.qchat.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

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
        mHandler.sendEmptyMessageDelayed(0,500);
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Fresco.initialize(getApplicationContext());
            App.mWxApi = WXAPIFactory.createWXAPI(getApplicationContext(), Config.WECHAT_APP_ID, true);
            App.mWxApi.registerApp(Config.WECHAT_APP_ID);
            if (UserUtil.isLogin(superCtx)) {
                startActivity(new Intent(superCtx, MainActivity.class));
            } else {
                startActivity(new Intent(superCtx, WXEntryActivity.class));
            }
            onBackPressed();
            overridePendingTransition(R.anim.slide_clam,R.anim.slide_clam);
        }
    };
}
