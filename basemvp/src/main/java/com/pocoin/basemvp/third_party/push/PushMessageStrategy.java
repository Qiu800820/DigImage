package com.pocoin.basemvp.third_party.push;

import android.content.Context;
import android.content.Intent;

import com.pocoin.basemvp.presentation.SimpleViewHolder;


/**
 * Created by Robert yao on 2016/11/9.
 */
public abstract class PushMessageStrategy{

    protected PushMessage pushMessage;

    public static final String ACTION_PUSH_DIALOG = "com.wsd.yjx.push.dialog";

    protected PushMessageStrategy(PushMessage pushMessage) {
        this.pushMessage = pushMessage;
    }

    public abstract String getTitle(Context context);
    public abstract String getContent(Context context);
    public abstract void notifyUi(Context context);
    public abstract void showDialog(Context context);
    public abstract Intent getIntent(String messageScheme);

    //为了兼容不同的消息类型 显示不同的item，3.0以后用这个抽象方法取代原有的 getTitle（） getContent（）
    public abstract int getCustomItemViewId();
    public abstract void bindDataToCustomView(SimpleViewHolder holder);
}
