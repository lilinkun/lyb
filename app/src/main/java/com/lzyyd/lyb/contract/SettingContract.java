package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.DownloadBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/14.
 */

public interface SettingContract extends IView {

    public void LoginOutSuccess(String msg);
    public void LoginOutFail(String msg);

    public void updateSuccess(DownloadBean downloadBean);
    public void updateFail(String failMsg);
}
