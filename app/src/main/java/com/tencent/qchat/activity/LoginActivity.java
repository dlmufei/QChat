package com.tencent.qchat.activity;

import android.content.Intent;
import android.view.View;

import com.tencent.qchat.R;

/**
 * Created by hiwang on 16/8/20.
 */

public class LoginActivity extends BaseActivity {

    @Override
    public int initLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {

    }

    public void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void wxLogin(View v) {
        openMainActivity();
    }

    public void qqLogin(View v) {
        openMainActivity();
    }
}
