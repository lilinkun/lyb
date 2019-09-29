package com.lzyyd.lyb.wxapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.lzyyd.lyb.interf.IWxLoginListener;
import com.lzyyd.lyb.util.LzyydUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;
    private String unionid;
    private String openid;
    public static int wxType = 0;
    public static IWxLoginListener iWxResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
//        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, LzyydUtil.APP_ID, false);
        iwxapi.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
    }

    private static final int RETURN_MSG_TYPE_SHARE = 2;

    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM){
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseResp;
            String extraData =launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            String str = extraData.substring(extraData.length()-1);
            if (extraData.substring(extraData.length()-1).equals("1")){
                iWxResult.setWxLoginSuccess("");
                Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
                finish();
            }else {
                iWxResult.setWxLoginFail("支付失败");
                finish();
            }
        }

    }


    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    public static void setLoginListener(IWxLoginListener iWxResultListener){
        iWxResult = iWxResultListener;
    }

    public static void wxType(int type){
        wxType = type;
    }

}
