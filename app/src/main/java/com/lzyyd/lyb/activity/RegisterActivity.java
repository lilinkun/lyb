package com.lzyyd.lyb.activity;


import android.app.ProgressDialog;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.contract.RegisterContract;
import com.lzyyd.lyb.interf.OnTitleBarClickListener;
import com.lzyyd.lyb.presenter.RegisterPresenter;
import com.lzyyd.lyb.ui.CustomRegisterLayout;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.Eyes;

import butterknife.BindView;

/**
 * Created by LG on 2018/11/13.
 * 注册
 */
public class RegisterActivity extends BaseActivity implements OnTitleBarClickListener, CustomRegisterLayout.OnRegisterListener, RegisterContract {


    @BindView(R.id.custom_title)
    CustomTitleBar customTitleBar;
    @BindView(R.id.custom_register)
    CustomRegisterLayout customRegisterLayout;

    private RegisterPresenter mRegisterPresenter = new RegisterPresenter();
    ProgressDialog progressDialog = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
        customTitleBar.SetOnTitleClickListener(this);

        customRegisterLayout.setVcodeLisener(this);

        mRegisterPresenter.onCreate(this);
        mRegisterPresenter.attachView(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.login_loading));
        progressDialog.setCancelable(true);
    }


    @Override
    public void onBackClick() {
        finish();
    }


    @Override
    public void getVcode(String phoneNum) {
        mRegisterPresenter.SendSms(phoneNum,"");
    }

    @Override
    public void getOver(String account,String phone,String vcode, String psd,String username) {
        mRegisterPresenter.register(account,phone,vcode,psd,progressDialog,username);
    }

    @Override
    public void getModify(String phone, String vcode, String psd) {
    }

    @Override
    public void onSendVcodeSuccess() {

    }

    @Override
    public void onSendVcodeFail(String msg) {

    }

    @Override
    public void onRegisterSuccess() {
        progressDialog.dismiss();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegisterPresenter.onStop();
    }

    @Override
    public void onRegisterFail(String msg) {
        progressDialog.dismiss();
        toast(msg);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}
