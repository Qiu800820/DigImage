package com.pocoin.basemvp.presentation.widgets.badge;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.layout.MvpFrameLayout;
import com.pocoin.basemvp.R;

/**
 * Created by Administrator on 2017/5/12.
 */

public abstract class BaseBadgeLayout extends MvpFrameLayout<BadgeContract.View, BadgeContract.Presenter> implements BadgeContract.View{



    private View view;
    private int height;
    private BadgeChangeListener badgeChangeListener;

    public BaseBadgeLayout(Context context) {
        this(context, null);
    }

    public BaseBadgeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseBadgeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseBadgeLayout, defStyleAttr, 0);
        height = (int) a.getDimension(R.styleable.BaseBadgeLayout_height, 10);
        a.recycle();
        init();
    }

    public void setBadgeChangeListener(BadgeChangeListener badgeChangeListener) {
        this.badgeChangeListener = badgeChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void init(){
        view = new View(getContext());
        LayoutParams layoutParams = new LayoutParams(height, height);
        layoutParams.gravity = Gravity.RIGHT;
        view.setLayoutParams(layoutParams);
        view.setBackgroundResource(R.drawable.oval_red_solid);
        view.setVisibility(View.GONE);
        addView(view);
    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }

    @Override
    public void setVisible(boolean isVisible) {
        if(view != null)
            view.setVisibility(isVisible? View.VISIBLE: View.GONE);
        if(badgeChangeListener != null)
            badgeChangeListener.onChangeListener(isVisible);
    }

    public boolean hasBadge(){
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public interface BadgeChangeListener{
        void onChangeListener(boolean isVisible);
    }
}
