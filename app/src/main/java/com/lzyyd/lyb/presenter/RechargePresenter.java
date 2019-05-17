package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzyyd.lyb.contract.RechargeContract;
import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.entity.CountBean;
import com.lzyyd.lyb.entity.WxRechangeBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/1/5.
 */

public class RechargePresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RechargeContract rechargeContract;

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
        rechargeContract = (RechargeContract) view;
    }

    public void setWxPay(String Batch_No,String Charge_Amt,String Logo_ID,String Charge_Type,String apptype,String apppackage,String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","微信支付中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","BankCharge");
        params.put("fun","BankChargeRecharge");
        params.put("Batch_No",Batch_No);
        params.put("Charge_Amt",Charge_Amt);
        params.put("Logo_ID",Logo_ID);
        params.put("Charge_Type",Charge_Type);
        params.put("apptype",apptype);
        params.put("apppackage",apppackage);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.wxPay(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<WxRechangeBean,Object>() {
                    @Override
                    public void onResponse(WxRechangeBean fareBean, String status,Object page) {
                        rechargeContract.setReChargeSuccess(fareBean);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        rechargeContract.setReChargeFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }));
    }

    public void getOrderData(String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","获取余额中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","BankBase");
        params.put("fun","BankBaseAmountAndPoint");
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getAccountInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CountBean,Object>() {
                    @Override
                    public void onResponse(CountBean countBean, String status,Object page) {
                        rechargeContract.InfoAccountSuccess(countBean);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        rechargeContract.InfoAccountFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }));
    }
}
