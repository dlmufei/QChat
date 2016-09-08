package com.tencent.qchat.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qchat.R;
import com.tencent.qchat.utils.ScreenUtil;
import com.tencent.qchat.utils.TimeUtil;

/**
 * Created by hiwang on 16/8/23.
 */

public class RefreshLayout extends LinearLayout {

    protected Context mContext;
    protected OnRefreshListener mRefreshListener;
    protected boolean mRefreshable = true;

    protected RecyclerView mRecyclerView;
    protected LinearLayout mHeaderView;
    protected RelativeLayout mFooterView;

    protected TextView mHeaderTextView;
    protected ImageView mHeaderArrowView;
    protected TextView mLastTimeView;
    protected String mLastTimeStr = null;

    protected TextView mFooterTextView;
    protected ImageView mFooterArrowView;

    protected static final String HEADER_IDLE = "下拉刷新";  //  空闲状态
    protected static final String HEADER_READY = "松开刷新"; //  刷新状态就绪
    protected static final String HEADER_REFRESHING = "正在刷新";    //  正在刷新
    protected static final String HEADER_REFRESHED = "刷新成功";    //  刷新完成

    protected static final String FOOTER_IDLE = "上拉加载更多";  //  空闲状态
    protected static final String FOOTER_READY = "松开加载更多"; //  上拉加载更多状态就绪
    protected static final String FOOTER_REFRESHING = "正在加载更多";    //  正在加载
    protected static final String FOOTER_REFRESHED = "加载更多成功";    //  加载完成

    protected int mListHeight = 0;
    protected double mRate = 0.4;
    protected int DISTANCE_BEGIN_REFRESH = 100;

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    protected void init() {
        setOrientation(VERTICAL);
        DISTANCE_BEGIN_REFRESH = ScreenUtil.dp2px(mContext, 70);
        mAnimator = new ValueAnimator();
        mAnimator.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!(getChildCount() == 1 && getChildAt(0) instanceof RecyclerView))
            throw new IllegalArgumentException("RefreshLayout can have and only have one child,which is an implements of RecyclerView");
        mRecyclerView = (RecyclerView) getChildAt(0);
        LayoutParams params = new LayoutParams(0, 0);
        mHeaderView = createHeaderView();
        addView(mHeaderView, 0, params);
        mFooterView = createFooterView();
        addView(mFooterView, 2, params);
        mHeaderTextView = (TextView) mHeaderView.findViewById(R.id.header_text);
        mHeaderArrowView = (ImageView) mHeaderView.findViewById(R.id.arrow);
        mLastTimeView = (TextView) mHeaderView.findViewById(R.id.last_time);
        mFooterTextView = (TextView) mFooterView.findViewById(R.id.footer_text);
        mFooterArrowView = (ImageView) mFooterView.findViewById(R.id.arrow);
    }

    protected float mLastX, mLastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mRefreshable) return super.onInterceptTouchEvent(ev);
        float currX = ev.getX();
        float currY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = currX;
                mLastY = currY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (currY > mLastY && canDown()) {
                    return true;
                } else if (currY < mLastY && canUp()) {
                    return true;
                }
                mLastX = currX;
                mLastY = currY;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float currX = ev.getX();
        float currY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = currX;
                mLastY = currY;
                break;
            case MotionEvent.ACTION_MOVE:
                scrollBy((int) (-(currY - mLastY) * mRate));
                updateHeader();
                updateFooter();
                mLastX = currX;
                mLastY = currY;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getScrollY() < mListHeight - DISTANCE_BEGIN_REFRESH) {    //  达到了下拉刷新的范围
                    smoothScrollTo(mListHeight - DISTANCE_BEGIN_REFRESH);
                    mHeaderTextView.setText(HEADER_REFRESHING);
                    mHeaderArrowView.setImageResource(R.mipmap.refresh_layout_loading);
//                    mStartTime = System.currentTimeMillis();
                    if (mRefreshListener != null) {
                        mRefreshListener.onRefreshDown();
                    }
                    //设置上次刷新时间,并记录此次刷新时间
                    if (mLastTimeStr == null) {
                        mLastTimeView.setVisibility(GONE);
                    } else {
                        mLastTimeView.setText(mLastTimeStr);
                        mLastTimeView.setVisibility(VISIBLE);
                    }
                    mLastTimeStr = "上次刷新  "+TimeUtil.currTimeStr();
                } else if (getScrollY() - mListHeight > DISTANCE_BEGIN_REFRESH) {   //  到达了上拉加载的范围
                    smoothScrollTo(mListHeight + DISTANCE_BEGIN_REFRESH);
                    mFooterTextView.setText(FOOTER_REFRESHING);
                    mFooterArrowView.setImageResource(R.mipmap.refresh_layout_loading);
//                    mStartTime = System.currentTimeMillis();
                    if (mRefreshListener != null) {
                        mRefreshListener.onRefreshUp();
                    }
                } else {  //  未达到刷新的范围,直接重置
                    smoothScrollTo(mListHeight);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    protected void updateHeader() {
        int scrollY = mListHeight - getScrollY();
        if (scrollY < DISTANCE_BEGIN_REFRESH) {
            mHeaderTextView.setText(HEADER_IDLE);
            mHeaderArrowView.setImageResource(R.mipmap.refresh_layout_arrow);
            mHeaderArrowView.setRotation(0);
        } else if (scrollY >= DISTANCE_BEGIN_REFRESH) {
            mHeaderTextView.setText(HEADER_READY);
            mHeaderArrowView.setImageResource(R.mipmap.refresh_layout_arrow);
            mHeaderArrowView.setRotation(180);
        }
    }

    protected void updateFooter() {
        int scrollY = getScrollY() - mListHeight;
        if (scrollY < DISTANCE_BEGIN_REFRESH) {
            mFooterTextView.setText(FOOTER_IDLE);
            mFooterArrowView.setImageResource(R.mipmap.refresh_layout_arrow);
            mFooterArrowView.setRotation(180);
        } else if (scrollY >= DISTANCE_BEGIN_REFRESH) {
            mFooterTextView.setText(FOOTER_READY);
            mFooterArrowView.setImageResource(R.mipmap.refresh_layout_arrow);
            mFooterArrowView.setRotation(0);
        }
    }

    protected boolean canDown() {
        return ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition() == 0;
    }

    protected boolean canUp() {
        return ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                .findLastCompletelyVisibleItemPosition() == mRecyclerView.getAdapter().getItemCount() - 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMinimumHeight(3 * mListHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mListHeight == 0) {
                mListHeight = mRecyclerView.getMeasuredHeight();
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mListHeight);
                mHeaderView.setLayoutParams(params);
                mFooterView.setLayoutParams(params);
                mRecyclerView.setLayoutParams(params);
                scrollTo(mListHeight);
            }
        }
    }

    protected LinearLayout createHeaderView() {
        return (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.widget_refresh_layout_header, null);
    }

    protected RelativeLayout createFooterView() {
        return (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.widget_refresh_layout_footer, null);
    }

