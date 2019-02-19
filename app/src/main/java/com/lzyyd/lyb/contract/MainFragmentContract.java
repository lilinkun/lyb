package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.DownloadBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by Administrator on 2018/12/31.
 */

public interface MainFragmentContract extends IView {
    public void getUpdateSuccess(DownloadBean downloadBean);
    public void getUpdateFail(String msg);
}
