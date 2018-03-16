package com.wsd.react.view;

import android.content.Context;

import com.pocoin.basemvp.data.YjxException;
import com.pocoin.basemvp.presentation.lce.ErrorMessage;
import com.wsd.react.R;

/**
 * Created by Sen on 2017/11/14.
 */

public class ReactLoadException extends YjxException {

    @Override
    public ErrorMessage getErrorMessage(final Context context) {
        return new ErrorMessage() {
            @Override
            public boolean isForceShowErrorView() {
                return true;
            }

            @Override
            public boolean isVisibleButton() {
                return true;
            }

            @Override
            public String getHintText() {
                return context.getString(R.string.react_load_fail);
            }

            @Override
            public String getErrorProcessButtonText() {
                return context.getString(R.string.react_retry);
            }

            @Override
            public int getHintImage() {
                return R.mipmap.react_error_image;
            }

            @Override
            public Runnable getErrorProcessRunnable() {
                return null;
            }

            @Override
            public Runnable getLceErrorProcessRunnable() {
                return null;
            }
        };
    }
}
