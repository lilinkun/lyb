package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.mvp.IView;

/**
 * Created by LG on 2018/12/3.
 */

public interface PersonalInfoContract extends IView{
    public void modifySuccess();
    public void modifyFail(String msg);
    public void getInfoSuccess(PersonalInfoBean loginBean);

    public void uploadImageSuccess(CollectDeleteBean collectDeleteBean);
    public void uploadImageFail(String msg);
}
