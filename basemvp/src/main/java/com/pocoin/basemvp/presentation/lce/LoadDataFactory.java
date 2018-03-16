package com.pocoin.basemvp.presentation.lce;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by Administrator on 2016/12/9.
 */

public class LoadDataFactory {

    public static LoadDataFactory mInstance;
    public static LoadDataFactory getInstance(){
        if(mInstance == null){
            synchronized(LoadDataFactory.class){
                if(mInstance == null)
                    mInstance = new LoadDataFactory();
            }
        }
        return mInstance;
    }
    private LoadDataFactory(){}
    public LoadData getLoadData(LcePresenter.LoadType loadType, MvpLceView view){
        LoadData loadData;
        switch (loadType){
            case LOAD_MORE:
                loadData = new LoadDataMore((LcePtrUlmView) view);
                break;
            case LOAD_REFRESH:
                loadData = new LoadDataRefresh(view);
                break;
            default:
                loadData = new LoadDataPop(view);
                break;
        }
        return loadData;
    }

}
