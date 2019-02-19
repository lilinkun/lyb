package com.lzyyd.lyb.contract;

import com.lzyyd.lyb.entity.HomeHeadBean;
import com.lzyyd.lyb.entity.TbMaterielBean;
import com.lzyyd.lyb.entity.UrlBean;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/11/27.
 */

public interface HomeContract extends IView{
        public void getUrlSuccess(UrlBean urlBean);
        public void getUrlFail(String msg);

        public void onFlashSuccess(HomeHeadBean homeHeadBean);
        public void onFlashFail(String msg);
}
