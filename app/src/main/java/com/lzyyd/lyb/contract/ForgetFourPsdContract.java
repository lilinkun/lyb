package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.LoginBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/27.
 */

public interface ForgetFourPsdContract extends IView {
    public void onModifyPsdSuccess(String message);
    public void onModiftPsdFail(String msg);

    public void onLoginSuccess(LoginBean loginBean);
    public void onLoginFail(String msg);
}
