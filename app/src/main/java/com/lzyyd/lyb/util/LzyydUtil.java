package com.lzyyd.lyb.util;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lzyyd.lyb.entity.LoginBean;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LG on 2018/11/10.
 */

public class LzyydUtil {

    public static final int PAGE_HOMEPAGE = 0;
    public static final int PAGE_MALL = 1;
//    public static final int PAGE_HEALTHHOME = 2;
    public static final int PAGE_ME = 2;

    public static  final String PAGE_COUNT = "20";

//    public static final String APP_ID = "wx7b154709878a1cbe";
//    public static final String APP_ID = "wx3686dfb825618610";
    public static final String APP_ID = "wx27fb4ad747521493";


    public static final int GOODS_ALL_WEAR = 3756;
    public static final int GOODS_WOMAN_WEAR = 3767;
    public static final int GOODS_HOUSE = 3758;
    public static final int GOODS_DIGITAL = 3759;
    public static final int GOODS_SHOES = 3762;
    public static final int GOODS_MAKEUP = 3763;
    public static final int GOODS_MAN_WEAR = 3764;
    public static final int GOODS_UNDERWEAR = 3765;
    public static final int GOODS_MOTHER = 3760;
    public static final int GOODS_FOOD = 3761;
    public static final int GOODS_MOTION = 3766;

    public static final String TYPEID = "TYPEID";
    public static final String LOGIN = "login";
    public static final String ORDERID = "orderid";
    public static final String ORDERAMOUNT = "orderamount";
    public static final String WHERE = "where";

    public static String RESULT_SUCCESS = "success";
    public static String RESULT_FAIL = "fail";

    public static String[] strs = {"tk_total_sales_des","total_sales_des","price_des","price_asc","tk_total_commi_des","tk_total_commi_asc"};


    public static final void setInputMethod(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 序列化对象
     * @param loginBean
     * @return
     * @throws IOException
     */
    public static String serialize(LoginBean loginBean) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(loginBean);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 序列化对象
     * @return
     * @throws IOException
     */
    public static String serialize(Serializable t) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(t);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static LoginBean deSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        LoginBean loginBean = (LoginBean) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Serializable unSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Serializable loginBean = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    public static void wxPay(String appid,String partnerId,String prepayId,String nonceStr,String timeStamp,String sign,Context context) {
        Toast.makeText(context, "获取订单中...", Toast.LENGTH_SHORT).show();
        IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(appid);
        try {
//            org.json.JSONObject json = new org.json.JSONObject(result);
//            if (null != json && !json.has("retcode")) {
            PayReq req = new PayReq();
            req.appId = appid;
            req.partnerId = partnerId;
            req.prepayId = prepayId;
            req.nonceStr = nonceStr;
            req.timeStamp = timeStamp;
            req.packageValue = "Sign=WXPay";
            req.sign = sign;
            req.extData = "app data";
            api.sendReq(req);
//            } else {
//                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
//                Toast.makeText(this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(context, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void wxProgramPay(String appid,Context context,String page){
        IWXAPI api = WXAPIFactory.createWXAPI(context, appid);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_236e7c5bd9d8";
        req.path = page;
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        api.sendReq(req);

    }

    public static String getCurDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String curDate = simpleDateFormat.format(date);
        return curDate;
    }

    public static String redecuStr(String total,String reduce){
        String dd = "";
        if(total.contains(reduce)){
            dd = total.substring(0,total.indexOf(reduce));
            dd = dd + total.substring(total.indexOf(reduce)+reduce.length(),total.length());
        }
        return dd;
    }

}
