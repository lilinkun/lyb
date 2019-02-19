package com.lzyyd.lyb.presenter;

import android.content.Context;

import com.lzyyd.lyb.contract.CollectContract;
import com.lzyyd.lyb.contract.HomeContract;
import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.HomeHeadBean;
import com.lzyyd.lyb.entity.ResultBean;
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
 * Created by LG on 2018/12/11.
 */

public class CollectPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private CollectContract collectContract;

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
        collectContract = (CollectContract) view;
    }


    public void getCollectDataList(String PageIndex,String PageCount,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Collect");
        params.put("fun","CollectList");
        params.put("PageIndex",PageIndex);
        params.put("PageCount",PageCount);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.GoodCollectList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<CollectBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<CollectBean> collectBeans, String status) {
                        collectContract.getCollectDataSuccess(collectBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        collectContract.getCollectFail(msg);
                    }
                }));
    }

    public void deleteCollect(String collectId,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Collect");
        params.put("fun","ProjectCheckDelete");
        params.put("collectId",collectId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.DeleteCollectGood(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean collectBeans, String status) {
                        if (collectBeans.getStatus() == 0) {
                            collectContract.deleteCollectSuccess(collectBeans.getMessage());
                        }else {
                            collectContract.deleteCollectFail(collectBeans.getMessage());
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        collectContract.deleteCollectFail(msg);
                    }
                }));
    }
}
