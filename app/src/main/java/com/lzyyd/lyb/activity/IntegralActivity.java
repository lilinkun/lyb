package com.lzyyd.lyb.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.adapter.IntegralAdapter;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.IntegralContract;
import com.lzyyd.lyb.entity.AmountPriceBean;
import com.lzyyd.lyb.entity.IntegralBean;
import com.lzyyd.lyb.entity.PointListBean;
import com.lzyyd.lyb.presenter.IntegralPresenter;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/12/29.
 */

public class IntegralActivity extends BaseActivity implements IntegralContract{

    @BindView(R.id.rv_style)
    RecyclerView rv_style;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_pay_balance)
    TextView tv_pay_balance;
    @BindView(R.id.tv_style)
    TextView tv_style;

    private IntegralPresenter integralPresenter = new IntegralPresenter();
    private int mListStyle = 0;
    private int PAGE_INDEX = 1;
    private int sizeInt = 0;
    private IntegralAdapter integralAdapter ;
    private int lastVisibleItem = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    public void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));

        integralPresenter.attachView(this);
        integralPresenter.onCreate(this);

        mListStyle =  getIntent().getBundleExtra(LzyydUtil.TYPEID).getInt("style");

        if (mListStyle == 0) {
            integralPresenter.getIntegralData(PAGE_INDEX+"", LzyydUtil.PAGE_COUNT, ProApplication.SESSIONID(this));
            tv_balance.setText("积分");
            tv_style.setText("积分明细");
        }else if(mListStyle == 1){
            integralPresenter.getPriceData(PAGE_INDEX+"", LzyydUtil.PAGE_COUNT, ProApplication.SESSIONID(this));
            tv_balance.setText("余额");
            tv_style.setText("余额明细");
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_style.setLayoutManager(linearLayoutManager);

        rv_style.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (integralAdapter != null) {
                        if (lastVisibleItem + 1 == integralAdapter.getItemCount()) {
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
                            if (PAGE_INDEX * Integer.valueOf(LzyydUtil.PAGE_COUNT) > pointListBeans.size()){
                                toast("已到末尾");
                            }else {
                                PAGE_INDEX++;
                                if (mListStyle == 0) {
                                    integralPresenter.getIntegralData(PAGE_INDEX + "", LzyydUtil.PAGE_COUNT, ProApplication.SESSIONID(IntegralActivity.this));
                                }else {
                                    integralPresenter.getPriceData(PAGE_INDEX+"", LzyydUtil.PAGE_COUNT, ProApplication.SESSIONID(IntegralActivity.this));
                                }
                            }
//                                }
//                            }, 200);
                        }

                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ll_back:

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

    private ArrayList<PointListBean> pointListBeans;
    @Override
    public void getGoodsIntegralSuccess(IntegralBean integralBean) {
        if (integralAdapter == null) {
            pointListBeans = integralBean.getPoint_list();
            integralAdapter = new IntegralAdapter(this, integralBean.getPoint_list(), 0);
            rv_style.setAdapter(integralAdapter);

            sizeInt = integralBean.getPoint_list().size();

            if (mListStyle == 0) {
                BigDecimal b = new BigDecimal(integralBean.getTotal_point());
                double total_point = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                tv_pay_balance.setText("¥" + total_point);
            }
        }else {
            pointListBeans.addAll(integralBean.getPoint_list());
            integralAdapter.setData(pointListBeans);
        }
    }

    @Override
    public void getGoodsIntegralFail(String msg) {
        toast(msg);
    }


    @Override
    public void getDataSuccess(AmountPriceBean amountPriceBean) {
        if (integralAdapter == null) {
            pointListBeans = amountPriceBean.getAmount_list();
            integralAdapter = new IntegralAdapter(this, amountPriceBean.getAmount_list(), 1);
            rv_style.setAdapter(integralAdapter);
            sizeInt = amountPriceBean.getAmount_list().size();
            if (mListStyle == 1) {
                tv_pay_balance.setText("¥" + amountPriceBean.getTotal_amount());
            }
        }else {
            pointListBeans.addAll(amountPriceBean.getAmount_list());
            integralAdapter.setData(pointListBeans);

        }
    }

    @Override
    public void getDataFail(String msg) {
        toast(msg);
    }
}
