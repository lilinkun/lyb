package com.lzyyd.lyb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.lzyyd.lyb.R;
import com.lzyyd.lyb.adapter.JdGoodsAdapter;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.JdGoodsContract;
import com.lzyyd.lyb.entity.JdGoodsBean;
import com.lzyyd.lyb.presenter.JdGoodsPresenter;
import com.lzyyd.lyb.ui.FullyGridLayoutManager;
import com.lzyyd.lyb.ui.GridSpacingItemDecoration;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.UiHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.jingfen.query.request.UnionOpenGoodsJingfenQueryRequest;
import jd.union.open.goods.jingfen.query.response.JFGoodsResp;
import jd.union.open.goods.jingfen.query.response.UnionOpenGoodsJingfenQueryResponse;

/**
 * Created by LG on 2019/1/22.
 */
public class JdGoodActivity extends BaseActivity implements JdGoodsContract, JdGoodsAdapter.OnItemClickListener {

    @BindView(R.id.rv_jd_goods)
    RecyclerView recyclerView;

    private JdGoodsAdapter jdGoodsAdapter;
    private ArrayList<JdGoodsBean> jdGoodsBean;

    private JdGoodsPresenter jdGoodsPresenter = new JdGoodsPresenter();

    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

    KelperTask mKelperTask;
    public final static String mhome = "http://m.jd.com";
    public final static String mlist = "https://wqs.jd.com/order/orderlist_merge.shtml";
    public static final int timeOut = 15;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    Bundle bundle = msg.getData();
                    JFGoodsResp[] st = (JFGoodsResp[])bundle.getSerializable("str");

//                    jdGoodsAdapter = new JdGoodsAdapter(JdGoodActivity.this,st);
//
//                    recyclerView.setAdapter(jdGoodsAdapter);
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_jdgoods;
    }

    @Override
    public void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));

        GridLayoutManager fullyGridLayoutManager = new GridLayoutManager(this, 3);
        fullyGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        int spanCount = 2; // 2 columns
        int spacing = 0; // 50px

        boolean includeEdge = false;
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        jdGoodsPresenter.onCreate(this);
        jdGoodsPresenter.attachView(this);

        jdGoodsPresenter.setData("1","20","desc","1","commissionShare", ProApplication.SESSIONID(this));

//        try {
//            mKelperTask = KeplerApiManager.getWebViewService().openAppWebViewPage(this, mhome, mKeplerAttachParameter, mOpenAppAction);
//            new Thread(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                            String SERVER_URL = "https://router.jd.com/api";
//                            String appKey = "b101e0a5049146d8abd7186eca381882";
//                            String appSecret ="359e3bae47a94afc82fefb7eeae46d5e";
//                            String accessToken = "";
//                                JdClient client=new DefaultJdClient(SERVER_URL,accessToken,appKey,appSecret);
//                                UnionOpenGoodsJingfenQueryRequest request = new UnionOpenGoodsJingfenQueryRequest();
//                                JFGoodsReq goodsReq = new JFGoodsReq();
//                                goodsReq.setEliteId(3);
//                                goodsReq.setSort("asc");
//                                goodsReq.setPageIndex(1);
//                                goodsReq.setPageSize(20);
//                                goodsReq.setSortName("price");
//                                request.setGoodsReq(goodsReq);
//                                UnionOpenGoodsJingfenQueryResponse response = client.execute(request);
//                                JFGoodsResp[] jfGoodsResp = response.getData();
//                                Message message = new Message();
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("str",jfGoodsResp);
//                                message.setData(bundle);
//                                message.what = 1000;
//                                handler.sendMessage(message);
//                            } catch (JdException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//            ).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

     private OpenAppAction mOpenAppAction = new OpenAppAction() {
            @Override
            public void onStatus(final int status,String url) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (status == OpenAppAction.OpenAppAction_result_APP){
                            toast("呼叫成功");
                        }else if (status == OpenAppAction.OpenAppAction_result_NoJDAPP){
                            toast("没有安装jdapp");
                        }else if (status == OpenAppAction_result_BlackUrl){
                            toast("不在白名单");
                        }
                    }
                });
            }
        };

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void getDataSuccess(ArrayList<JdGoodsBean> jdGoodsBean) {
        this.jdGoodsBean = jdGoodsBean;
        jdGoodsAdapter = new JdGoodsAdapter(JdGoodActivity.this,jdGoodsBean);

        recyclerView.setAdapter(jdGoodsAdapter);

        jdGoodsAdapter.setItemClickListener(this);
    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void onItemClick(int position) {
        if (jdGoodsBean != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("jdgoods", jdGoodsBean.get(position));
            UiHelper.launcherBundle(this, BuyJdGoodsActivity.class, bundle);
        }
    }
}
