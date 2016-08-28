package com.tencent.qchat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qchat.R;

/**
 * Created by hiwang on 16/8/28.
 */
public class TopHintView extends RelativeLayout {

    protected Context mContext;
    protected TextView mHintView;

    protected int TIME_TO_SHOW=1000;//  从显示到消失的时间

    protected Handler mHandler;

    public TopHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initHandler();
        initHintView(attrs);
    }

    protected void initHandler() {
        mHandler=new Handler(mContext.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                hide();
            }
        };
    }

    protected void initHintView(AttributeSet attrs) {
        mHintView = new TextView(mContext);
        mHintView.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(CENTER_IN_PARENT);
        addView(mHintView, params);
        setVisibility(GONE);
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.TopHintView);
        mHintView.setTextColor(ta.getColor(R.styleable.TopHintView_text_color, Color.WHITE));
        mHintView.setTextSize(ta.getDimensionPixelSize(R.styleable.TopHintView_text_size, 16));
        ta.recycle();
    }

    public void setHintText(String hint) {
        mHintView.setText(hint);
        show();
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0,TIME_TO_SHOW);
    }

    /**
     * 把show和hide封装一下,可以在这里做一些动画
     */
    protected void show(){
//        setTranslationY(0);
        setVisibility(VISIBLE);
    }
    protected void hide(){
//        setTranslationY(-getHeight());
        setVisibility(GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(0);
    }
}
