package com.pocoin.basemvp.presentation.lce;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.pocoin.basemvp.R;


/**
 * Created by Robert on 2016/11/24.
 */
public class ToolbarManager {

    private Toolbar toolBar;


    public static ToolbarManager with(AppCompatActivity appcompatActivity){
        inflateByViewStub(appcompatActivity);
        Toolbar toolbar = (Toolbar) appcompatActivity.findViewById(R.id.tool_bar);
        setUpToolBar(appcompatActivity, toolbar);
        return new ToolbarManager(toolbar);
    }

    private static void inflateByViewStub(AppCompatActivity appCompatActivity){
        try {
            ViewStub view = (ViewStub) appCompatActivity.findViewById(R.id.view_stub_title);
            view.inflate();
        }catch (Throwable e){
            // ignore
        }
    }

    private static void setUpToolBar(AppCompatActivity appCompatActivity, Toolbar toolbar) {
        toolbar.setTitle("");
        appCompatActivity.setSupportActionBar(toolbar);
    }

    private ToolbarManager(Toolbar toolBar) {
        if (null == toolBar){
            throw new IllegalArgumentException("You must set toolbar instance");
        }
        this.toolBar = toolBar;
    }

    public ToolbarManager title(String s) {
        TextView textView = (TextView) toolBar.findViewById(R.id.title_content_tv);
        if(textView != null){
            textView.setText(s);
        }else{
            toolBar.setTitle(s);
        }
        return this;
    }

    public ToolbarManager setNavigationIcon(int res, View.OnClickListener onClickListener){
        toolBar.setNavigationIcon(res);
        toolBar.setNavigationOnClickListener(onClickListener);
        return this;
    }
}
