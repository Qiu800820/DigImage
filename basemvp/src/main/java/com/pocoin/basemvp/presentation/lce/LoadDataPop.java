package com.pocoin.basemvp.presentation.lce;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.pocoin.basemvp.domain.UseCase;

/**
 * Created by Administrator on 2016/12/9.
 */

public class LoadDataPop implements LoadData{

    private MvpLceView mvpLceView;

    public LoadDataPop(MvpLceView mvpLceView) {
        this.mvpLceView = mvpLceView;
    }

    @Override
    public void paramsFail() {

    }

    @Override
    public void onNext(Object dataList) {
        if(mvpLceView != null){
            mvpLceView.setData(dataList);
            mvpLceView.showContent();
        }
    }

    @Override
    public void addPageParams(UseCase.RequestPageValue requestPageValue) {
        if(mvpLceView instanceof LcePtrView)
            requestPageValue.setPage(((LcePtrView) mvpLceView).getFirstPage());
    }

    @Override
    public void onError(Throwable e) {
        if(mvpLceView != null){
            mvpLceView.showError(e, false);
        }
    }

    @Override
    public void onFinish() {

    }
}
