package com.lzyyd.lyb.manager;

import android.content.Context;

import com.lzyyd.lyb.entity.AddressBean;
import com.lzyyd.lyb.entity.AmountPriceBean;
import com.lzyyd.lyb.entity.BuyBean;
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
import com.lzyyd.lyb.entity.WxInfoBean;
import com.lzyyd.lyb.entity.WxRechangeBean;
import com.lzyyd.lyb.http.RetrofitHelper;
import com.lzyyd.lyb.http.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import okhttp3.MultipartBody;
import rx.Observable;

/**
 * Created by LG on 2018/11/27.
 */
public class DataManager {
    private RetrofitService mRetrofitService;

    public DataManager(Context context){
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }

    public Observable<ResultBean<TbjsonBean<ArrayList<TbMaterielBean>>, Object>> tbList(HashMap<String,String> hashMap){
        return mRetrofitService.tbApi(hashMap);
    }

    public Observable<ResultBean<UrlBean, Object>> getUrl(HashMap<String,String> hashMap){
        return mRetrofitService.getUrl(hashMap);
    }

    /**
     * 修改支付密码
     * @param mHashMap
     * @return
     */
    public Observable<ResultBean> modifyPsd(HashMap<String, String> mHashMap){
        return mRetrofitService.modifyPsd(mHashMap);
    }

    /**
     * 发送验证码
     * @param mHashMap
     * @return
     */
    public Observable<ResultBean> register(HashMap<String, String> mHashMap){
        return mRetrofitService.register(mHashMap);
    }

    /**
     * 登陆
     * @param mHashMap
     * @return
     */
    public Observable<ResultBean<LoginBean,Object>> login(HashMap<String, String> mHashMap){
        return mRetrofitService.login(mHashMap);
    }

    /**
     * 是否注册
     * @param mHashMap
     */
    public Observable<ResultBean<Boolean,Object>> isRegister(HashMap<String, String> mHashMap){
        return mRetrofitService.isRegister(mHashMap);
    }

 /**
     * 登陆
     * @param mHashMap
     * @return
     */
    public Observable<ResultBean<String,Object>> loginout(HashMap<String, String> mHashMap){
        return mRetrofitService.loginout(mHashMap);
    }


    public Observable<ResultBean<DownloadBean,Object>> update(HashMap<String,String> mHashMap){
        return mRetrofitService.update(mHashMap);
    }

    /**
     * 意见反馈
     */
    public Observable<ResultBean> opinion(HashMap<String, String> mHashMap){
        return mRetrofitService.opinion(mHashMap);
    }

    /**
     * 获取个人信息
     */
    public Observable<ResultBean<PersonalInfoBean,Object>> getInfo(HashMap<String, String> mHashMap){
        return mRetrofitService.getInfo(mHashMap);
    }

    /**
     * 上传头像
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> getHeadInfo(HashMap<String,String> mHashMap){
        return mRetrofitService.uploadImage(mHashMap);
    }

    /**
     * 上传头像
     */
    public Observable<ResultBean> getCollect(HashMap<String,String> mHashMap ){
        return mRetrofitService.getCollect(mHashMap);
    }

    public Observable<ResultBean> getNewUrl(HashMap<String,String> mHashMap ){
        return mRetrofitService.getNewUrl(mHashMap);
    }


    /**
     * 扣除积分
     *
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> getData(HashMap<String,String> mHashMap ){
        return mRetrofitService.getData(mHashMap);
    }
    /**
     * 是否扣除积分
     *
     */
    public Observable<ResultBean<String,Object>> isExChange(HashMap<String,String> mHashMap){
        return mRetrofitService.isExChange(mHashMap);
    }

    /**
     * 获取flash
     */
    public Observable<ResultBean<HomeHeadBean,Object>> getFlash(HashMap<String,String> mHashMap){
        return mRetrofitService.getFlash(mHashMap);
    }

    /**
     * 获取种类
     */
    public Observable<ResultBean<ArrayList<CategoryListBean<ArrayList<Object>>>,Object>> getCategory(HashMap<String,String> mHashMap){
        return mRetrofitService.getCategory(mHashMap);
    }

