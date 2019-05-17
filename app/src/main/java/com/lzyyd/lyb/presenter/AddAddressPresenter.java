package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.AddAddressContract;
import com.lzyyd.lyb.entity.ProvinceBean;
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
 * Created by LG on 2018/12/10.
 */

public class AddAddressPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private AddAddressContract addAddressContract;

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
        addAddressContract = (AddAddressContract) view;
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
                    public void onResponse(ArrayList<ProvinceBean> provinceBeans, String status,Object page) {
                        addAddressContract.getDataSuccess(provinceBeans,localType);
                    }

                    @Override
                    public void onErr(String msg, String status) {

                    }
                }));
    }

    public void getSaveAddress(String Consignee,String Province,String City,String District,String Address,String ZipCode,String Mobile,String IsDefault,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserAddress");
        params.put("fun","UserAddressCreate");
        params.put("Consignee",Consignee);
        params.put("Province",Province);
        params.put("City",City);
        params.put("District",District);
        params.put("Address",Address);
        params.put("ZipCode",ZipCode);
        params.put("Mobile",Mobile);
        params.put("IsDefault",IsDefault);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getSaveAddress(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {

                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        addAddressContract.getSaveSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addAddressContract.getSaveFail(msg);
                    }
                }));
    }

    /**
     * 修改地址
     */
    public void modifyAddress(String AddressId,String Consignee,String Province,String City,String District,String Address,String ZipCode,String Mobile,String IsDefault,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserAddress");
        params.put("fun","UserAddressUpdate");
        params.put("Consignee",Consignee);
        params.put("AddressId",AddressId);
        params.put("Province",Province);
        params.put("City",City);
        params.put("District",District);
        params.put("Address",Address);
        params.put("ZipCode",ZipCode);
        params.put("Mobile",Mobile);
        params.put("IsDefault",IsDefault);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getSaveAddress(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {

                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        addAddressContract.modifySuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addAddressContract.modifyFail(msg);
                    }
                }));
    }
}
