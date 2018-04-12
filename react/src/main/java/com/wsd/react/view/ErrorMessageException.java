package com.wsd.react.view;

import android.content.Context;

/**
 * Created by Sen on 2018/4/10.
 */

public abstract class ErrorMessageException extends Exception{
    public abstract ErrorView.ErrorMessage getErrorMessage(Context context);
}