//    protected long mStartTime;    //  记录开始刷新的时间,方式刷新过快影响体验,把刷新时间控制在500毫秒以外
//    protected long mMinRefreshTime = 400; //  最小的刷新时间

    public void refreshDownComplete() {
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator.removeAllUpdateListeners();
        }
        scrollTo(mListHeight);
//        long end = System.currentTimeMillis();
//        mHeaderTextView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mHeaderTextView.setText(HEADER_REFRESHED);
//                if (mListHeight > 0) scrollTo(mListHeight);
//            }
//        }, end - mStartTime < mMinRefreshTime ? mMinRefreshTime - end + mStartTime : 0);
    }

    public void setRefreshable(boolean b) {
        mRefreshable = b;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public interface OnRefreshListener {

        void onRefreshDown();

        void onRefreshUp();
    }

    protected ValueAnimator mAnimator;

    protected void smoothScrollTo(int pos) {
        smoothScrollBy(pos - getScrollY());
    }

    protected void scrollTo(int pos) {
        if (pos >= 0 && pos <= 2 * mListHeight) {
            scrollTo(0, pos);
        }
    }

    protected void smoothScrollBy(int pos) {
        int src = getScrollY();
        final int dest = src + pos;
        if (dest < 0 || dest > 2 * mListHeight) return;
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator.removeAllUpdateListeners();
        }
        mAnimator.setIntValues(src, dest);
        mAnimator.setDuration((long) Math.abs(pos * 500 / mListHeight));
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedFraction() < 1) {
                    scrollTo(0, (Integer) animation.getAnimatedValue());
                } else {
                    animation.removeUpdateListener(this);
                    scrollTo(0, dest);
                }
            }
        });
        mAnimator.start();
    }

    protected void scrollBy(int pos) {
        int curr = pos + getScrollY();
        if (curr <= mListHeight * 2 && curr >= 0) {
            scrollBy(0, pos);
        }
    }

}
