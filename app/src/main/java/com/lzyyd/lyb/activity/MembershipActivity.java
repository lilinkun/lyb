package com.lzyyd.lyb.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.adapter.MemberShipAdapter;
import com.lzyyd.lyb.adapter.OrderShopAdapter;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.MemberShipContract;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.presenter.MemberShipPresenter;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.UiHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/12/22.
 */

public class MembershipActivity extends BaseActivity implements MemberShipContract, OrderShopAdapter.OnItemClickListener {

    @BindView(R.id.rv_membership)
    RecyclerView rv_membership;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    MemberShipPresenter memberShipPresenter = new MemberShipPresenter();
    private MemberShipAdapter memberShipAdapter;
    private ArrayList<SelfGoodsBean> selfGoodsBeans;
    private int pageIndex = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_membership;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarColor(this,getResources().getColor(R.color.black_bg));
        memberShipPresenter.attachView(this);
        memberShipPresenter.onCreate(this);
        memberShipPresenter.memberShip(pageIndex+"", LzyydUtil.PAGE_COUNT, ProApplication.SESSIONID(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_membership.setLayoutManager(linearLayoutManager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                memberShipPresenter.memberShip(pageIndex+"", LzyydUtil.PAGE_COUNT, ProApplication.SESSIONID(MembershipActivity.this));
            }
        });

        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.member_divider));
        rv_membership.addItemDecoration(divider);
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:

                finish();

                break;
        }
    }

    @Override
    public void getDataSuccess(ArrayList<SelfGoodsBean> selfGoodsBeans) {

        if (refreshLayout != null && refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }

        if (memberShipAdapter == null ) {
            this.selfGoodsBeans = selfGoodsBeans;
            memberShipAdapter = new MemberShipAdapter(this, selfGoodsBeans);
            rv_membership.setAdapter(memberShipAdapter);
            memberShipAdapter.setItemClickListener(this);
        }else {
            memberShipAdapter.setData(selfGoodsBeans);
        }

    }

    @Override
    public void getDataFail(String msg) {
        if (!msg.contains("查无数据")) {
            toast(msg);
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onItemClick(int position) {

        if (!ButtonUtils.isFastDoubleClick()) {
            if (selfGoodsBeans.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("goodsid", selfGoodsBeans.get(position).getGoods_id());
                UiHelper.launcherBundle(this, SelfGoodsDetailActivity.class, bundle);
            }
        }
    }
}
