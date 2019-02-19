package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.entity.TbMaterielBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/8.
 */

public interface TbAllContract extends IView {
    public void onSuccess(ArrayList<TbMaterielBean> tbShopBeans);
    public void onError(String msg);

}
