package com.pocoin.basemvp.presentation.lce.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.pocoin.basemvp.R;
import com.pocoin.basemvp.presentation.BaseFragment;
import com.pocoin.basemvp.presentation.lce.LcePresenter;

/**
 * Created by Robert yao on 2016/11/10.
 */
public abstract class BaseLceFragment<M, V extends MvpLceView<M>, P extends LcePresenter<V>> extends BaseFragment<V,P> implements LceViewPlugin.LceViewHandler, MvpLceView<M>{


    private LceViewPlugin lceViewPlugin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lceViewPlugin = new LceViewPlugin(this, this);
        lceViewPlugin.setupBaseView(inflater.inflate(R.layout.fragment_lce_base, container, false));
        return lceViewPlugin.getBaseView();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData(pullToRefresh? LcePresenter.LoadType.LOAD_REFRESH: LcePresenter.LoadType.LOAD_POP);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        lceViewPlugin.showLoading(pullToRefresh);
    }

    @Override
    public void showContent() {
        lceViewPlugin.showContent();
    }

    @Override
    public void
    showError(Throwable e, boolean pullToRefresh) {
        lceViewPlugin.showError(e, pullToRefresh);
    }

    public void setupContentView(View view) {
        lceViewPlugin.setupContentView(view);
    }
}
