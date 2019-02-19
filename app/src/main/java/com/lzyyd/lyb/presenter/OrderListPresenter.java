package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzyyd.lyb.activity.OrderListActivity;
import com.lzyyd.lyb.contract.OrderListContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.CountBean;
import com.lzyyd.lyb.entity.OrderBean;
import com.lzyyd.lyb.entity.OrderDetailBean;
import com.lzyyd.lyb.entity.OrderListBean;
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
 * Created by LG on 2018/12/20.
 */

public class OrderListPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private OrderListContract orderListContract;

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

    }

    @Override
    public void attachView(IView view) {
        orderListContract = (OrderListContract)view;
    }

    public void getOrderData(String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","BankBase");
        params.put("fun","BankBaseAmountAndPoint");
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getAccountInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CountBean,Object>() {
                    @Override
                    public void onResponse(CountBean countBean, String status) {
                        orderListContract.InfoAccountSuccess(countBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderListContract.InfoAccountFail(msg);
                    }
                }));
    }

    /**
     * 余额支付
     */
    public void selfPay(String PayPwd,String OrderNo,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Order");
        params.put("fun","BankNoPay");
        params.put("PayPwd",PayPwd);
        params.put("OrderNo",OrderNo);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.selfPay(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean collectDeleteBean, String status) {
                        orderListContract.selfPaySuccess(collectDeleteBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderListContract.selfPayFail(msg);
                    }
                }));
    }


    /**
     * 确认收货
     */
    public void sureReceipt(String OrderId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Order");
        params.put("fun","ConfirmReceipt");
        params.put("OrderId",OrderId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.sureReceipt(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean collectDeleteBean, String status) {
                        orderListContract.sureReceiptSuccess(collectDeleteBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderListContract.sureReceiptFail(msg);
                    }
                }));
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
                    public void onResponse(WxRechangeBean fareBean, String status) {
                        orderListContract.wxInfoSuccess(fareBean);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderListContract.wxInfoFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }));
    }

}
