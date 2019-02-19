package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzyyd.lyb.contract.LoginContract;
import com.lzyyd.lyb.contract.SelfGoodsContract;
import com.lzyyd.lyb.entity.LoginBean;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/12.
 */

public class SelfGoodsPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SelfGoodsContract selfGoodsContract;

    @Override
    public void onCreate(Context mContext) {
        this.mContext = mContext;
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
        selfGoodsContract = (SelfGoodsContract) view;
    }


    public void getGoodList(String PageIndex,String PageCount,String CatId,String SortField,String SortType,String GoodsName,String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","获取数据中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Goods");
        params.put("fun", "GoodsList");
        params.put("PageIndex", PageIndex);
        params.put("PageCount", PageCount);
        params.put("SortField", SortField);
        params.put("CatId", CatId);
        params.put("SortType",SortType);
        params.put("GoodsName", GoodsName);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getselfGoodList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<SelfGoodsBean>,Object>(){

                    @Override
                    public void onResponse(ArrayList<SelfGoodsBean> selfGoodsBeans, String status) {
                        selfGoodsContract.getDataSuccess(selfGoodsBeans);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfGoodsContract.getDataFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }
}
