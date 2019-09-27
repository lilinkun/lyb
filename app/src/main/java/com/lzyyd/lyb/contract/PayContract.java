package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.OrderDetailBean;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.entity.WxRechangeBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/9/10.
 */
public interface PayContract extends IView {

    /**
     * 余额支付
     */
    public void selfPaySuccess(CollectDeleteBean collectDeleteBean);
    public void selfPayFail(String msg);


    public void getInfoSuccess(PersonalInfoBean personalInfoBean);
    public void getInfoFail(String msg);

    public void setDataSuccess(ArrayList<OrderDetailBean> orderDetailBeans);
    public void setDataFail(String msg);


    public void wxInfoSuccess(CollectDeleteBean wxRechangeBean);
    public void wxInfoFail(String msg);
}
