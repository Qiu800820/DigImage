package com.pocoin.basemvp.presentation.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Robert yao on 2016/11/15.
 */

public class CountDownButton extends AppCompatButton {

    public static final int DEFAULT_MILLIS_IN_FUTURE = 60 * 1000;
    public static final int DEFAULT_COUNT_DOWN_INTERVAL = 1000;
    public static final String DEFAULT_INIT_STRING = "获取验证码";
    public static final String DEFAULT_COUNT_DOWNING_STRING = "%d秒后可再次获取";

    private CountDownTimer mCountDownTimer;
    private boolean isCountDowning;
    private Drawable initDrawable;
    private Drawable countDowningDrawable;
    private CountDownStringBuilder countDownStringBuilder;
    private long millisInFuture = DEFAULT_MILLIS_IN_FUTURE;
    private long countDownInterval  = DEFAULT_COUNT_DOWN_INTERVAL;
    private ColorStateList colorStateList;

    public interface CountDownStringBuilder{
        String makeCountDownString(long countDown);
        String getInitString();
    }
    public CountDownButton(Context context) {
        super(context);
        init();
    }
    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        setText(DEFAULT_INIT_STRING);
        mCountDownTimer = new MyCountDownTimer(millisInFuture, countDownInterval);
    }

    public void startCountDown() {
        if (isCountDowning) {
            return;
        }
        forbidClickButton();
        mCountDownTimer.start();
        isCountDowning = true;
    }

    private void forbidClickButton() {
        setEnabled(false);
        setClickable(false);
        colorStateList = getSupportBackgroundTintList();
        setSupportBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        if (null != countDowningDrawable){
            setBackground(countDowningDrawable);
        }
    }

    private void allowClickButton() {
        setEnabled(true);
        setClickable(true);
        setSupportBackgroundTintList(colorStateList);
        if (null != initDrawable){
            setBackground(initDrawable);
        }
    }

    private void stopCountDown(boolean isMyCountDownTimerInvoke) {
        if (isCountDowning) {
            if (!isMyCountDownTimerInvoke) {
                mCountDownTimer.cancel();
            }
            allowClickButton();
            resetInitText();
        }
        isCountDowning = false;
    }

    public void stopCountDown() {
        stopCountDown(false);
    }

    public void destroy(){
        if(mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText(makeCountDownText(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            stopCountDown(true);
        }
    }
    private void resetInitText(){
        if (null != countDownStringBuilder){
            setText(countDownStringBuilder.getInitString());
        }else {
            setText(DEFAULT_INIT_STRING);
        }
    }
    private String makeCountDownText(long millisUntilFinished) {
        long realCount = millisUntilFinished / 1000;
        if (null == countDownStringBuilder){
            return String.format(DEFAULT_COUNT_DOWNING_STRING,realCount);
        }else {
            return countDownStringBuilder.makeCountDownString(realCount);
        }
    }
    public void setCountDownStringBuilder(CountDownStringBuilder countDownStringBuilder) {
        this.countDownStringBuilder = countDownStringBuilder;
        setText(countDownStringBuilder.getInitString());
    }
    public void setInitAndCountDowningDrawable(Drawable initDrawable,Drawable countDowningDrawable) {
        this.initDrawable = initDrawable;
        this.countDowningDrawable = countDowningDrawable;
    }
    public void setMillisInFutureAndCountDownInterval(long millisInFuture ,long countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        reCreateCountDownTimer();
    }
    private void reCreateCountDownTimer() {
        if (!isCountDowning){
            mCountDownTimer = new MyCountDownTimer(millisInFuture, countDownInterval);
        }else {
            throw new RuntimeException("isCountDowning");
        }
    }
}
