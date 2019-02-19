package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.LoginBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/11/30.
 */

public interface LoginContract extends IView{
    public void onLoginSuccess(LoginBean loginBean);
    public void onLoginFail(String msg);

    public void isRegisterSuccess(boolean isRegister);
    public void isRegisterFail(String msg);
}
