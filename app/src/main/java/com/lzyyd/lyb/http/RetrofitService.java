package com.lzyyd.lyb.http;

import com.lzyyd.lyb.entity.AddressBean;
import com.lzyyd.lyb.entity.AmountPriceBean;
import com.lzyyd.lyb.entity.BuyBean;
import com.lzyyd.lyb.entity.Category1Bean;
import com.lzyyd.lyb.entity.CategoryListBean;
import com.lzyyd.lyb.entity.CollectBean;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.CountBean;
import com.lzyyd.lyb.entity.DownloadBean;
import com.lzyyd.lyb.entity.FareBean;
import com.lzyyd.lyb.entity.FaresBean;
import com.lzyyd.lyb.entity.GoodsDetailBean;
import com.lzyyd.lyb.entity.HomeCategoryBean;
import com.lzyyd.lyb.entity.HomeHeadBean;
import com.lzyyd.lyb.entity.IntegralBean;
import com.lzyyd.lyb.entity.JdGoodsBean;
import com.lzyyd.lyb.entity.LoginBean;
import com.lzyyd.lyb.entity.OrderBean;
import com.lzyyd.lyb.entity.OrderDetailBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.entity.PageBean;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.entity.ProvinceBean;
import com.lzyyd.lyb.entity.ResultBean;
import com.lzyyd.lyb.entity.RushBuyBean;
import com.lzyyd.lyb.entity.SelfGoodsBean;
import com.lzyyd.lyb.entity.SelfOrderBean;
import com.lzyyd.lyb.entity.SelfStoreBean;
import com.lzyyd.lyb.entity.TbMaterielBean;
import com.lzyyd.lyb.entity.TbjsonBean;
import com.lzyyd.lyb.entity.UrlBean;
import com.lzyyd.lyb.entity.WxRechangeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by LG on 2018/11/7.
 */

public interface RetrofitService {

//    @FormUrlEncoded
//    @POST("LoginDateServlet")
//    Observable<HashMap<String,String>> setTest(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<TbjsonBean<ArrayList<TbMaterielBean>>, Object>> tbApi(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<UrlBean, Object>> getUrl(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> modifyPsd(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<Boolean,Object>> isRegister(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<String,Object>> loginout(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<DownloadBean,Object>> update(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> opinion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<PersonalInfoBean,Object>> getInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> getCollect(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> getNewUrl(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> getData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<String,Object>> isExChange(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<HomeHeadBean,Object>> getFlash(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<CategoryListBean<ArrayList<Object>>>,Object>> getCategory(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<GoodsDetailBean<ArrayList>,Object>> getSelfGoodDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectBean,Object>> addGoodCollect(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<String,Object>> isGoodCollect(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<RushBuyBean>,Object>> rushBuy(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<CollectBean>,PageBean>> GoodCollectList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> getMobile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<SelfGoodsBean>,PageBean>> getselfGoodList(@FieldMap Map<String, String> params);
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<String>,Object>> getselfSearch(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<SelfStoreBean>,Object>> getSelfStoreGoodsList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> getIsAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<SelfOrderBean>,Object>> getSelfOrderList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> exitOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> addCartAdd(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<BuyBean,Object>> rightNowBuy(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<FareBean,Object>> getfare(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<FaresBean>,Object>> getfares(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> DeleteCollectGood(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> sureOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> selfPay(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<WxRechangeBean,Object>> wxPay(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> sureReceipt(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> deleteOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> isDefault(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> modifyOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> deleteGoods(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CountBean,Object>> getAccountInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<AddressBean>,Object>> getConsigneeAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<ProvinceBean>,Object>> getLocalData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> getSaveAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> getDeleteAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<OrderListBean<ArrayList<OrderBean>>> ,Object>> getCartList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<OrderDetailBean>,Object>> getOrderDetail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<IntegralBean,Object>> getShoppingPrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<JdGoodsBean>,Object>> getJdGoods(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<AmountPriceBean,Object>> getAmountPrice(@FieldMap Map<String, String> params);

    /**
     * 上传图片
     * @return
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<CollectDeleteBean,Object>> uploadImage(@FieldMap Map<String, String> params);

    /**
     * 上传头像
     */
    @Multipart
    @POST("/member/uploadMemberIcon.do")
    Call<Result<String>> uploadMemberIcon(@Part List<MultipartBody.Part> partList,@FieldMap Map<String,String> params);

}
