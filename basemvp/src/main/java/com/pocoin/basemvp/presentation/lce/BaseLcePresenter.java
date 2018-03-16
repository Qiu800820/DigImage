package com.pocoin.basemvp.presentation.lce;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.domain.UseCase;
import com.pocoin.basemvp.presentation.ActivityHintView;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/8.
 */

public abstract class BaseLcePresenter<V extends MvpLceView> extends MvpBasePresenter<V> implements LcePresenter<V> {

    protected UseCase useCase;
    private LoadData loadData;
    private LoadType currentLoadType;

    public BaseLcePresenter(UseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void loadData(LoadType loadType) {
        if (!isViewAttached()){
            return;
        }
        currentLoadType = loadType;
        loadData = LoadDataFactory.getInstance().getLoadData(currentLoadType, getView());

        UseCase.RequestValues requestValues = generateRequestValue(currentLoadType);
        if(requestValues != null) {

            if(requestValues instanceof UseCase.RequestPageValue){
                loadData.addPageParams((UseCase.RequestPageValue) requestValues);
            }

            if (!requestValues.checkInput()) {
                if (getView() instanceof ActivityHintView) {
                    ((ActivityHintView) getView()).showToast(requestValues.getErrorStringRes());
                }
                loadData.paramsFail();
                return;
            }
        }

        getView().showLoading(currentLoadType != LoadType.LOAD_POP);


        useCase.unSubscribe();
        useCase.execute(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                BaseLcePresenter.this.onError(e);
                BaseLcePresenter.this.onFinish();
            }

            @Override
            public void onNext(Object cars) {
                BaseLcePresenter.this.onNext(cars);
                BaseLcePresenter.this.onFinish();
            }
        }, requestValues);
    }

    protected void onNext(Object data){
        if(!isViewAttached())
            return;
        if(loadData != null)
            loadData.onNext(data);
    }

    protected void onError(Throwable e){
        JLog.e(e);
        if(!isViewAttached())
            return;
        if(loadData != null)
            loadData.onError(e);
    }

    protected void onFinish(){
        if(!isViewAttached())
            return;
        if(loadData != null)
            loadData.onFinish();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        useCase.unSubscribe();
    }

    public LoadType getCurrentLoadType(){
        return currentLoadType;
    }

    protected abstract UseCase.RequestValues generateRequestValue(LoadType loadType);
}
