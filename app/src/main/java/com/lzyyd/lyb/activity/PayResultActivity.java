package com.lzyyd.lyb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.UiHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/12/20.
 */

public class PayResultActivity extends BaseActivity {

    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_see_order)
    TextView tv_see_order;
    @BindView(R.id.tv_back_home)
    TextView tv_back_home;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_result;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarColor(this,getResources().getColor(R.color.pop_text_bg));

        String price = getIntent().getBundleExtra(LzyydUtil.TYPEID).getString("price");
        tv_price.setText("¥ "  + price);

    }

    @OnClick({R.id.tv_see_order,R.id.tv_back_home,R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_see_order:

                Bundle bundle = new Bundle();
                bundle.putString("goodsname", "");
                UiHelper.launcherBundle(this, OrderListActivity.class,bundle);
                finish();

                break;

            case R.id.tv_back_home:

                Intent intent = new Intent(PayResultActivity.this, MainFragmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.ll_back:

                finish();

                break;
        }

    }
}
