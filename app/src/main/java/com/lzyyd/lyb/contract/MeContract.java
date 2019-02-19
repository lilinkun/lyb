package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/3.
 */

public interface MeContract extends IView{
    public void getInfoSuccess(PersonalInfoBean personalInfoBean);
    public void getInfoFail(String msg);
}
