package com.lzyyd.lyb.presenter;

import android.content.Context;
import android.content.Intent;

import com.lzyyd.lyb.contract.MeContract;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/3.
 */

public class MePresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private MeContract meContract;

    @Override
    public void onCreate(Context context) {
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
        meContract = (MeContract) view;
    }

    public void getInfo(String session){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserBase");
        params.put("fun","UserBasedetailById");
        params.put("SessionId",session);
        mCompositeSubscription.add(manager.getInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<PersonalInfoBean,Object>() {

                    @Override
                    public void onResponse(PersonalInfoBean loginBean, String status,Object page) {
                        meContract.getInfoSuccess(loginBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        meContract.getInfoFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<PersonalInfoBean,Object> o) {
                        super.onNext(o);
                    }

                }));
    }


}
