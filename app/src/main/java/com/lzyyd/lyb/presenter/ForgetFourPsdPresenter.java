package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.contract.ForgetFourPsdContract;
import com.lzyyd.lyb.contract.ForgetPasswordContract;
import com.lzyyd.lyb.entity.LoginBean;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/27.
 */

public class ForgetFourPsdPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ForgetFourPsdContract forgetPasswordContract;

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
        forgetPasswordContract = (ForgetFourPsdContract) view;
    }

    /**
     * 忘记密码
     * @param username
     */
    public void getModify(String new_password,String mobile,String Code,String username){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserBase");
        params.put("fun","RetrievePassword");
        params.put("new_password",new_password);
        params.put("mobile",mobile);
        params.put("Code",Code);
        params.put("username",username);
        mCompositeSubscription.add(manager.getMobile(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status) {
                        forgetPasswordContract.onModifyPsdSuccess(o.toString());
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        forgetPasswordContract.onModiftPsdFail(msg);
                    }
                }));
    }

    /**
     * 登陆
     */
    public void login(String username,String psw,String sessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "UserBase");
        params.put("fun", "Login");
        params.put("UserName", username);
        params.put("PassWord", psw);
        params.put("SessionId", sessionId);
        params.put("MobileType","android");
        mCompositeSubscription.add(manager.login(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean loginBean, String status) {
                        forgetPasswordContract.onLoginSuccess(loginBean);
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        forgetPasswordContract.onLoginFail(msg);
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }
}
