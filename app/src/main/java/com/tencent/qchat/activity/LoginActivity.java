package com.tencent.qchat.activity;

import android.content.Intent;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.qchat.R;
import com.tencent.qchat.constant.Config;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.utils.UserUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by hiwang on 16/8/20.
 */

public class LoginActivity extends BaseActivity implements IUiListener {

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
        mTencentClient = Tencent.createInstance(Config.QQ_APP_ID, getApplicationContext());
    }

    public void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void wxLogin(View v) {
        openMainActivity();
    }

    public void qqLogin(View v) {
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
                        UserUtil.setToken(superCtx,jsonObject.get("token").getAsString());
                        UserUtil.setIsStaff(superCtx,jsonObject.get("is_staff").getAsBoolean());
                        UserUtil.setIsLogin(superCtx,true);
                        openActivity(MainActivity.class);
                        finish();
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
}
