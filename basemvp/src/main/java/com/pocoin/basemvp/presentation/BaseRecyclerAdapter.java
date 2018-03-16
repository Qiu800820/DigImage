package com.pocoin.basemvp.presentation;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder>{

    protected List<T> datas;

    public AdapterItemListener<T> adapterItemListener;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void addDatas(List<T> datas){
        if (this.datas == null) {
            setDatas(datas);
        } else if(datas != null){
            this.datas.addAll(datas);
        }
    }

    public void addDatas(List<T> datas, int index) {
        if (this.datas == null) {
            setDatas(datas);
        } else {
            this.datas.addAll(index, datas);
        }
    }


    public void setAdapterItemListener(AdapterItemListener<T> adapterItemListener) {
        this.adapterItemListener = adapterItemListener;
    }

    public boolean hasItemListener() {
        return adapterItemListener != null;
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        if(datas == null || datas.isEmpty())
            return null;
        return position < datas.size() && position >= 0 ? datas.get(position) : null;
    }


}
