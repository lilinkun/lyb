package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.ChooseAddressContract;
import com.lzyyd.lyb.contract.HomeContract;
import com.lzyyd.lyb.entity.AddressBean;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.TbMaterielBean;
import com.lzyyd.lyb.entity.TbjsonBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/9.
 */

public class ChooseAddressPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ChooseAddressContract chooseAddressContract;

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
        chooseAddressContract = (ChooseAddressContract) view;
    }

    /**
     * 获取收货地址
     * @param PageIndex
     * @param PageCount
     * @param SessionId
     */
    public void getAddress(String PageIndex,String PageCount,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserAddress");
        params.put("fun","UserAddressList");
        params.put("PageIndex",PageIndex);
        params.put("PageCount",PageCount);
        params.put("SessionId",SessionId);

        mCompositeSubscription.add(manager.getConsigneeAddress(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<AddressBean>, Object>() {

                    @Override
                    public void onResponse(ArrayList<AddressBean> addressBeans, String status) {
                            chooseAddressContract.setDataSuccess(addressBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                            chooseAddressContract.setDataFail(msg);
                    }
                })
        );
    }


    /**
     * 删除地址
     * @param userAddressId
     */
    public void deletAddress(String userAddressId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserAddress");
        params.put("fun","UserAddressDelete");
        params.put("userAddressId",userAddressId);
        params.put("SessionId",SessionId);

        mCompositeSubscription.add(manager.getDeleteAddress(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {

                    @Override
                    public void onResponse(Object o, String status) {
                        chooseAddressContract.deleteSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        chooseAddressContract.deleteFail(msg);
                    }
                })
        );
    }


    /**
     * 设置默认地址
     * @param UserAddressId
     * @param SessionId
     */
    public void isDefault(String UserAddressId ,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserAddress");
        params.put("fun","UpdateUserAddressIsdefault");
        params.put("UserAddressId",UserAddressId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.isDefault(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean ,Object>() {

                    @Override
                    public void onResponse(CollectDeleteBean collectDeleteBean, String status) {
                        chooseAddressContract.isDefaultSuccess(collectDeleteBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        chooseAddressContract.isDefaultFail(msg);
                    }
                }));
    }
}
