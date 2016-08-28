package com.tencent.qchat.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.qchat.activity.App;

/**
 * Created by hiwang on 16/8/28.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.mWxApi.handleIntent(getIntent(),this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        App.mWxApi.handleIntent(intent,this);
        handleIntent(intent);
    }

    protected void handleIntent(Intent intent) {
        System.out.println("intent");
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //用户同意
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        System.out.println("onreq");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        System.out.println("onresp");
    }
}
