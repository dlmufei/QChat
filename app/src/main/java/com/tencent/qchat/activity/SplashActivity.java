package com.tencent.qchat.activity;

import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.qchat.R;

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
        Fresco.initialize(getApplicationContext());
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
