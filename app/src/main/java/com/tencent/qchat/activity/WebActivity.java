package com.tencent.qchat.activity;

import android.widget.TextView;

import com.tencent.qchat.R;
import com.tencent.qchat.widget.XWebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hiwang on 16/8/27.
 */
public class WebActivity extends BaseActivity {

    @BindView(R.id.webview)
    XWebView mWebView;

    @BindView(R.id.title)
    TextView mTitleView;

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }

    public static final String URL = "target_url";  //  网页链接
    public static final String TITLE="page_title";  //  页面的标题

    String mUrl;
    String mTitle;

    @Override
    public int initLayoutRes() {
        return R.layout.activity_webview;
    }

    @Override
    public void onWillLoadView() {
        mUrl = getIntent().getStringExtra(URL);
        mTitle=getIntent().getStringExtra(TITLE);
    }

    @Override
    public void onDidLoadView() {
        mTitleView.setText(mTitle);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_clam,R.anim.slide_out_right);
    }
}
