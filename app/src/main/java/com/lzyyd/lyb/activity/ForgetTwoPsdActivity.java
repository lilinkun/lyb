package com.lzyyd.lyb.activity;

import android.view.View;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.util.UiHelper;

import butterknife.OnClick;

/**
 * Created by LG on 2018/12/27.
 */

public class ForgetTwoPsdActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_two;
    }

    @Override
    public void initEventAndData() {

    }

    @OnClick({R.id.tv_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_next:
                UiHelper.launcher(this,ForgetThreePsdActivity.class);
                break;
        }
    }
}
