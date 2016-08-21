package com.tencent.qchat.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tencent.qchat.R;
import com.tencent.qchat.utils.LoadViewInterface;

import butterknife.ButterKnife;

/**
 * Created by hiwang on 16/8/11.
 * <p>
 * 上层Activity,所有的Activity都要继承自它
 */

public abstract class BaseActivity extends AppCompatActivity implements LoadViewInterface {

    Context superCtx;

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
        setContentView(layout);
        ButterKnife.bind(this);
        onDidLoadView();
    }
    /**
     * 实现经典的Activity打开动画
     */
    protected void playOpenAnimation(){
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_clam);
    }
    /**
     * 实现经典的Activity退出动画
     */
    protected void playExitAnimation(){
        overridePendingTransition(R.anim.slide_clam,R.anim.slide_out_right);
    }
}
