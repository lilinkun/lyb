package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzyyd.lyb.contract.SelfGoodsContract;
import com.lzyyd.lyb.contract.SettingContract;
import com.lzyyd.lyb.entity.DownloadBean;
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
 * Created by LG on 2018/12/14.
 */

public class SettingPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SettingContract settingContract;

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
        settingContract = (SettingContract) view;
    }


    public void LoginOut(String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","退出登录中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "UserBase");
        params.put("fun", "Logout");
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.loginout(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<String,Object>(){

                    @Override
                    public void onResponse(String selfGoodsBeans, String status) {
                        settingContract.LoginOutSuccess(selfGoodsBeans);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        settingContract.LoginOutFail(msg);
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

    public void update(String sessionid){
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "Update");
        params.put("cls", "Home");
        params.put("SessionId", sessionid);
        mCompositeSubscription.add(manager.update(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<DownloadBean , Object>() {

                    @Override
                    public void onResponse(DownloadBean downloadBean, String status) {
                        settingContract.updateSuccess(downloadBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        settingContract.updateFail(msg);
                    }


                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }
                })
        );

    }
}
