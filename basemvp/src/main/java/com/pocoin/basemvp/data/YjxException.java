package com.pocoin.basemvp.data;

import android.content.Context;

import com.pocoin.basemvp.presentation.lce.ErrorMessage;


/**
 * Created by Administrator on 2017/7/14.
 */

public abstract class YjxException extends Exception{
    public abstract ErrorMessage getErrorMessage(Context context);
}
