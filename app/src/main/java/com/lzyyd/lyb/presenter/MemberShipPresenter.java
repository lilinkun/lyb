package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.MemberShipContract;
import com.lzyyd.lyb.entity.PageBean;
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
 * Created by LG on 2018/12/27.
 */

public class MemberShipPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private MemberShipContract memberShipContract;

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
        memberShipContract = (MemberShipContract) view;
    }

    public void memberShip(String PageIndex,String PageCount,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Goods");
        params.put("fun", "GoodsList");
        params.put("PageIndex", PageIndex);
        params.put("PageCount", PageCount);
        params.put("GoodsIndextype","7");
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getselfGoodList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<SelfGoodsBean>,PageBean>(){

                    @Override
                    public void onResponse(ArrayList<SelfGoodsBean> selfGoodsBeans, String status,PageBean page) {
                        memberShipContract.getDataSuccess(selfGoodsBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        memberShipContract.getDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }
}
