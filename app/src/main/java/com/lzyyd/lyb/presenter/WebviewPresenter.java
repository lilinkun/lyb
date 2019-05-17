package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.TbAllContract;
import com.lzyyd.lyb.contract.WebviewContract;
import com.lzyyd.lyb.entity.OrderBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/27.
 */

public class WebviewPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private WebviewContract webviewContract;

    @Override
    public void onCreate(Context context) {
        this.mContext = context;
        manager = new DataManager(context);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void attachView(IView view) {
        webviewContract = (WebviewContract) view;
    }

    /**
     *
     * @param SessionId
     */
    public void getNewUrl(String Type,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserBase");
        params.put("fun","Html5Url");
        params.put("Type",Type);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getNewUrl(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {

                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        webviewContract.onDataSuccess(o.toString());
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        webviewContract.onDataSuccess(msg);
                    }
                }));
    }
}
