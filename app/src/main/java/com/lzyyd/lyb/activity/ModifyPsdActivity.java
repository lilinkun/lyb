package com.lzyyd.lyb.activity;

import android.content.SharedPreferences;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.RegisterContract;
import com.lzyyd.lyb.interf.OnTitleBarClickListener;
import com.lzyyd.lyb.presenter.RegisterPresenter;
import com.lzyyd.lyb.ui.CustomRegisterLayout;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;

import butterknife.BindView;

/**
 * Created by LG on 2018/11/13.
 */

public class ModifyPsdActivity extends BaseActivity implements OnTitleBarClickListener,CustomRegisterLayout.OnRegisterListener,RegisterContract {

    @BindView(R.id.custom_title)
    CustomTitleBar customTitleBar;
    @BindView(R.id.custom_register)
    CustomRegisterLayout customRegisterLayout;

    private RegisterPresenter mRegisterPresenter = new RegisterPresenter();
    String psd = "";
    String mobile = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_psw;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
        customTitleBar.SetOnTitleClickListener(this);

        customRegisterLayout.setVcodeLisener(this);
        mRegisterPresenter.onCreate(this);
        mRegisterPresenter.attachView(this);
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onSendVcodeSuccess() {

    }

    @Override
    public void onSendVcodeFail(String msg) {

    }

    @Override
    public void onRegisterSuccess() {
            toast("修改成功");
            SharedPreferences sharedPreferences = getSharedPreferences(LzyydUtil.LOGIN, MODE_PRIVATE);

            if (mobile.equals(sharedPreferences.getString("account",""))) {
                sharedPreferences.edit().putString("sessionid", ProApplication.SESSIONID(this))
                        .putString("password", psd).commit();
            }

        finish();
    }

    @Override
    public void onRegisterFail(String msg) {

    }

    @Override
    public void getVcode(String phoneNum) {
        mRegisterPresenter.SendSms(phoneNum,"");
    }


    @Override
    public void getOver(String account,String phone, String vcode, String psd,String username) {
    }

    @Override
    public void getModify(String phone, String vcode, String psd) {
        mRegisterPresenter.modify(phone,vcode,psd,ProApplication.SESSIONID(this));
        this.mobile = phone;
        this.psd = psd;
    }
}
