package com.pocoin.basemvp.presentation.lce;

import com.pocoin.basemvp.domain.UseCase;

/**
 * Created by Administrator on 2016/12/9.
 */

public interface LoadData<M> {

    void paramsFail();

    void onError(Throwable e);

    void onNext(M dataList);

    void addPageParams(UseCase.RequestPageValue requestPageValue);

    void onFinish();
}
