package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/27.
 */

public interface MemberShipContract extends IView{
    public void getDataSuccess(ArrayList<SelfGoodsBean> selfGoodsBeans);
    public void getDataFail(String msg);
}
