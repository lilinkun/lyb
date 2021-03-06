package com.lzyyd.lyb.activity;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.ModifyPayPsdContract;
import com.lzyyd.lyb.entity.LoginBean;
import com.lzyyd.lyb.presenter.ModifyPayPsdPresenter;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.PhoneFormatCheckUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/12/22.
 */

public class ModifyPayActivity extends BaseActivity implements ModifyPayPsdContract{

    @BindView(R.id.tv_phone)
    TextView textView;
    @BindView(R.id.tv_send_vcode)
    TextView tv_send_vcode;
    @BindView(R.id.ev_sure_psd)
    EditText ev_sure_psd;
    @BindView(R.id.ev_new_psd)
    EditText ev_new_psd;
    @BindView(R.id.ev_vcode)
    EditText ev_vcode;
    @BindView(R.id.titlebar)
    CustomTitleBar titleBar;

    ModifyPayPsdPresenter modifyPayPsdPresenter = new ModifyPayPsdPresenter();
    private int modifyInt = 0;
    private LoginBean loginBean;

    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_pay_psd;
    }

    @Override
    public void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));

        modifyPayPsdPresenter.attachView(this);
        modifyPayPsdPresenter.onCreate(this);

        modifyInt = getIntent().getBundleExtra(LzyydUtil.TYPEID).getInt("modify");

        if (modifyInt == 1) {
            titleBar.setTileName("修改密码");
        }else {
            titleBar.setTileName("修改支付密码");
        }

        SharedPreferences sharedPreferences = getSharedPreferences(LzyydUtil.LOGIN,MODE_PRIVATE);
        String loginInfo = sharedPreferences.getString("logininfo","");
        try {
            loginBean = (LoginBean) LzyydUtil.unSerialization(loginInfo);
            textView.setText(PhoneFormatCheckUtils.phoneAddress(loginBean.getMobile()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.tv_send_vcode,R.id.ev_new_psd,R.id.tv_modify_commit,R.id.iv_head_left})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_send_vcode:

                modifyPayPsdPresenter.SendSms(ProApplication.SESSIONID(this));

                myCountDownTimer.start();
                break;

            case R.id.tv_modify_commit:
                if(ev_vcode.getText().toString().isEmpty()){
                    toast("验证码不能为空");
                }else if (ev_new_psd.getText().toString().isEmpty()){
                    toast("新密码不能为空");
                }else if (ev_new_psd.getText().toString().length() < 6){
                    toast(R.string.psd_limit);
                }else if (ev_sure_psd.getText().toString().isEmpty()){
                    toast("确认密码不能为空");
                }else if (!ev_new_psd.getText().toString().equals(ev_sure_psd.getText().toString())){
                    toast("两次密码不同，请确认");
                }else {
                    if (modifyInt == 1){
                        modifyPayPsdPresenter.modify(loginBean.getMobile(),ev_vcode.getText().toString(),ev_new_psd.getText().toString(),ProApplication.SESSIONID(this));
                    }else {
                        modifyPayPsdPresenter.modifyPsd(ev_vcode.getText().toString(), ev_new_psd.getText().toString(), ProApplication.SESSIONID(this));
                    }
                }

                break;

            case R.id.iv_head_left:

                finish();

                break;
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onSendVcodeSuccess() {
        toast("获取验证码成功");
    }

    @Override
    public void onSendVcodeFail(String str) {toast("获取验证码失败");}

    @Override
    public void modifySuccess() {
        toast("修改成功");
        finish();
    }

    @Override
    public void modifyFail(String str) {
        toast("修改失败");
    }

    @Override
    public void modifyPsdSuccess() {
        toast("修改成功");
        finish();
    }

    @Override
    public void modifyPsdFail(String msg) {
        toast(msg);
    }

    /**
     * 获取验证码倒计时
     */
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            tv_send_vcode.setClickable(false);
            tv_send_vcode.setText(l/1000+"s");
            tv_send_vcode.setTextColor(getResources().getColor(R.color.line_bg));
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tv_send_vcode.setText(R.string.register_send_vcoed);
            //设置可点击
            tv_send_vcode.setClickable(true);

            tv_send_vcode.setTextColor(getResources().getColor(R.color.register_vcode_bg));
        }
    }
}
