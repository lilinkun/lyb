package com.lzyyd.lyb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.activity.SelfGoodsDetailActivity;
import com.lzyyd.lyb.adapter.RecordAdapter;
import com.lzyyd.lyb.adapter.SelfGoodsAdapter;
import com.lzyyd.lyb.base.BaseFragment;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.SelfGoodsContract;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.presenter.SelfGoodsPresenter;
import com.lzyyd.lyb.ui.GridSpacingItemDecoration;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.UiHelper;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by LG on 2018/12/12.
 */

public class SelfGoodFragment extends BaseFragment implements SelfGoodsContract, SelfGoodsAdapter.OnItemClickListener {

    @BindView(R.id.rv_self_goods)
    RecyclerView mSelfGoodsRv;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int lastVisibleItem = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SelfGoodsPresenter selfGoodsPresenter = new SelfGoodsPresenter();
    private SelfGoodsAdapter selfGoodsAdapter;
    private ArrayList<SelfGoodsBean> selfGoodsBeans;
    private boolean isLoad = true;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_self_goods;
    }

    @Override
    public void initEventAndData() {

        selfGoodsPresenter.attachView(this);
        selfGoodsPresenter.onCreate(getActivity());

        selfGoodsPresenter.getGoodList("1","20","132","add_time","","",ProApplication.SESSIONID(getActivity()));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        int spanCount = 2; // 2 columns
        int spacing = 20; // 50px

        boolean includeEdge = false;
        mSelfGoodsRv.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mSelfGoodsRv.setLayoutManager(gridLayoutManager);

        mSelfGoodsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (selfGoodsAdapter != null) {
                        if (lastVisibleItem + 1 == selfGoodsAdapter.getItemCount()) {
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    PAGE_INDEX++;
//                                    tbAllPresenter.setList(PAGE_INDEX + "",PAGE_COUNT,"64362000477",getArguments().getString("TBId"),"tk_total_sales_des", ProApplication.SESSIONID(getActivity()));
//                                }
//                            }, 200);
                        }

                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            }
        });


    }



    public void onPageChange(int page){
//        tbDisCountBeans.clear();
//        PAGE_INDEX = 1;
//        tbAllPresenter.setList(PAGE_INDEX + "",PAGE_COUNT,"64362000477",getArguments().getString("TBId"), LzyydUtil.strs[page], ProApplication.SESSIONID(getActivity()));
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void getDataSuccess(ArrayList<SelfGoodsBean> selfGoodsBeans,boolean load) {
        this.selfGoodsBeans = selfGoodsBeans;
        this.isLoad = load;
        if (selfGoodsAdapter == null) {
            selfGoodsAdapter = new SelfGoodsAdapter(getActivity(), selfGoodsBeans,2);
            mSelfGoodsRv.setAdapter(selfGoodsAdapter);
            selfGoodsAdapter.setItemClickListener(this);
        }else {

        }

    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void onItemClick(int position) {

        if (!ButtonUtils.isFastDoubleClick()) {
            Bundle bundle = new Bundle();
            bundle.putString("goodsid", selfGoodsBeans.get(position).getGoods_id());
            UiHelper.launcherBundle(getActivity(), SelfGoodsDetailActivity.class, bundle);
        }
    }
}
