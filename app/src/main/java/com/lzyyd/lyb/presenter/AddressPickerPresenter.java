package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.AddAddressContract;
import com.lzyyd.lyb.contract.AddressPickerContract;
import com.lzyyd.lyb.entity.ProvinceBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/10.
 */

public class AddressPickerPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private AddressPickerContract addressPickerContract;

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
        addressPickerContract = (AddressPickerContract) view;
    }

    public void getLocalData(String parentId,final int localType){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Dict_Region");
        params.put("fun","DictRegionList");
        params.put("parentId",parentId);
        mCompositeSubscription.add(manager.getLocalData(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<ProvinceBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<ProvinceBean> provinceBeans, String status) {
                        addressPickerContract.getDataSuccess(provinceBeans,localType);
                    }

                    @Override
                    public void onErr(String msg, String status) {

                    }
                }));
    }
}
