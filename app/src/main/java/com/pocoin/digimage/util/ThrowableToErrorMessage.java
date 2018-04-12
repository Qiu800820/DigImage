package com.pocoin.digimage.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pocoin.digimage.R;
import com.wsd.react.view.ErrorMessageException;
import com.wsd.react.view.ErrorView;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Robert yao on 2016/11/17.
 */

public class ThrowableToErrorMessage {
    @NonNull
    public static ErrorView.ErrorMessage toErrorMessage(Throwable t, Context context){
        if (t instanceof TimeoutException || t instanceof SocketException || t instanceof SocketTimeoutException ||
                t instanceof IOException){
            return new NoNetWorkErrorMessage(context);
        }
        if (t instanceof ErrorMessageException){
            return ((ErrorMessageException) t).getErrorMessage(context);
        }

        return new UnKnownErrorMessage(context, t.getMessage());
    }

    private static class UnKnownErrorMessage implements ErrorView.ErrorMessage {

        private Context context;
        private String error;

        private UnKnownErrorMessage(Context context, String error) {
            this.context = context;
            this.error = error;
        }

        @Override
        public String getHintText() {
            return error;
        }

        @Override
        public boolean isVisibleButton() {
            return true;
        }


        @Override
        public String getErrorProcessButtonText() {
            return context.getString(R.string.report_error);
        }

        @Override
        public int getHintImage() {
            return R.mipmap.ic_launcher;
        }

        @Override
        public Runnable getErrorProcessRunnable() {
            return new Runnable() {
                @Override
                public void run() {
                    if(context instanceof Activity){
                        ((Activity) context).finish();
                    }
                }
            };
        }
    }

    private static class NoNetWorkErrorMessage implements ErrorView.ErrorMessage {

        private Context context;

        private NoNetWorkErrorMessage(Context context) {
            this.context = context;
        }

        @Override
        public String getHintText() {
            return context.getString(R.string.connect_fail_toast);
        }

        @Override
        public boolean isVisibleButton() {
            return true;
        }


        @Override
        public String getErrorProcessButtonText() {
            return context.getString(R.string.refresh);
        }

        @Override
        public int getHintImage() {
            return R.mipmap.ic_launcher;
        }

        @Override
        public Runnable getErrorProcessRunnable() {
            return null;
        }
    }

}
