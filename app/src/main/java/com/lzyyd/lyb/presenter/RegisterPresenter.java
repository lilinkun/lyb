package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.lzyyd.lyb.contract.RegisterContract;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/11/29.
 */

public class RegisterPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RegisterContract registerContract;



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
        registerContract = (RegisterContract) view;
    }

    /**
     * 发送短信验证码
     * @param mobile
     * @param sessionId
     */
    public void SendSms(String mobile,String sessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "SendSms");
        params.put("fun", "RegisteredSmsCode");
        params.put("mobile", mobile);
        params.put("SessionId", sessionId);
        mCompositeSubscription.add(manager.register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack(){

                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        registerContract.onSendVcodeSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        registerContract.onSendVcodeFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }


    /**
     * 注册
     * @param mobile
     * @param code
     * @param progressDialog
     */
    public void register(String account,String mobile, String code, String psd, final ProgressDialog progressDialog,String username){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "UserBase");
        params.put("fun", "Register");
        params.put("user_password", psd);
        params.put("mobile", mobile);
        params.put("Code", code);
        params.put("UserName",account);
        params.put("name",username);
        mCompositeSubscription.add(manager.register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack(){

                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        registerContract.onRegisterSuccess();
                        progressDialog.show();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        registerContract.onRegisterFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }

    /**
     * 修改密码
     * @param mobile
     * @param code
     */
    public void modify(String mobile, String code, String psd,String SessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "UserBase");
        params.put("fun", "UpdatePassWord");
        params.put("new_password", psd);
        params.put("mobile", mobile);
        params.put("Code", code);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack(){

                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        registerContract.onRegisterSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        registerContract.onRegisterFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }

}
