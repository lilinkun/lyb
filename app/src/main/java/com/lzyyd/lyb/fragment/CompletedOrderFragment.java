package com.lzyyd.lyb.fragment;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.activity.AllOrderActivity;
import com.lzyyd.lyb.adapter.SelfOrderAdapter;
import com.lzyyd.lyb.base.BaseFragment;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.SelfOrderContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.SelfOrderBean;
import com.lzyyd.lyb.interf.IPayOrderClickListener;
import com.lzyyd.lyb.presenter.SelfOrderPresenter;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.UiHelper;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by LG on 2018/12/5.
 */

public class CompletedOrderFragment  extends BasePagerFragment implements SelfOrderContract, SelfOrderAdapter.OnItemClick, SelfOrderAdapter.OnItemClickListener {

    @BindView(R.id.compelet_order_rv)
    RecyclerView compeletOrderRv;
    @BindView(R.id.ll_no_order)
    LinearLayout ll_no_order;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SelfOrderPresenter selfOrderPresenter = new SelfOrderPresenter();
    private ArrayList<SelfOrderBean> selfOrderBeans;
    private String orderId;
    private SelfOrderAdapter selfOrderAdapter;
    private IPayOrderClickListener payListener;
    private int pageIndex = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_completed_order;
    }

    @Override
    public void initEventAndData() {
        selfOrderPresenter.attachView(this);
        selfOrderPresenter.onCreate(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        compeletOrderRv.setLayoutManager(linearLayoutManager);
        //添加自定义分割线
//        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.custom_divider));
//        compeletOrderRv.addItemDecoration(divider);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                selfOrderPresenter.getOrderData(pageIndex+"", LzyydUtil.PAGE_COUNT,"2", ProApplication.SESSIONID(getActivity()));
            }
        });

        selfOrderPresenter.getOrderData(pageIndex+"", LzyydUtil.PAGE_COUNT,"2", ProApplication.SESSIONID(getActivity()));
    }

    public void setData(){
        if (getActivity() != null) {
            selfOrderPresenter.getOrderData(pageIndex+"", LzyydUtil.PAGE_COUNT, "2", ProApplication.SESSIONID(getActivity()));
        }
    }

    public void setPayListener(IPayOrderClickListener payListener){
        this.payListener = payListener;
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void getDataSuccess(ArrayList<SelfOrderBean> selfOrderBeans) {

        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
        compeletOrderRv.setVisibility(View.VISIBLE);
        ll_no_order.setVisibility(View.GONE);

        if (selfOrderBeans.size() > 0) {
            this.selfOrderBeans = selfOrderBeans;
            if (selfOrderAdapter == null) {
                selfOrderAdapter = new SelfOrderAdapter(getActivity(), selfOrderBeans, this);

                compeletOrderRv.setAdapter(selfOrderAdapter);
                selfOrderAdapter.setItemClickListener(this);
            }else {
                selfOrderAdapter.setData(selfOrderBeans);
            }
        }else {
            ll_no_order.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getDataFail(String msg) {
        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
        ll_no_order.setVisibility(View.VISIBLE);
        if (msg.contains("查无数据")) {
            compeletOrderRv.setVisibility(View.GONE);
        }
    }

    @Override
    public void exitOrderSuccess(CollectDeleteBean collectDeleteBean) {

        for (int i = 0;i<selfOrderBeans.size();i++){
            if(selfOrderBeans.get(i).getOrderId().equals(orderId)){
                selfOrderBeans.remove(i);
                if (selfOrderAdapter != null) {
                    selfOrderAdapter.setData(selfOrderBeans);
                }
            }
        }
    }

    @Override
    public void exitOrderFail(String smg) {

    }

    @Override
    public void exit_order(String orderId) {
        this.orderId = orderId;
        selfOrderPresenter.exitOrder(orderId,ProApplication.SESSIONID(getActivity()));
    }

    @Override
    public void go_pay(SelfOrderBean orderId) {
    }


    @Override
    public void sureReceipt(String orderId) {
        payListener.SureReceive(orderId);
    }

    @Override
    public void getQrcode(String orderId) {

    }

    @Override
    public void onItemClick(int position) {

        if (!ButtonUtils.isFastDoubleClick()) {
            Bundle bundle = new Bundle();
            bundle.putInt("status", selfOrderBeans.get(position).getOrderStatus());
            bundle.putString("order_sn", selfOrderBeans.get(position).getOrderSn());
            UiHelper.launcherForResultBundle(getActivity(), AllOrderActivity.class, 0x0987, bundle);
        }
    }

    @Override
    public void loadData() {
        selfOrderPresenter.getOrderData(pageIndex+"", LzyydUtil.PAGE_COUNT,"2", ProApplication.SESSIONID(getActivity()));
    }
}
