package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.BrowseRecordBean;
import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/11.
 */

public interface CollectContract extends IView {
    public void getCollectDataSuccess(ArrayList<CollectBean> collectBeans,String pageCount);
    public void getCollectFail(String msg);

    public void deleteCollectSuccess(String msg);
    public void deleteCollectFail(String msg);
}
