package com.pocoin.digimage.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.pocoin.basemvp.presentation.BaseActivity;
import com.pocoin.digimage.Injection;
import com.pocoin.digimage.R;
import com.pocoin.digimage.react.activity.MainRnActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity<WelcomeContract.View, WelcomeContract.Presenter> implements WelcomeContract.View {

    @BindView(R.id.skip_tv)
    TextView skip;
    private boolean canSkip = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        getPresenter().init();
        Injection.provideCodePush().updateVersion(getFilesDir().getPath());
    }

    @NonNull
    @Override
    public WelcomeContract.Presenter createPresenter() {
        return new WelcomePresenter(Injection.provideWelcomeUseCase());
    }


    @Override
    public void gotoHomePage(boolean finish) {
        startActivity(MainRnActivity.getMainRnIntent(this));

        if(finish){
            finish();
        }
    }

    @Override
    public void gotoGuidePage(boolean finish) {
        // todo
    }

    @Override
    public void skip(boolean finish) {
        if (!canSkip)
            return;
        canSkip = false;
        if(isShowGuidePage()) {
            gotoGuidePage(finish);
        }else{
            gotoHomePage(finish);
        }
    }

    private boolean isShowGuidePage() {
        return false;//SpUtils.getInstance().getBooleanValue("isShowGuide", true);
    }

    @Override
    public void setCountDown(String countDown) {
        skip.setText(countDown);
    }

    @OnClick({R.id.skip_tv})
    public void skip(View view){
        skip(true);
    }

    @Override
    public Context getActivityContext() {
        return this;
    }
}
