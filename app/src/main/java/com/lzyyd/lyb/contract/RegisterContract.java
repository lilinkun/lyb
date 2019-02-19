package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/11/30.
 */

public interface RegisterContract extends IView{
    public void onSendVcodeSuccess();
    public void onSendVcodeFail(String msg);

    public void onRegisterSuccess();
    public void onRegisterFail(String msg);
}
