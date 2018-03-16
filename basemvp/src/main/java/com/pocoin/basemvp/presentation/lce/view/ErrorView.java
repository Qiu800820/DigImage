package com.pocoin.basemvp.presentation.lce.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.R;
import com.pocoin.basemvp.presentation.lce.ErrorMessage;


/**
 * Created by Robert yao on 2016/11/9.
 */

public class ErrorView extends LinearLayout{

    private ImageView errorHintIv;
    private TextView errorHintTv;
    private Button errorProcessButton;
    private OnErrorProcessListener onErrorProcessListener;
    private OnLoginErrorProcessListener onLoginErrorProcessListener;
    private LoginReceiver loginReceiver;
    private String action;

    public interface OnErrorProcessListener {
        void onErrorProcessButtonClick();
    }

    public interface OnLoginErrorProcessListener{
        void refresh();
    }

    public ErrorView(Context context) {
        super(context);
        initViews();
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }


    private void initViews() {
        inflate(getContext(), R.layout.layout_error_view, this);
        errorHintIv = (ImageView) findViewById(R.id.error_img);
        errorHintTv = (TextView) findViewById(R.id.error_error);
        errorProcessButton = (Button) findViewById(R.id.error_button);
        errorHintIv.setOnClickListener(listener);
        errorProcessButton.setOnClickListener(listener);
    }

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != onErrorProcessListener){
                onErrorProcessListener.onErrorProcessButtonClick();
            }
        }
    };

    public void with(ErrorMessage errorMessage, final MvpLceView lceView){
        setErrorMessage(errorMessage.getHintText());
        setErrorHintImageViewBackgroundResource(errorMessage.getHintImage());
        setErrorProcessButtonText(errorMessage.getErrorProcessButtonText());
        setErrorProcessButtonVisible(errorMessage.isVisibleButton());

        if(errorMessage instanceof TokenErrorMessage){
            setOnLoginErrorProcessListener(new ErrorView.OnLoginErrorProcessListener() {
                @Override
                public void refresh() {
                    if(lceView != null)
                        lceView.loadData(false);
                }
            }, ((TokenErrorMessage) errorMessage).getLoginAction());
        }
    }

    public void setErrorMessage(String errorMessage){
        errorHintTv.setText(errorMessage);
    }
    public void serErrorHintTvTextSize(float textSize){
        errorHintTv.setTextSize(textSize);
    }
    public void setErrorHintTvTextColor(int color){
        errorHintTv.setTextColor(getContext().getResources().getColor(color));
    }
    public void setErrorHintImageViewBackgroundResource(int  backgroundResource){
        errorHintIv.setImageResource(backgroundResource);
    }
    public void setErrorHintImageViewVisible(boolean isVisible){
        errorHintIv.setVisibility(isVisible ? VISIBLE : GONE);
    }
    public void setErrorProcessButtonText(String buttonText){
        errorProcessButton.setText(buttonText);
    }
    public void setErrorProcessButtonVisible(boolean isVisible){
        errorProcessButton.setVisibility(isVisible ? VISIBLE : GONE);
    }
    public void setErrorProcessButtonBackgroundResource(int backgroundResource){
        errorProcessButton.setBackgroundResource(backgroundResource);
    }
    public void setOnErrorProcessListener(OnErrorProcessListener onErrorProcessListener) {
        this.onErrorProcessListener = onErrorProcessListener;
    }

    public void setOnLoginErrorProcessListener(OnLoginErrorProcessListener onLoginErrorProcessListener, String action) {
        this.onLoginErrorProcessListener = onLoginErrorProcessListener;
        this.action = action;
        initLoginReceiver(action);
    }

    private void initLoginReceiver(String action){
        if(loginReceiver == null) {
            loginReceiver = new LoginReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            getContext().registerReceiver(loginReceiver, filter);
        }
    }

    public class LoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context mContext, Intent intent) {
            JLog.d("接收到广播信息:" + intent.getAction());

            if(loginReceiver != null) {
                getContext().unregisterReceiver(loginReceiver);
                loginReceiver = null;
            }

            if(null != onLoginErrorProcessListener){
                onLoginErrorProcessListener.refresh();
                onLoginErrorProcessListener = null;
            }

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(loginReceiver != null) {
            getContext().unregisterReceiver(loginReceiver);
            loginReceiver = null;
        }
    }
}