    /**
     * 获取自营商品详情
     */
    public Observable<ResultBean<GoodsDetailBean<ArrayList>,Object>> getSelfGoodDetail(HashMap<String,String> mHashMap){
        return mRetrofitService.getSelfGoodDetail(mHashMap);
    }

    /**
     * 增加自营商品收藏
     */
    public Observable<ResultBean<CollectBean,Object>> addGoodCollect(HashMap<String,String> mHashMap){
        return mRetrofitService.addGoodCollect(mHashMap);
    }

    /**
     * 是否是自营商品收藏商品
     */
    public Observable<ResultBean<String,Object>> isGoodCollect(HashMap<String,String> mHashMap){
        return mRetrofitService.isGoodCollect(mHashMap);
    }

    /**
     * 抢购
     */
    public Observable<ResultBean<ArrayList<RushBuyBean>,Object>> rushBuy(HashMap<String,String> mHashMap){
        return mRetrofitService.rushBuy(mHashMap);
    }


    /**
     * 获取自营商品收藏列表
     */
    public Observable<ResultBean<ArrayList<CollectBean>,PageBean>> GoodCollectList(HashMap<String,String> mHashMap){
        return mRetrofitService.GoodCollectList(mHashMap);
    }
    /**
     * 根据用户名获取信息
     */
    public Observable<ResultBean> getMobile(HashMap<String,String> mHashMap){
        return mRetrofitService.getMobile(mHashMap);
    }

    /**
     * 删除自营商品收藏列表
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> DeleteCollectGood(HashMap<String,String> mHashMap){
        return mRetrofitService.DeleteCollectGood(mHashMap);
    }

    /**
     * 获取自营商品列表
     */
    public Observable<ResultBean<ArrayList<SelfGoodsBean>,PageBean>> getselfGoodList(HashMap<String,String> mHashMap){
        return mRetrofitService.getselfGoodList(mHashMap);
    }

    /**
     * 搜索推荐
     */
    public Observable<ResultBean<ArrayList<String>,Object>> getselfSearch(HashMap<String,String> mHashMap){
        return mRetrofitService.getselfSearch(mHashMap);
    }

    /**
     * 获取店铺列表
     */
    public Observable<ResultBean<ArrayList<SelfStoreBean>,Object>> getSelfStoreGoodsList(HashMap<String,String> mHashMap){
        return mRetrofitService.getSelfStoreGoodsList(mHashMap);
    }

    /**
     * 判断是否有地址
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> getIsAddress(HashMap<String,String> mHashMap){
        return mRetrofitService.getIsAddress(mHashMap);
    }


    /**
     * 获取订单列表
     */
    public Observable<ResultBean<ArrayList<SelfOrderBean>,Object>> getSelfOrderList(HashMap<String,String> mHashMap){
        return mRetrofitService.getSelfOrderList(mHashMap);
    }

    /**
     * 删除订单
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> exitOrder(HashMap<String,String> mHashMap){
        return mRetrofitService.exitOrder(mHashMap);
    }


    /**
     * 增加购物车
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> addCartAdd(HashMap<String,String> mHashMap){
        return mRetrofitService.addCartAdd(mHashMap);
    }

    /**
     * 获取收货地址
     */
    public Observable<ResultBean<ArrayList<AddressBean>,Object>> getConsigneeAddress(HashMap<String,String> mHashMap){
        return mRetrofitService.getConsigneeAddress(mHashMap);
    }

    /**
     * 获取收货区域
     */
    public Observable<ResultBean<ArrayList<ProvinceBean>,Object>> getLocalData(HashMap<String,String> mHashMap){
        return mRetrofitService.getLocalData(mHashMap);
    }

    /**
     * 增加收货地址
     */
    public Observable<ResultBean> getSaveAddress(HashMap<String,String> mHashMap){
        return mRetrofitService.getSaveAddress(mHashMap);
    }

