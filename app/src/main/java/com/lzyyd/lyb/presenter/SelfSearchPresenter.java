package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.SelfSearchContract;
import com.lzyyd.lyb.contract.TbAllContract;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.entity.SelfGoodsBean;
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
 * Created by LG on 2018/12/19.
 */

public class SelfSearchPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SelfSearchContract selfSearchContract;

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
        selfSearchContract = (SelfSearchContract) view;
    }

    public void setList(String PageIndex,String PageCount,String adzone_id,String q,String sort,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","TaobaoTbk");
        params.put("fun","dgMaterialOptional");
        params.put("PageIndex",PageIndex);
        params.put("PageCount",PageCount);
        params.put("adzone_id",adzone_id);
        params.put("has_coupon","1");
        params.put("sort",sort);
        params.put("SessionId",SessionId);
        if (q.equals("文体车品")){
            q = "汽车";
        }
        params.put("q",q);

        mCompositeSubscription.add(manager.tbList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<TbjsonBean<ArrayList<TbMaterielBean>>,Object>(){

                    @Override
                    public void onResponse(TbjsonBean<ArrayList<TbMaterielBean>> arrayListTbjsonBean, String status,Object page) {
                        selfSearchContract.onSuccess(arrayListTbjsonBean.getResultList());
//                        homeContract.onSuccess(arrayListTbjsonBean.getResults()));
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfSearchContract.onError(msg);
                    }

                    @Override
                    public void onNext(ResultBean<TbjsonBean<ArrayList<TbMaterielBean>>, Object> result) {
                        super.onNext(result);
                    }
                }));
    }

    public void selfSearch(String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Goods");
        params.put("fun", "Serach_Goods");
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getselfSearch(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<String>,Object>(){

                    @Override
                    public void onResponse(ArrayList<String> selfGoodsBeans, String status,Object page) {
                        selfSearchContract.onSelfSuccess(selfGoodsBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        selfSearchContract.onSelfFail(msg);
                    }

                })
        );
    }
}
