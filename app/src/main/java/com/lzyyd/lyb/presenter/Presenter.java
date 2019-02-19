package com.lzyyd.lyb.presenter;

import android.content.Context;
import android.content.Intent;

import com.lzyyd.lyb.mvp.IView;


/**
 * Created by LG on 2018/1/31.
 */

public interface Presenter {
    void onCreate(Context context);

    void onStart();

    void onStop();

    void attachView(IView view);
}