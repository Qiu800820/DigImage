package com.pocoin.digimage.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pocoin.digimage.R;
import com.pocoin.digimage.react.activity.MainRnActivity;
import com.sum.xlog.core.XLog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.skip_tv)
    TextView skip;
    private boolean canSkip = true;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long o) {
                        return 4 - o.intValue();
                    }
                }).take(4 + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        setCountDown(getString(R.string.format_skip, integer));
                    }

                    @Override
                    public void onError(Throwable e) {
                        XLog.e("init", e);
                    }

                    @Override
                    public void onComplete() {
                        skip(true);
                    }
                });
    }


    public void gotoHomePage(boolean finish) {
        startActivity(MainRnActivity.getMainRnIntent(this));

        if(finish){
            finish();
        }
    }



    public void skip(boolean finish) {
        if (!canSkip)
            return;
        canSkip = false;
        gotoHomePage(finish);
    }



    public void setCountDown(String countDown) {
        skip.setText(countDown);
    }

    @OnClick({R.id.skip_tv})
    public void skip(View view){
        skip(true);
    }


    public Context getActivityContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null && disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
