package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzyyd.lyb.contract.PayContract;
import com.lzyyd.lyb.contract.PersonalInfoContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.OrderDetailBean;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.entity.ResultBean;
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
 * Created by LG on 2019/9/11.
 */
public class PayPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private PayContract payContract;

    @Override
    public void onCreate(Context context) {
        this.manager = new DataManager(context);
        this.mContext = context;
        this.mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(IView view) {
        payContract = (PayContract) view;
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
                        payContract.getInfoSuccess(loginBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        payContract.getInfoFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<PersonalInfoBean,Object> o) {
                        super.onNext(o);
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
                    public void onResponse(CollectDeleteBean collectDeleteBean, String status,Object page) {
                        payContract.selfPaySuccess(collectDeleteBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        payContract.selfPayFail(msg);
                    }
                }));
    }

    /**
     *　订单详情
     * @param OrderSn
     * @param SessionId
     */
    public void orderDetail(String OrderSn,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Order");
        params.put("fun","OrderInfoDetail");
        params.put("OrderSn",OrderSn);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getOrderDetail(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<OrderDetailBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<OrderDetailBean> orderDetailBeans, String status,Object page) {
                        payContract.setDataSuccess(orderDetailBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        payContract.setDataFail(msg);
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
        mCompositeSubscription.add(manager.wxPay1(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean fareBean, String status,Object page) {
                        payContract.wxInfoSuccess(fareBean);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        payContract.wxInfoFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }));
    }

}
