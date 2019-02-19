package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/26.
 */

public interface MyNickNameContract extends IView {
    public void modifySuccess();
    public void modifyFail(String msg);
}
