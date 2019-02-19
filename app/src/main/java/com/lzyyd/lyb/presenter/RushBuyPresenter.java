package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.RushBuyContract;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.entity.RushBuyBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/1/2.
 */

public class RushBuyPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RushBuyContract rushBuyContract;

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
    public void onStop()  {
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void attachView(IView view) {
        rushBuyContract = (RushBuyContract) view;
    }

    public void getRushBuyData(String PageIndex,String PageCount,String GoodsType,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Goods");
        params.put("fun", "LimitedActivityGoods_List");
        params.put("PageIndex", PageIndex);
        params.put("PageCount", PageCount);
        params.put("GoodsType", GoodsType);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.rushBuy(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<RushBuyBean>,Object>(){

                    @Override
                    public void onResponse(ArrayList<RushBuyBean> o, String status) {
                        rushBuyContract.getDataSuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        rushBuyContract.getDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<RushBuyBean>,Object> o) {
                        super.onNext(o);
                    }

                })
        );
    }

    public void getData(String PageIndex,String PageCount,String GoodsType,String SessionId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Goods");
        params.put("fun", "aheadActivityGoods_List");
        params.put("PageIndex", PageIndex);
        params.put("PageCount", PageCount);
        params.put("GoodsType", GoodsType);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.rushBuy(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<RushBuyBean>, Object>() {

                    @Override
                    public void onResponse(ArrayList<RushBuyBean> o, String status) {
                        rushBuyContract.getDataSuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        rushBuyContract.getDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<RushBuyBean>, Object> o) {
                        super.onNext(o);
                    }

                })
        );
    }
}
