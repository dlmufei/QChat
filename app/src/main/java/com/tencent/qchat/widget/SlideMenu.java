package com.tencent.qchat.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.tencent.qchat.R;
import com.tencent.qchat.utils.ScreenUtil;

/**
 * Created by hiwang on 16/8/17.
 * <p>
 * 侧滑菜单
 */

public class SlideMenu extends RelativeLayout {


    private static final String TAG = "SlideMenu";
    private static final int DEFAULT_PADDING = 100;   //  默认菜单右边距 dp

    private Context mContext;
    private int mScreenWidth;   //  屏幕宽度 px
    private int mMenuWidth;     //  菜单宽度 px
    private int mRightPadding;  //  菜单完全展开时,菜单右侧举例屏幕右侧的距离 px

    private boolean mFirstMeasure = true;

    private boolean mIsOpened = false;

    private VelocityTracker mVelocityTracker;
    private ValueAnimator mAnimator;

    /**
     * 打开菜单
     */
    public void open() {
        if (!mIsOpened) {
            smoothScrollTo(0);
            mIsOpened = true;
        }
    }

    /**
     * 关闭菜单
     */
    public void close() {
        if (mIsOpened) {
            smoothScrollTo(mMenuWidth);
            mIsOpened = false;
        }
    }

    /**
     * 切换开关状态
     */
    public void toggle() {
        if (mIsOpened) {
            smoothScrollTo(mMenuWidth);
            mIsOpened = false;
        } else {
            smoothScrollTo(0);
            mIsOpened = true;
        }
    }

    /**
     * 判断开关状态
     */
    public boolean isOpened() {
        return mIsOpened;
    }

    public void init(AttributeSet attrs) {
        setHorizontalScrollBarEnabled(false);
        mScreenWidth = ScreenUtil.getScreenWidth(mContext);
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.SlideMenu);
        mRightPadding = ta.getDimensionPixelSize(R.styleable.SlideMenu_slide_right_padding,
                ScreenUtil.dp2px(mContext, DEFAULT_PADDING));
        if (mRightPadding < 0 || mRightPadding > mScreenWidth) {
            mRightPadding = ScreenUtil.dp2px(mContext, DEFAULT_PADDING);
        }
        mMenuWidth = mScreenWidth - mRightPadding;
        ta.recycle();
        mVelocityTracker = VelocityTracker.obtain();
        mAnimator = new ValueAnimator();
        mAnimator.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mFirstMeasure) {
            if (getChildCount() != 2) {
                throw new IllegalStateException("SlideMenu should have and only have two children...");
            }
            ViewGroup menu = (ViewGroup) getChildAt(0);
            ViewGroup content = (ViewGroup) getChildAt(1);
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;
            ((LayoutParams) content.getLayoutParams()).addRule(ALIGN_PARENT_RIGHT);
            setMinimumWidth(mMenuWidth + mScreenWidth);
            getLayoutParams().width = mMenuWidth + mScreenWidth;
            mFirstMeasure = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int bar = ScreenUtil.getStatusBarHeight(mContext);
                if (bar <= 0) bar = ScreenUtil.dp2px(mContext, 25);
                menu.setPadding(0, bar, 0, 0);
                content.setPadding(0, bar, 0, 0);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mFirstMeasure = true;
            scrollTo(mMenuWidth, 0);
            mIsOpened = false;
        }
    }

    float mLastX = 0, mLastY = 0;
    float mOriginX = 0, mOriginY = 0;
    float mRatio = 4;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOriginX = mLastX = ev.getX();
                mOriginY = mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsOpened && ev.getX() > mMenuWidth) {
                    return true;
                } else if (Math.abs(ev.getX() - mLastX) > Math.abs(ev.getY() - mLastY) * mRatio) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getX();
                mLastY = ev.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mLastX) > Math.abs(ev.getY() - mLastY) * mRatio) {
                    scrollBy((int) (mLastX - ev.getX()));
                }
                mLastX = ev.getX();
                mLastY = ev.getY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(100);
                float v_x = mVelocityTracker.getXVelocity();
                float v_y = mVelocityTracker.getYVelocity();
                if (v_x > 20 && v_x > v_y * mRatio) {
                    smoothScrollTo(0);
                    mIsOpened = true;
                } else if (v_x < -20 && v_x < v_y * mRatio) {
                    smoothScrollTo(mMenuWidth);
                    mIsOpened = false;
                } else {
                    int scroll = getScrollX();
                    if (scroll > mMenuWidth / 2.5) {
                        smoothScrollTo(mMenuWidth);
                        mIsOpened = false;
                    } else if (scroll < mMenuWidth / 2.5 && scroll != 0) {
                        smoothScrollTo(0);
                        mIsOpened = true;
                    } else if (ev.getX() > mMenuWidth && (Math.pow(mOriginX - ev.getX(), 2) + Math.pow(mOriginY - ev.getY(), 2)) < 2) {
                        close();
                        return true;
                    } else {
                        return super.onTouchEvent(ev);
                    }
                }
                return true;

        }
        return super.onTouchEvent(ev);
    }

    public void smoothScrollTo(int pos) {
        smoothScrollBy(pos - getScrollX());
    }

    public void scrollTo(int pos) {
        if (pos >= 0 && pos <= mMenuWidth) {
            scrollTo(pos, 0);
        }
    }

    public void smoothScrollBy(int pos) {
        int src = getScrollX();
        final int dest = src + pos;
        if (dest < 0 || dest > mMenuWidth) return;
        mAnimator.setIntValues(src, dest);
        mAnimator.setDuration((long) Math.abs(pos * 200 / mMenuWidth));
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedFraction() < 1) {
                    scrollTo((int) animation.getAnimatedValue(), 0);
                } else {
                    animation.removeUpdateListener(this);
                    scrollTo(dest, 0);
                }
            }
        });
        mAnimator.start();
    }

    public void scrollBy(int pos) {
        int curr = pos + getScrollX();
        if (curr <= mMenuWidth && curr >= 0) {
            scrollBy(pos, 0);
        }
    }


    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
        }
    }

}
