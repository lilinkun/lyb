package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.ProvinceBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/12/10.
 */

public interface AddAddressContract extends IView {
    public void getDataSuccess(ArrayList<ProvinceBean> provinceBeans,int id);
    public void getDataFail(String msg);
    public void getSaveSuccess();
    public void getSaveFail(String msg);
    public void modifySuccess();
    public void modifyFail(String msg);
}
