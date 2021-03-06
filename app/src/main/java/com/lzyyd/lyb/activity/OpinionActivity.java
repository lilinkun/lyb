package com.lzyyd.lyb.activity;

import android.view.View;
import android.widget.EditText;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.OpinionContract;
import com.lzyyd.lyb.interf.OnTitleBarClickListener;
import com.lzyyd.lyb.presenter.OpinionPresenter;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.Eyes;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/11/21.
 */

public class OpinionActivity extends BaseActivity implements OnTitleBarClickListener,OpinionContract {

    @BindView(R.id.titlebar)
    CustomTitleBar customTitleBar;
    @BindView(R.id.et_opinion)
    EditText opinionEditText;

    OpinionPresenter opinionPresenter = new OpinionPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_opinion;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
        customTitleBar.SetOnTitleClickListener(this);

        opinionPresenter.attachView(this);
        opinionPresenter.onCreate(this);
    }

    @OnClick({R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_commit:

                if (opinionEditText != null && !opinionEditText.getText().toString().trim().isEmpty()){
                    opinionPresenter.upload(opinionEditText.getText().toString(), ProApplication.SESSIONID(this));
                }

                break;
        }
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
    public void onUploadSuccess() {
        opinionEditText.setText("");
        toast("意见反馈成功");
    }

    @Override
    public void onFail(String msg) {
        toast(msg);
    }
}
