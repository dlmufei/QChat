package com.tencent.qchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tencent.qchat.R;
import com.tencent.qchat.utils.LoadViewInterface;
import com.tencent.qchat.utils.ScreenUtil;

import butterknife.ButterKnife;

/**
 * Created by hiwang on 16/8/11.
 * <p/>
 * 上层Activity,所有的Activity都要继承自它
 */

public abstract class BaseActivity extends AppCompatActivity implements LoadViewInterface {

    Context superCtx;
    ViewGroup mRootView;

    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        superCtx = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        int layout = initLayoutRes();
        if (layout == 0)
            throw new NullPointerException("you may forget to set the layout resource id ..." + getLocalClassName());
        onWillLoadView();
        mRootView = (ViewGroup) LayoutInflater.from(this).inflate(layout, null);
        View blank = new View(this);
        blank.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mRootView.addView(blank, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getStatusBarHeight(this)));
        setContentView(mRootView);
        ButterKnife.bind(this);
        onDidLoadView();
    }

    protected void openActivity(Class clz) {
        startActivity(new Intent(this, clz));
    }

    protected void openWebActivity(String url, String title) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.URL, url)
                .putExtra(WebActivity.TITLE, title);
        startActivity(intent);
    }

    protected void setFullScreen() {
        mRootView.removeViewAt(0);
    }

    /**
     * 实现经典的Activity打开动画
     */
    protected void playOpenAnimation() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_clam);
    }

    /**
     * 实现经典的Activity退出动画
     */
    protected void playExitAnimation() {
        overridePendingTransition(R.anim.slide_clam, R.anim.slide_out_right);
    }
}
