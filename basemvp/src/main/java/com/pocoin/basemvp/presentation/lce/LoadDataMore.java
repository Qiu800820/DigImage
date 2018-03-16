package com.pocoin.basemvp.presentation.lce;

import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.domain.UseCase;

/**
 * Created by Administrator on 2016/12/9.
 */

public class LoadDataMore implements LoadData{

    private LcePtrUlmView lcePtrUlmView;
    private boolean isLoadSuccess;
    public LoadDataMore(LcePtrUlmView lcePtrUlmView){
        this.lcePtrUlmView = lcePtrUlmView;
    }

    @Override
    public void paramsFail() {
        if(lcePtrUlmView != null)
            lcePtrUlmView.loadMoreComplete(isLoadSuccess);
    }

    @Override
    public void onError(Throwable e) {
        JLog.e(e);
        if(lcePtrUlmView != null){
            lcePtrUlmView.showError(e, true);
        }
    }

    @Override
    public void onNext(Object dataList) {
        if(lcePtrUlmView != null){
            lcePtrUlmView.addData(dataList);
            lcePtrUlmView.showContent();
            isLoadSuccess = true;
        }
    }

    @Override
    public void onFinish() {
        if(lcePtrUlmView != null){
            lcePtrUlmView.loadMoreComplete(isLoadSuccess);
        }
    }

    @Override
    public void addPageParams(UseCase.RequestPageValue requestPageValue) {
        isLoadSuccess = false;
        requestPageValue.setPage(lcePtrUlmView.getPage() + 1);
        requestPageValue.setLimit(lcePtrUlmView.getLimit());
    }
}
