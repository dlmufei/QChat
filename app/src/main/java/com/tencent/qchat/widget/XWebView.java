package com.tencent.qchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.tencent.qchat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hiwang on 16/8/27.
 */
public class XWebView extends RelativeLayout {

    protected Context mContext;

    @BindView(R.id.inner_webview)
    WebView mInnerWebView;

    protected static final String USER_AGENT="youliao/1.0.0";

    public XWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    protected void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_xwebview, null);
        addView(view,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);
        mInnerWebView.getSettings().setJavaScriptEnabled(true);
        mInnerWebView.setDrawingCacheEnabled(true);
        mInnerWebView.setWebChromeClient(new WebChromeClient());
        mInnerWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mInnerWebView.getSettings().setUserAgentString(USER_AGENT);
    }

    public void loadUrl(String url) {
        mInnerWebView.loadUrl(url);
    }

}
