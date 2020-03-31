package com.lzyyd.lyb.activity;

import android.view.View;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.UpdateManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/8/21.
 */

public class AboutAppActivity extends BaseActivity {

    @BindView(R.id.tv_page)
    TextView tv_page;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));


        final double code = UpdateManager.getInstance().getVersionName(this);

        tv_page.setText("版本: " + code);
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
