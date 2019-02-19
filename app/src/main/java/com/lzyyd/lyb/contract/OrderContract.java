package com.lzyyd.lyb.contract;

import android.view.View;

import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.OrderBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/14.
 */

public interface OrderContract extends IView {

    public void OrderListSuccess(ArrayList<OrderListBean<ArrayList<OrderBean>>> orderListBeans);
    public void OrderListFail(String msg);

    public void modifyOrderSuccess(CollectDeleteBean collectDeleteBean, String num, View view);
    public void modifyOrderFail(String msg);

    public void deleteGoodsSuccess(CollectDeleteBean b);
    public void deleteGoodsFail(String msg);

    public void cartOrderBuySuccess();
    public void cartOrderBuyFail();

    public void isAddressSuccess(String msg);
    public void isAddressFail(String msg);

}
