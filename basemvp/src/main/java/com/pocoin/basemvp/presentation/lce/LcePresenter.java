package com.pocoin.basemvp.presentation.lce;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface LcePresenter<V extends MvpLceView> extends MvpPresenter<V>{

    enum LoadType{
        LOAD_MORE, LOAD_REFRESH, LOAD_POP
    }

    void loadData(LoadType loadType);

}
