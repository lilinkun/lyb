package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/2.
 */

public interface OpinionContract extends IView {
    public void onUploadSuccess();
    public void onFail(String msg);
}
