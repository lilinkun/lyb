package com.lzyyd.lyb.presenter;

import android.content.Context;
import android.content.Intent;

import com.lzyyd.lyb.contract.OpinionContract;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/2.
 */

public class OpinionPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private OpinionContract opinionContract;

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
        opinionContract = (OpinionContract) view;
    }


    public void upload(String contents,String sessionId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("cls","FeedBack");
        hashMap.put("fun","FeedBackCreate");
        hashMap.put("contents",contents);
        hashMap.put("SessionId",sessionId);
        mCompositeSubscription.add(manager.opinion(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {


                    @Override
                    public void onResponse(Object o, String status) {
                        opinionContract.onUploadSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        opinionContract.onFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }
                }));
    }
}
