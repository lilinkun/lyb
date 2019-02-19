package com.lzyyd.lyb.interf;

import com.lzyyd.lyb.entity.SelfOrderBean;

/**
 * Created by LG on 2018/12/20.
 */

public interface IPayOrderClickListener {
    public void payMode(SelfOrderBean selfOrderBean , int mode);
    public void SureReceive(String orderId);
    public void getQrcode(String orderId);
}
