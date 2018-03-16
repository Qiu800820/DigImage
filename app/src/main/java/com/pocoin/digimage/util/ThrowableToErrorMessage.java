package com.pocoin.digimage.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.pocoin.basemvp.data.YjxException;
import com.pocoin.basemvp.presentation.lce.ErrorMessage;
import com.pocoin.digimage.R;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Robert yao on 2016/11/17.
 */

public class ThrowableToErrorMessage {
    @NonNull
    public static ErrorMessage toErrorMessage(Throwable t, Context context){
        if (t instanceof HttpException){
            return new ServiceErrorMessage(context, ((HttpException) t).message());
        }
        if (t instanceof TimeoutException || t instanceof SocketException || t instanceof SocketTimeoutException ||
                t instanceof IOException){
            return new NoNetWorkErrorMessage(context);
        }
        if (t instanceof YjxException){
            return ((YjxException) t).getErrorMessage(context);
        }

        return new UnKnownErrorMessage(context, t.getMessage());
    }

    private static class UnKnownErrorMessage implements ErrorMessage{

        private Context context;
        private String error;

        private UnKnownErrorMessage(Context context, String error) {
            this.context = context;
            this.error = error;
        }

        @Override
        public boolean isForceShowErrorView() {
            return false;
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
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            };
        }

        @Override
        public Runnable getLceErrorProcessRunnable() {
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

    private static class NoNetWorkErrorMessage implements ErrorMessage{

        private Context context;

        private NoNetWorkErrorMessage(Context context) {
            this.context = context;
        }

        @Override
        public String getHintText() {
            return context.getString(R.string.connect_fail_toast);
        }

        @Override
        public boolean isForceShowErrorView() {
            return false;
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
            return new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, context.getString(R.string.connect_fail_toast), Toast.LENGTH_LONG).show();
                }
            };
        }

        @Override
        public Runnable getLceErrorProcessRunnable() {
            return null;
        }
    }

    private static class ServiceErrorMessage implements ErrorMessage {

        private Context context;
        private String error;
        private Runnable runnable;

        private ServiceErrorMessage(Context context, String error) {
            this(context, error, null);
        }

        private ServiceErrorMessage(Context context, String error, Runnable runnable) {
            this.context = context;
            this.error = error;
            this.runnable = runnable;
        }

        @Override
        public String getHintText() {
            return error;
        }

        @Override
        public boolean isForceShowErrorView() {
            return false;
        }

        @Override
        public String getErrorProcessButtonText() {
            return context.getString(R.string.retry);
        }

        @Override
        public boolean isVisibleButton() {
            return true;
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

                    if(runnable !=  null){
                        runnable.run();
                    }else{
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    }
                }
            };
        }

        @Override
        public Runnable getLceErrorProcessRunnable() {
            return null;
        }
    }

}
