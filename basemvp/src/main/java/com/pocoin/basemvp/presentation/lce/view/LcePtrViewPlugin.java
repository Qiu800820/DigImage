package com.pocoin.basemvp.presentation.lce.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.pocoin.basemvp.util.ReflectUtil;

import java.lang.reflect.Method;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/12/22.
 */

public class LcePtrViewPlugin extends LceViewPlugin implements PtrHandler {

    private PtrFrameLayout ptrFrameLayout;
    protected LcePtrViewHandler lcePtrViewHandler;

    public LcePtrViewPlugin(LcePtrViewHandler lcePtrViewHandler, MvpLceView mvpLceView) {
        super(lcePtrViewHandler, mvpLceView);
        this.lcePtrViewHandler = lcePtrViewHandler;
    }

    @Override
    public void setupBaseView(View view) {
        super.setupBaseView(view);
        ptrFrameLayout = new PtrFrameLayout(baseView.getContext());
        contentView.addView(ptrFrameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ptrFrameLayout.addView(lcePtrViewHandler.provideContentSubView(), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lcePtrViewHandler.setupPtrHeaderAndHandler(ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(this);
        try {
            Method method = ReflectUtil.findMethod(ptrFrameLayout, "onFinishInflate");
            method.invoke(ptrFrameLayout);
        }catch (Throwable e){
            throw new NullPointerException(e.getMessage());
        }
    }
    @Deprecated
    @Override
    public void setupContentView(View view) {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return lcePtrViewHandler.checkCanDoRefresh(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mvpLceView.loadData(true);
    }

    public void refreshComplete() {
        ptrFrameLayout.refreshComplete();
    }

    public boolean isRefreshing() {
        return ptrFrameLayout.isRefreshing();
    }

    public interface LcePtrViewHandler extends LceViewHandler{
        View provideContentSubView();
        void setupPtrHeaderAndHandler(PtrFrameLayout ptrFrameLayout);
        boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header);
    }
}
