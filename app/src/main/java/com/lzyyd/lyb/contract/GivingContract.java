package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2019/1/6.
 */

public interface GivingContract extends IView {
    public void getAccount();
    public void getAccountFail(String msg);

    public void givingIntegralSuccess();
    public void givingIntegralFail(String msg);

    public void getPointSuccess();
    public void getPointFail();
}
