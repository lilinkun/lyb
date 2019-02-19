package com.lzyyd.lyb.presenter;

import android.content.Context;
import android.content.Intent;

import com.lzyyd.lyb.contract.PersonalInfoContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.http.callback.HttpResultCallBack;
import com.lzyyd.lyb.manager.DataManager;
import com.lzyyd.lyb.mvp.IView;
import com.lzyyd.lyb.util.ApiUtil;
import com.lzyyd.lyb.util.FileImageUpload;
import com.lzyyd.lyb.util.UToast;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/12/3.
 */

public class PersonalInfoPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private PersonalInfoContract personalInfoContract;


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

    }

    @Override
    public void attachView(IView view) {
        personalInfoContract = (PersonalInfoContract) view;
    }

    public void modifyInfo(String account,String session){
    }

    /**
     * 获取个人信息
     * @param session
     */
    public void getInfo(String session){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserBase");
        params.put("fun","UserBasedetailById");
        params.put("SessionId",session);
        mCompositeSubscription.add(manager.getInfo(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new HttpResultCallBack<PersonalInfoBean,Object>() {

            @Override
            public void onResponse(PersonalInfoBean loginBean, String status) {
                personalInfoContract.getInfoSuccess(loginBean);
            }

            @Override
            public void onErr(String msg, String status) {

            }

            @Override
            public void onNext(ResultBean<PersonalInfoBean,Object> o) {
                super.onNext(o);
            }

        }));
    }

    public void uploadImage(String HeadPic,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserBase");
        params.put("fun","UploadHeadPic");
        params.put("HeadPic", HeadPic);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getHeadInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<CollectDeleteBean,Object>() {
                    @Override
                    public void onResponse(CollectDeleteBean o, String status) {
                        personalInfoContract.uploadImageSuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        personalInfoContract.uploadImageFail(msg);
                    }
                }));
    }

    public void modifyInfo(String session){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","UserBase");
        params.put("fun","UserBasedetailById");
        params.put("SessionId",session);
    }


}
