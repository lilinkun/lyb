package com.lzyyd.lyb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.MyNickNameContract;
import com.lzyyd.lyb.interf.OnTitleBarClickListener;
import com.lzyyd.lyb.presenter.MyNickNamePresenter;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/11/23.
 */

public class MyNickNameActivity extends BaseActivity implements OnTitleBarClickListener , MyNickNameContract {

    @BindView(R.id.titlebar)
    CustomTitleBar customTitleBar;
    @BindView(R.id.et_nick)
    EditText nickEditText;

    private MyNickNamePresenter myNickNamePresenter = new MyNickNamePresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_nickname;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
        customTitleBar.SetOnTitleClickListener(this);

        myNickNamePresenter.attachView(this);
        myNickNamePresenter.onCreate(this);

        Bundle bundle = getIntent().getBundleExtra(LzyydUtil.TYPEID);
        String nickname = bundle.getString("nick");
        nickEditText.setText(nickname);
    }

    @Override
    public void onBackClick() {
        finish();
    }

    @OnClick({R.id.recommend_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.recommend_commit:

                if (nickEditText.getText().toString().trim().isEmpty()){
                    toast(R.string.mynick_isempty);
                }else {

                    myNickNamePresenter.modifyAccout(nickEditText.getText().toString(), ProApplication.SESSIONID(this));
                }
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
    public void modifySuccess() {
        Intent intent = new Intent();
        intent.putExtra("account", nickEditText.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void modifyFail(String msg) {
        toast(msg);
    }
}
