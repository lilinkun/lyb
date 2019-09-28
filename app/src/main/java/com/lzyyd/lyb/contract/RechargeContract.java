package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.CountBean;
import com.lzyyd.lyb.entity.WxRechangeBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2019/1/5.
 */

public interface RechargeContract extends IView{
    public void setReChargeSuccess(CollectDeleteBean reChargeSuccess);
    public void setReChargeFail(String msg);
//    public void setReChargeSuccess(WxRechangeBean reChargeSuccess);
//    public void setReChargeFail(String msg);

    public void InfoAccountSuccess(CountBean countBean);
    public void InfoAccountFail(String msg);
}
