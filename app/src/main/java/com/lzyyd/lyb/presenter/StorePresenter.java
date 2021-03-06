package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzyyd.lyb.contract.StoreContract;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.entity.SelfStoreBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/20.
 */

public class StorePresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private StoreContract storeContract;

    @Override
    public void onCreate(Context context) {
        this.mContext = context;
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(IView view) {
        storeContract = (StoreContract) view;
    }

    public void setData(String StoreId,String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","获取数据中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Goods");
        params.put("fun", "StoreGoodsList");
        params.put("StoreId", StoreId);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getSelfStoreGoodsList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<SelfStoreBean>,Object>(){

                    @Override
                    public void onResponse(ArrayList<SelfStoreBean> selfGoodsBeans, String status,Object page) {
                        storeContract.getDataSuccess(selfGoodsBeans);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        storeContract.getDataFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                })
        );
    }
}
