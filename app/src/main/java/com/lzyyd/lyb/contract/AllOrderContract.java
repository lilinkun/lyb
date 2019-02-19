package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.CountBean;
import com.lzyyd.lyb.entity.OrderDetailBean;
import com.lzyyd.lyb.entity.WxRechangeBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/21.
 */

public interface AllOrderContract extends IView {
    public void setDataSuccess(ArrayList<OrderDetailBean> orderDetailBeans);
    public void setDataFail(String msg);

    public void exitOrderSuccess(CollectDeleteBean collectDeleteBean);
    public void exitOrderFail(String smg);

    public void InfoAccountSuccess(CountBean orderDetailBean);
    public void InfoAccountFail(String msg);

    public void selfPaySuccess(CollectDeleteBean collectDeleteBean);
    public void selfPayFail(String msg);

    public void sureReceiptSuccess(CollectDeleteBean collectDeleteBean);
    public void sureReceiptFail(String msg);

    public void wxInfoSuccess(WxRechangeBean wxRechangeBean);
    public void wxInfoFail(String msg);
}
