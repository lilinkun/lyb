package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.AddressBean;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/9.
 */

public interface ChooseAddressContract extends IView{
    public void setDataSuccess(ArrayList<AddressBean> addressBeanArrayList);
    public void setDataFail(String msg);

    public void deleteSuccess();
    public void deleteFail(String msg);

    public void isDefaultSuccess(CollectDeleteBean collectDeleteBean);
    public void isDefaultFail(String msg);
}
