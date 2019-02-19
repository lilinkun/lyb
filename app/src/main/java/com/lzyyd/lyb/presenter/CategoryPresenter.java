package com.lzyyd.lyb.presenter;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.lzyyd.lyb.contract.CategoryContract;
import com.lzyyd.lyb.entity.Category1Bean;
import com.lzyyd.lyb.entity.CategoryListBean;
import com.lzyyd.lyb.entity.GoodsDetailBean;
import com.lzyyd.lyb.entity.HomeCategoryBean;
import com.lzyyd.lyb.entity.HomeHeadBean;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;
import com.lzyyd.lyb.util.UToast;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/7.
 */

public class CategoryPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private CategoryContract categoryContract;

    @Override
    public void onCreate(Context context) {
        this.mContext = context;
        manager = new DataManager(context);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void attachView(IView view) {
        categoryContract = (CategoryContract) view;
    }


    public void getCategoryList(String SessionId){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("cls","Category");
        hashMap.put("fun","CategoryList");
        hashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getCategory(hashMap)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new HttpResultCallBack<ArrayList<CategoryListBean<ArrayList<Object>>>, Object>() {

            @Override
            public void onResponse(ArrayList<CategoryListBean<ArrayList<Object>>> categoryListBeans, String status) {

                categoryContract.getDataSuccess(categoryListBeans);
                /*if (homeHeadBeanCategoryListBean.get(0).getSubclass().size() > 0){
                    ArrayList<Object> getSubclass = (ArrayList<Object>)homeHeadBeanCategoryListBean.get(0).getSubclass();
                    Gson gson = new Gson();
                    for (int i = 0; i < getSubclass.size();i++) {
                        String str = gson.toJson(getSubclass.get(i));
                        HomeCategoryBean homeCategoryBean = gson.fromJson(str, HomeCategoryBean.class);
                        UToast.show(mContext,homeCategoryBean.getCat_id() + "");
                    }
                }*/
            }

            @Override
            public void onErr(String msg, String status) {
                UToast.show(mContext,msg);
            }

            @Override
            public void onNext(ResultBean<ArrayList<CategoryListBean<ArrayList<Object>>>, Object> result) {
                super.onNext(result);
            }
        }));
    }
}
