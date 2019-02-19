package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.RushBuyBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/1/2.
 */

public interface RushBuyContract extends IView{
    public void getDataSuccess(ArrayList<RushBuyBean> rushBuyBeans);
    public void getDataFail(String msg);
}
