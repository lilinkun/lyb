package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.entity.SelfStoreBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/20.
 */

public interface StoreContract extends IView {
    public void getDataSuccess(ArrayList<SelfStoreBean> selfGoodsBeans);
    public void getDataFail(String msg);
}
