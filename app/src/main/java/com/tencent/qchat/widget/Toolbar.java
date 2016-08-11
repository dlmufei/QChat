package com.tencent.qchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qchat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MAC on 15/12/1.
 * 自定义Toolbar代替ActionBar
 */
public class Toolbar extends RelativeLayout {

    @BindView(R.id.back)
    ImageView backView;
    @BindView(R.id.more)
    ImageView moreView;
    @BindView(R.id.title_left)
    TextView titleLeftView;//这里的R.id.title_left和R.id.title_center分别指代"紧邻返回箭头右侧的文字"和"ActionBar中间的文字"
    @BindView(R.id.title_center)
    TextView titleCenterView;
    ToolBarListener listener;
    @BindView(R.id.title_right)
    TextView titleRightView;

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_toolbar, null);
        addView(view, lp);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back)
    public void to_back() {
        if (listener != null)
            listener.onBackClicked();
    }

    @OnClick(R.id.more)
    public void to_more() {
        if (listener != null)
            listener.onMoreClicked();
    }

    @OnClick(R.id.title_left)
    public void to_left_title() {
        if (listener != null) {
            listener.onTitleLeftClicked();
        }
    }

    @OnClick(R.id.title_right)
    public void to_right_title() {
        if (listener != null) {
            listener.onTitleRightClicked();
        }
    }

    public interface ToolBarListener {

        void onBackClicked();

        void onMoreClicked();

        void onTitleLeftClicked();

        void onTitleRightClicked();

        void onInit(ImageView back, TextView titleLeft, TextView titleCenter, TextView titleRight, ImageView more);
    }

    public void setToolBarListener(ToolBarListener listener) {
        this.listener = listener;
        listener.onInit(backView, titleLeftView, titleCenterView, titleRightView, moreView);
    }

}
