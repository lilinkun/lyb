package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/12.
 */

public interface SelfGoodsContract extends IView {

    public void getDataSuccess(ArrayList<SelfGoodsBean> selfGoodsBeans,boolean page);
    public void getDataFail(String msg);
}
