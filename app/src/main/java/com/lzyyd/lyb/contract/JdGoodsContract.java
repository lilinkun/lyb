package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.JdGoodsBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/1/31.
 */

public interface JdGoodsContract extends IView {

    public void getDataSuccess(ArrayList<JdGoodsBean> jdGoodsBean);
    public void getDataFail(String msg);

}
