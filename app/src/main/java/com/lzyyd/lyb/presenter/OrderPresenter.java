package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.lzyyd.lyb.contract.OpinionContract;
import com.lzyyd.lyb.contract.OrderContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.OrderBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/14.
 */

public class OrderPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private OrderContract orderContract;

    @Override
    public void onCreate(Context context) {
        this.mContext = context;
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void attachView(IView view)  {
        orderContract = (OrderContract) view;
    }

    /**
     * 获取购物车列表
     * @param SessionId
     */
    public void getList(String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","获取数据中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Cart");
        params.put("fun","CartList");
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getCartList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<OrderListBean<ArrayList<OrderBean>>>,Object>() {
                    @Override
                    public void onResponse(ArrayList<OrderListBean<ArrayList<OrderBean>>> orderListBeans, String status,Object page) {
                        orderContract.OrderListSuccess(orderListBeans);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderContract.OrderListFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }));
    }


    /**
     * 修改购物车信息
     * @param Num
     * @param CartId
     * @param SessionId
     */
    public void modifyOrder(final String Num, String CartId,final View view, String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","修改信息中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Cart");
        params.put("fun","CartUpdate");
        params.put("Num",Num);
        params.put("CartId",CartId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.modifyOrder(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean orderListBeans, String status,Object page) {
                        orderContract.modifyOrderSuccess(orderListBeans,Num,view);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderContract.OrderListFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }));
    }


    /**
     * 删除信息
     * @param CartId
     * @param SessionId
     */
    public void deleteOrder(String CartId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Cart");
        params.put("fun","CartDelete");
        params.put("CartId",CartId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.deleteGoods(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean b, String status,Object page) {
                        orderContract.deleteGoodsSuccess(b);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderContract.deleteGoodsFail(msg);
                    }
                }));
    }

    /**
     * 判断是否有地址
     * @param SessionId
     */
    public void isUserAddress(String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserAddress");
        params.put("fun","Is_UserAddress");
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getIsAddress(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean collectDeleteBean, String status,Object page) {
                        if (collectDeleteBean.getStatus() == 0) {
                            orderContract.isAddressSuccess(collectDeleteBean.getMessage());
                        }else if (collectDeleteBean.getStatus() == 101){
                            orderContract.isAddressFail(collectDeleteBean.getMessage());
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        orderContract.isAddressFail(msg);
                    }
                }));
    }
}
