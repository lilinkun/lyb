package com.lzyyd.lyb.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.lzyyd.lyb.contract.RegisterContract;
import com.lzyyd.lyb.contract.SelfGoodsDetailContract;
import com.lzyyd.lyb.entity.BuyBean;
import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.GoodsDetailBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.entity.PageBean;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/8.
 */

public class SelfGoodsDetailPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SelfGoodsDetailContract selfGoodsDetailContract;


    @Override
    public void onCreate(Context context) {
        this.mContext = context;
        manager = new DataManager(context);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void attachView(IView view) {
        selfGoodsDetailContract = (SelfGoodsDetailContract) view;
    }

    public void getGoodsDetail(String goodsId,String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","获取数据中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Goods");
        params.put("fun","GoodsDetail");
        params.put("goodsId",goodsId);
        params.put("SessionId",SessionId);

        mCompositeSubscription.add(manager.getSelfGoodDetail(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new HttpResultCallBack<GoodsDetailBean<ArrayList>, Object>() {

            @Override
            public void onResponse(GoodsDetailBean<ArrayList> objectObjectGoodsDetailBean, String status,Object page) {
                selfGoodsDetailContract.getDataSuccess(objectObjectGoodsDetailBean);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onErr(String msg, String status) {
                selfGoodsDetailContract.getDataFail(msg);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

        }));

    }

    public void onCollect(String goodsId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Collect");
        params.put("fun","CollectCreate");
        params.put("goodsId",goodsId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addGoodCollect(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectBean, Object>() {

                    @Override
                    public void onResponse(CollectBean collectBean, String status,Object page) {
                        selfGoodsDetailContract.addCollectSuccess(collectBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfGoodsDetailContract.addCollectFail(msg);
                    }
                }));
    }

    /**
     * 是否有收藏
     * @param goodsId
     * @param SessionId
     */
    public void isCollect(String goodsId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Collect");
        params.put("fun","Is_Collect");
        params.put("goodsId",goodsId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.isGoodCollect(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<String, Object>() {

                    @Override
                    public void onResponse(String collectBean, String status,Object page) {
//                        selfGoodsDetailContract.addCollectSuccess(collectBean);
                        selfGoodsDetailContract.isGoodsCollectSuccess(collectBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
//                        selfGoodsDetailContract.addCollectFail(msg);
                    }

                }));
    }

    /**
     * 删除收藏
     * @param collectId
     * @param SessionId
     */
    public void deleteCollect(String collectId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Collect");
        params.put("fun","ProjectCheckDelete");
        params.put("collectId",collectId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.DeleteCollectGood(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean collectBeans, String status,Object page) {
                        if (collectBeans.getStatus() == 0) {
                            selfGoodsDetailContract.deleteCollectSuccess(collectBeans.getMessage());
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {

                    }
                }));
    }


    /**
     * 加入购物车
     * @param GoodsId
     * @param AttrId
     * @param Num
     * @param SessionId
     */
    public void addCartAdd(String GoodsId,String AttrId,String Num,String SessionId){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,"请稍等...","加入购物车...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Cart");
        params.put("fun","CartAdd");
        params.put("GoodsId",GoodsId);
        params.put("AttrId",AttrId);
        params.put("Num",Num);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addCartAdd(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean collectBeans, String status,Object page) {
                        if (collectBeans.getStatus() == 0) {
                            selfGoodsDetailContract.addCartSuccess(collectBeans.getMessage());
                        }else {
                            selfGoodsDetailContract.addCartFail("加入购物车失败"+collectBeans.getMessage());
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfGoodsDetailContract.addCartFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }));
    }

    /**
     * 随机商品
     * @param GoodsId
     * @param SessionId
     */
    public void randomGoods(String GoodsId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Goods");
        params.put("fun","RandomGoodsList");
        params.put("GoodsId",GoodsId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getselfGoodList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<SelfGoodsBean>,PageBean>() {
                    @Override
                    public void onResponse(ArrayList<SelfGoodsBean> selfGoodsBeans, String status,PageBean page) {
                        selfGoodsDetailContract.getCommendGoodsSuccess(selfGoodsBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfGoodsDetailContract.getCommendGoodsFail(msg);
                    }
                }));
    }

    /**
     * 立即购买
     * @param GoodsId
     * @param AttrId
     * @param Num
     * @param SessionId
     */
    public void rightNowBuy(String GoodsId,String AttrId,String Num,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Order");
        params.put("fun","Buy");
        params.put("GoodsId",GoodsId);
        params.put("AttrId",AttrId);
        params.put("Num",Num);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.rightNowBuy(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<BuyBean,Object>() {
                    @Override
                    public void onResponse(BuyBean rightNows, String status,Object page) {
                        selfGoodsDetailContract.getRightNowBuySuccess(rightNows);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfGoodsDetailContract.getRightNowBuyFail(msg);
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
                            selfGoodsDetailContract.isAddressSuccess(collectDeleteBean.getMessage());
                        }else if (collectDeleteBean.getStatus() == 101){
                            selfGoodsDetailContract.isAddressFail(collectDeleteBean.getMessage());
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfGoodsDetailContract.isAddressFail(msg);
                    }
                }));
    }

}
