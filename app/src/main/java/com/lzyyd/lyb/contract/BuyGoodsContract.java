package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/4.
 */

public interface BuyGoodsContract extends IView {
    public void collectSuccess(CollectDeleteBean collectDeleteBean);
    public void collectFail(String msg);

    public void exReChangeSuccess(String msg);
    public void exReChangeFail(String msg);

}
