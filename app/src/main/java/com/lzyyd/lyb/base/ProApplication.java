package com.lzyyd.lyb.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.kepler.jd.Listener.AsyncInitListener;
import com.kepler.jd.login.KeplerApiManager;
import com.lzyyd.lyb.util.DeviceData;

/**
 * Created by LG on 2018/11/29.
 */

public class ProApplication extends Application{
    private static Context mContext;
    private static ProApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        KeplerApiManager.asyncInitSdk(ProApplication.this, "b101e0a5049146d8abd7186eca381882", "359e3bae47a94afc82fefb7eeae46d5e", new AsyncInitListener() {
            @Override
            public void onSuccess() {
                Log.e("kepler","kepler asyncInitSdk onSuccess");
            }

            @Override
            public void onFailure() {
                Log.e("Kepler",
                        "Kepler asyncInitSdk 授权失败，请检查lib 工程资源引用；包名,签名证书是否和注册一致");
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static ProApplication getInstance() {
        return instance;
    }


    public static String SESSIONID(Context mContext){
      return  "lyb" + DeviceData.getUniqueId(mContext);
    }

    public static String HEADIMG = "";

    public static String BANNERIMG = "";

    public static synchronized ProApplication context() {
        return (ProApplication) mContext;
    }
}
