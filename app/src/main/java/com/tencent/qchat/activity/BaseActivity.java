package com.tencent.qchat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.qchat.utils.LoadViewInterface;

import butterknife.ButterKnife;

/**
 * Created by hiwang on 16/8/11.
 *
 * 上层Activity,所有的Activity都要继承自它
 */

public abstract class BaseActivity extends AppCompatActivity implements LoadViewInterface{

    Context superCtx;

    public void showToast(String msg){
        if(TextUtils.isEmpty(msg))return;
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        superCtx=this;
        int layout=initLayoutRes();
        if(layout==0) throw new NullPointerException("you may forget to set the layout resource id ..."+getLocalClassName());
        onWillLoadView();
        setContentView(layout);
        ButterKnife.bind(this);
        onDidLoadView();
    }
}
