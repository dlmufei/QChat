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
            toggle();
        }
    }

    /**
     * 关闭菜单
     */
    public void close() {
        if (mIsOpened) {
            toggle();
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

    float mLastX = 0, mLastY = 0; //用来记录上一次的触屏位置
    float mOriginX = 0, mOriginY = 0;  //为了判断是点击事件还是滑动事件
    float mRatio = 4;   //  当 x>mRatio*y 的时候才认为用户想滑动菜单


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOriginX = mLastX = ev.getX();
                mOriginY = mLastY = ev.getY();      //  记录Down的位置
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsOpened && ev.getX() > mMenuWidth) {  // 当菜单处于打开状态时,如果用户点击菜单以外的区域就进行拦截,进而在onTouchEvent中执行关闭菜单的效果
                    return true;
                } else if (Math.abs(ev.getX() - mLastX) > Math.abs(ev.getY() - mLastY) * mRatio) {  //  为了解决滑动冲突,首先在这里判断是否符合滑动菜单的要求,然后决定是否拦截该事件
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
            case MotionEvent.ACTION_DOWN:  //  记录Down的位置
                mLastX = ev.getX();
                mLastY = ev.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mLastX) > Math.abs(ev.getY() - mLastY) * mRatio) {  //  根据move滑动菜单
                    scrollBy((int) (mLastX - ev.getX()));
                }
                mLastX = ev.getX();  //  记录上一次触摸时间的位置
                mLastY = ev.getY();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(100);
                float v_x = mVelocityTracker.getXVelocity();
                float v_y = mVelocityTracker.getYVelocity();
                if (v_x > 30 && v_x > v_y * mRatio) {   //  当速度大于20时并且符合滑动要求时进行动态关闭或打开菜单操作
                    smoothScrollTo(0);
                    mIsOpened = true;
                } else if (v_x < -30 && v_x < v_y * mRatio) {  //  同上
                    smoothScrollTo(mMenuWidth);
                    mIsOpened = false;
                } else {
                    int scroll = getScrollX();
                    if (scroll > mMenuWidth / 2.5) {    // 菜单划出距离超过mMenuWidth的2/5时自动关闭菜单
                        smoothScrollTo(mMenuWidth);
                        mIsOpened = false;
                    } else if (scroll < mMenuWidth / 2.5 && scroll != 0) {  // 菜单划出距离小于mMenuWidth的2/5时,并且当前菜单没有打开时,自动打开菜单
                        smoothScrollTo(0);
                        mIsOpened = true;
                    } else if (mIsOpened && ev.getX() > mMenuWidth && (Math.pow(mOriginX - ev.getX(), 2) + Math.pow(mOriginY - ev.getY(), 2)) < 2) {
                        //  菜单处于打开状态时,如果发生菜单外区域的点击事件,就关闭菜单,这个是仿照手机qq的效果
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
