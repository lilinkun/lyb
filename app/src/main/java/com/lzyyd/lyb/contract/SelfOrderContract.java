package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.SelfOrderBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/18.
 */

public interface SelfOrderContract extends IView {
    public void getDataSuccess(ArrayList<SelfOrderBean> selfOrderBeans);
    public void getDataFail(String msg);

    public void exitOrderSuccess(CollectDeleteBean collectDeleteBean);
    public void exitOrderFail(String smg);
}
