package com.pocoin.basemvp.presentation.lce;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.domain.UseCase;

/**
 * Created by Administrator on 2016/12/9.
 */

public class LoadDataRefresh implements LoadData{

    private LcePtrView lcePtrView;


    public LoadDataRefresh(MvpLceView lceView) {
        if(lceView instanceof LcePtrView)
            this.lcePtrView = (LcePtrView) lceView;
    }

    @Override
    public void paramsFail() {
        if(lcePtrView != null)
            lcePtrView.refreshComplete();
    }

    @Override
    public void onError(Throwable e) {
        JLog.e(e);
        if(lcePtrView != null) {
            lcePtrView.showError(e, true);
        }
    }

    @Override
    public void onFinish() {
        if(lcePtrView != null){
            lcePtrView.refreshComplete();
        }
    }

    @Override
    public void onNext(Object dataList) {
        if(lcePtrView != null) {
            lcePtrView.refreshComplete();
            lcePtrView.setData(dataList);
            lcePtrView.showContent();
        }
    }

    @Override
    public void addPageParams(UseCase.RequestPageValue requestPageValue) {
        requestPageValue.setPage(lcePtrView.getFirstPage());
    }
}
