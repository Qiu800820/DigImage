package com.wsd.react.view;

import android.content.Context;

import com.wsd.react.R;

/**
 * Created by Sen on 2017/11/14.
 */

public class ReactLoadException extends ErrorMessageException {

    @Override
    public ErrorView.ErrorMessage getErrorMessage(final Context context) {
        return new ErrorView.ErrorMessage() {

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

        };
    }
}
