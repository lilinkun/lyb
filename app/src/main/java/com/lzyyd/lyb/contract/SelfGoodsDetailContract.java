package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.BuyBean;
import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.entity.GoodsDetailBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/8.
 */

public interface SelfGoodsDetailContract extends IView {
    public void getDataSuccess(GoodsDetailBean<ArrayList> goodsDetailBean);
    public void getDataFail(String msg);

    public void addCollectSuccess(CollectBean collectBean);
    public void addCollectFail(String msg);

    public void isGoodsCollectSuccess(String msg);

    public void deleteCollectSuccess(String msg);

    public void addCartSuccess(String msg);
    public void addCartFail(String msg);


    public void getCommendGoodsSuccess(ArrayList<SelfGoodsBean> selfGoodsBean);
    public void getCommendGoodsFail(String msg);

    public void getRightNowBuySuccess(BuyBean orderListBeans);
    public void getRightNowBuyFail(String msg);

    public void isAddressSuccess(String msg);
    public void isAddressFail(String msg);
}
