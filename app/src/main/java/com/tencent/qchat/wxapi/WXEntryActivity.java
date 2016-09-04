package com.tencent.qchat.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.qchat.R;
import com.tencent.qchat.activity.App;
import com.tencent.qchat.activity.BaseActivity;
import com.tencent.qchat.activity.MainActivity;
import com.tencent.qchat.constant.Config;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.utils.NetworkChecker;
import com.tencent.qchat.utils.QLog;
import com.tencent.qchat.utils.UserUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by hiwang on 16/8/28.
 */
public class WXEntryActivity extends BaseActivity implements IUiListener, IWXAPIEventHandler {


    protected Tencent mTencentClient;
    @BindView(R.id.loading)
    SimpleDraweeView mLoadingView;

    @Override
    public int initLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void onWillLoadView() {

    }

    @Override
    public void onDidLoadView() {
        setFullScreen();
        mTencentClient = Tencent.createInstance(Config.QQ_APP_ID, getApplicationContext());
    }

    public void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        onBackPressed();
        overridePendingTransition(R.anim.slide_clam, R.anim.slide_out_bottom);
    }

    public void wxLogin(View v) {
        if (!App.mWxApi.isWXAppInstalled()) {
            showToast("请先安装微信");
            return;
        }
        if(!NetworkChecker.IsNetworkAvailable(this)){
            showToast("网络异常");
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_login";
        App.mWxApi.sendReq(req);
        UserUtil.setOpenType(this, "wechat");
    }

    public void qqLogin(View v) {
        if(!NetworkChecker.IsNetworkAvailable(this)){
            showToast("网络异常");
            return;
        }
        mLoadingView.setVisibility(View.VISIBLE);
        UserUtil.setOpenType(this, "qq");
        mTencentClient.login(this, "all", this);
    }

    public void visitor(View v) {
        openMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    @Override
    public void onComplete(Object o) {
        JSONObject obj = (JSONObject) o;
        try {
            String openId = obj.getString(Constants.PARAM_OPEN_ID);
            String token = obj.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = obj.getString(Constants.PARAM_EXPIRES_IN);
            mTencentClient.setOpenId(openId);
            mTencentClient.setAccessToken(token, expires);
            UserUtil.setOpenId(this, openId);
            fetchUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void fetchUserInfo() {

        new UserInfo(this, mTencentClient.getQQToken()).getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                saveUserInfo((JSONObject) o);
                loginToSelfServer();
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    protected void loginToSelfServer() {
        RetrofitHelper.getInstance().login(UserUtil.getNickName(this), UserUtil.getAvator(this),
                UserUtil.getOpenType(this), UserUtil.getOpenId(this),
                new Subscriber<JsonObject>() {

                    @Override
                    public void onCompleted() {
                        showToast("登录成功");
                        mLoadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        mLoadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        UserUtil.setToken(WXEntryActivity.this, jsonObject.get("token").getAsString());
                        UserUtil.setIsStaff(WXEntryActivity.this, jsonObject.get("is_staff").getAsString().equals("1") ? true : false);
                        UserUtil.setIsLogin(WXEntryActivity.this, true);
                        openMainActivity();

                    }
                });
    }

    protected void saveUserInfo(JSONObject obj) {
        try {
            UserUtil.setNickName(this, obj.getString("nickname"));
            UserUtil.setAvator(this, obj.getString("figureurl_1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError uiError) {
        showToast(getResources().getString(R.string.error_network));
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_clam, R.anim.slide_out_bottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        App.mWxApi.handleIntent(intent, this);
    }


    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    //发送到微信请求的响应结果将回调到onResp方法
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode != 0) {
            showToast("授权失败", baseResp.errStr);
        } else { // 登录成功,通过code获取access_token
            getAccessTokenByCode(((SendAuth.Resp)baseResp).code);
        }
    }

    protected void getAccessTokenByCode(final String code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Config.WECHAT_APP_ID + "&secret=" + Config.WECHAT_SECRET
                        + "&code=" + code + "&grant_type=authorization_code";
                try {
                    HttpURLConnection conn= (HttpURLConnection) new URL(url).openConnection();
                    conn.setUseCaches(false);
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String result="",line;
                    while ((line=br.readLine())!=null){
                        result+=line;
                    }
                    QLog.d(WXEntryActivity.class,result);
                    JSONObject jo=new JSONObject(result);
                    UserUtil.setOpenId(WXEntryActivity.this,jo.getString("openid"));
                    UserUtil.setToken(WXEntryActivity.this,jo.getString("access_token"));
                    getUserInfoByIDToken(jo.getString("openid"),jo.getString("access_token"));
                } catch (Exception e) {
                    e.printStackTrace();
                    QLog.d(WXEntryActivity.class,"微信登录失败111");
                    showToast("登录失败1",e.getMessage());
                }
            }
        }).start();
    }

    protected void getUserInfoByIDToken(String openid, String access_token) {
        String url="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
        try {
            HttpURLConnection conn= (HttpURLConnection) new URL(url).openConnection();
            conn.setUseCaches(false);
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result="",line;
            while ((line=br.readLine())!=null){
                result+=line;
            }
            QLog.d(WXEntryActivity.class,result);
            JSONObject jo=new JSONObject(result);
            UserUtil.setNickName(WXEntryActivity.this,jo.getString("nickname"));
            UserUtil.setAvator(WXEntryActivity.this,jo.getString("headimgurl"));
            loginToSelfServer();
        } catch (Exception e) {
            e.printStackTrace();
            QLog.d(WXEntryActivity.class,"微信登录失败222");
            showToast("登录失败2",e.getMessage());
        }
    }
}
