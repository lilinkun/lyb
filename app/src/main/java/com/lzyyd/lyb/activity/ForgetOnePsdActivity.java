package com.lzyyd.lyb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.contract.ForgetPasswordContract;
import com.lzyyd.lyb.presenter.ForgetPasswordPresenter;
import com.lzyyd.lyb.util.ActivityUtil;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.UiHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/12/27.
 */

public class ForgetOnePsdActivity extends BaseActivity implements ForgetPasswordContract {

    @BindView(R.id.et_username)
    EditText et_username;

    private ForgetPasswordPresenter forgetPasswordPresenter = new ForgetPasswordPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_one;
    }

    @Override
    public void initEventAndData() {
        forgetPasswordPresenter.attachView(this);
        forgetPasswordPresenter.onCreate(this);
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));

        ActivityUtil.addActivity(this);
    }

    @OnClick({R.id.tv_next,R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_next:

                if (et_username != null && !et_username.getText().toString().isEmpty()){
                    forgetPasswordPresenter.getMobile(et_username.getText().toString());
                }

                break;

            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void getMobileSuccess(String mobile) {
        if (mobile != null && !mobile.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString("mobile", mobile);
            bundle.putString("username", et_username.getText().toString());
            UiHelper.launcherBundle(this, ForgetThreePsdActivity.class, bundle);
        }else {
            toast("此用户为绑定有效手机号码");
        }
    }

    @Override
    public void getMobileFail(String msg) {
        toast(msg);
    }

    @Override
    public void getVcodeSuccess(String vcode) {

    }

    @Override
    public void getVcodeFail(String msg) {

    }
}
