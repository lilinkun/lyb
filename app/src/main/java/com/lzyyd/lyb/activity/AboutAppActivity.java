package com.lzyyd.lyb.activity;

import android.view.View;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.util.Eyes;

import butterknife.OnClick;

/**
 * Created by LG on 2019/8/21.
 */

public class AboutAppActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:

                finish();

                break;
        }
    }
}
