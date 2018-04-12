package com.wsd.react.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsd.react.R;
/**
 * Created by Robert yao on 2016/11/9.
 */

public class ErrorView extends LinearLayout{

    private ImageView errorHintIv;
    private TextView errorHintTv;
    private Button errorProcessButton;
    private OnErrorProcessListener onErrorProcessListener;

    public interface OnErrorProcessListener {
        void onErrorProcessButtonClick();
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

    public void with(ErrorMessage errorMessage){
        setErrorMessage(errorMessage.getHintText());
        setErrorHintImageViewBackgroundResource(errorMessage.getHintImage());
        setErrorProcessButtonText(errorMessage.getErrorProcessButtonText());
        setErrorProcessButtonVisible(errorMessage.isVisibleButton());
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
    public interface ErrorMessage {
        boolean isVisibleButton();
        String getHintText();
        String getErrorProcessButtonText();
        @DrawableRes
        int getHintImage();
        Runnable getErrorProcessRunnable();
    }
}
