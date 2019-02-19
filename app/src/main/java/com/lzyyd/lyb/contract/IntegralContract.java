package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.AmountPriceBean;
import com.lzyyd.lyb.entity.IntegralBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/29.
 */

public interface IntegralContract extends IView {
    public void getGoodsIntegralSuccess(IntegralBean integralBean);
    public void getGoodsIntegralFail(String msg);

    public void getDataSuccess(AmountPriceBean amountPriceBean);
    public void getDataFail(String msg);
}