    /**
     * 增加收货地址
     */
    public Observable<ResultBean> getDeleteAddress(HashMap<String,String> mHashMap){
        return mRetrofitService.getDeleteAddress(mHashMap);
    }


    /**
     *　设置默认地址
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> isDefault(HashMap<String,String> mHashMap){
        return mRetrofitService.isDefault(mHashMap);
    }

    /**
     * 获取购物车列表
     */
    public Observable<ResultBean<ArrayList<OrderListBean<ArrayList<OrderBean>>> ,Object>> getCartList(HashMap<String,String> mHashMap){
        return mRetrofitService.getCartList(mHashMap);
    }

    /**
     * 获取订单详情
     */
    public Observable<ResultBean<ArrayList<OrderDetailBean>,Object>> getOrderDetail(HashMap<String,String> mHashMap){
        return mRetrofitService.getOrderDetail(mHashMap);
    }

    /**
     * 获取购物积分
     */
    public Observable<ResultBean<IntegralBean,Object>> getShoppingPrice(HashMap<String,String> mHashMap){
        return mRetrofitService.getShoppingPrice(mHashMap);
    }

    /**
     * 获取京东商品
     */
    public Observable<ResultBean<ArrayList<JdGoodsBean>,Object>> getJdGoods(HashMap<String,String> mHashMap){
        return mRetrofitService.getJdGoods(mHashMap);
    }

    /**
     * 获取购物金额
     */
    public Observable<ResultBean<AmountPriceBean,Object>> getAmountPrice(HashMap<String,String> mHashMap){
        return mRetrofitService.getAmountPrice(mHashMap);
    }

    /**
     * 删除购物车
     */
    public Observable<ResultBean<CollectDeleteBean ,Object>> deleteGoods(HashMap<String,String> mHashMap){
        return mRetrofitService.deleteGoods(mHashMap);
    }

    /**
     * 获取帐户余额
     */
    public Observable<ResultBean<CountBean,Object>> getAccountInfo(HashMap<String,String> mHashMap){
        return mRetrofitService.getAccountInfo(mHashMap);
    }

    /**
     * 获取购物车信息
     */
    public Observable<ResultBean<CollectDeleteBean ,Object>> modifyOrder(HashMap<String,String> mHashMap){
        return mRetrofitService.modifyOrder(mHashMap);
    }

    /**
     * 立即购买
     */
    public Observable<ResultBean<BuyBean,Object>> rightNowBuy(HashMap<String,String> mHashMap){
        return mRetrofitService.rightNowBuy(mHashMap);
    }

    /**
     * 获取单个邮费
     */
    public Observable<ResultBean<FareBean,Object>> getfare(HashMap<String,String> mHashMap){
        return mRetrofitService.getfare(mHashMap);
    }

    /**
     * 获取多个邮费
     */
    public Observable<ResultBean<ArrayList<FaresBean>,Object>> getfares(HashMap<String,String> mHashMap){
        return mRetrofitService.getfares(mHashMap);
    }

    /**
     * 确认订单
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> sureOrder(HashMap<String,String> mHashMap){
        return mRetrofitService.sureOrder(mHashMap);
    }

    /**
     * 余额支付
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> selfPay(HashMap<String,String> mHashMap){
        return mRetrofitService.selfPay(mHashMap);
    }
    /**
     * 微信支付
     */
    public Observable<ResultBean<WxRechangeBean,Object>> wxPay(HashMap<String,String> mHashMap){
        return mRetrofitService.wxPay(mHashMap);
    }
    /**
     * 微信支付1
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> wxPay1(HashMap<String,String> mHashMap){
        return mRetrofitService.wxPay1(mHashMap);
    }

    /**
     * 确认收货
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> sureReceipt(HashMap<String,String> mHashMap){
        return mRetrofitService.sureReceipt(mHashMap);
    }

    /**
     *删除订单
     */
    public Observable<ResultBean<CollectDeleteBean,Object>> deleteOrder(HashMap<String,String> mHashMap){
        return mRetrofitService.deleteOrder(mHashMap);
    }

}
