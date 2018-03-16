package com.pocoin.basemvp.presentation.lce;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/12/7.
 */

public class PageManager {

    private int firstPage;
    private int limit;
    private RecyclerView recyclerView;
    private int page;


    private PageManager(int limit, int firstPage, @NonNull RecyclerView recyclerView) {
        this.limit = limit;
        this.firstPage = firstPage;
        this.page = firstPage;
        this.recyclerView = recyclerView;
    }

    public void refresh() {
        this.page = firstPage;
    }

    public void next() {
        this.page++;
    }

    public int getLimit(){
        return limit;
    }

    public int getCurrentPage(){
        return getCurrentPageV2();
    }

    /**
     * v2 需要Presenter加载完成后setPage
     */
    public int getCurrentPageV2(){
        return page;
    }

    /**
     * v1 根据Adapter Count 自动计算Page
     */
    public int getCurrentPageV1(){

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int count = adapter.getItemCount();
        //TODO note:硬编码 为了兼容Paginate 自动添加的Item 目前没有找到更好的办法
        if(adapter.getItemViewType(count - 1) == (Integer.MAX_VALUE - 50))
            count = count -1;

        return (int) Math.ceil(count / (float) limit);
    }

    public static class Builder{
        private int limit = 10;
        private int firstPage = 0;

        private RecyclerView recyclerView;

        public Builder with(RecyclerView recyclerView){
            this.recyclerView = recyclerView;
            return this;
        }

        public Builder setLimit(int limit){
            if(limit > 0)
                this.limit = limit;
            return this;
        }

        public Builder setFirstPage(int firstPage) {
            this.firstPage = firstPage;
            return this;
        }

        public PageManager build(){
            return new PageManager(limit, firstPage, recyclerView);
        }
    }

}
