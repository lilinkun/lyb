package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzyyd.lyb.contract.SelfOrderContract;
import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.entity.SelfOrderBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/18.
 */

public class SelfOrderPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SelfOrderContract selfOrderContract;

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
        selfOrderContract = (SelfOrderContract) view;
    }

    public void getOrderData(String PageIndex,String PageCount,String OrderStatus,String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","获取数据中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Order");
        params.put("fun", "OrderInfoList");
        params.put("PageIndex", PageIndex);
        params.put("PageCount", PageCount);
        params.put("OrderStatus", OrderStatus);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getSelfOrderList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<SelfOrderBean>,Object>(){

                    @Override
                    public void onResponse(ArrayList<SelfOrderBean> selfGoodsBeans, String status) {
                        selfOrderContract.getDataSuccess(selfGoodsBeans);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfOrderContract.getDataFail(msg);
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

    public void exitOrder(String OrderId,String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","取消订单中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Order");
        params.put("fun", "CancleOrder");
        params.put("OrderId", OrderId);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.exitOrder(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>(){

                    @Override
                    public void onResponse(CollectDeleteBean collectDeleteBean, String status) {
                        selfOrderContract.exitOrderSuccess(collectDeleteBean);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfOrderContract.exitOrderFail(msg);
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
