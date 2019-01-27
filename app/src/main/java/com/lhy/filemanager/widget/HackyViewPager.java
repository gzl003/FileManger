package com.lhy.filemanager.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ScaleGestureDetector seems to mess up the touch events, which means that ViewGroups which make use of
 * onInterceptTouchEvent throw a lot of IllegalArgumentException: pointerIndex out of range.There's not much I can do
 * in my code for now, but we can mask the result by just catching the problem and ignoring
 * it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

    private boolean mScrollEnable = true;

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否禁止滚动
     *
     * @param enable false 是不可以滚动  true 是可以滚动
     */
    public void setScrollEnable(boolean enable) {
        mScrollEnable = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScrollEnable) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollEnable) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
