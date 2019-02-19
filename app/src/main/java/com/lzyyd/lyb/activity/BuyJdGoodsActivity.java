package com.lzyyd.lyb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.entity.JdGoodsBean;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import jd.union.open.goods.jingfen.query.response.JFGoodsResp;

/**
 * Created by LG on 2019/1/31.
 */

public class BuyJdGoodsActivity extends BaseActivity {

    @BindView(R.id.img_big_icon)
    ImageView mImgBigIcon;
    @BindView(R.id.img_small_icon)
    ImageView mImgSmallIcon;
    @BindView(R.id.tx_title)
    TextView mTvTitle;
    @BindView(R.id.tx_pay_money)
    TextView mTvMoney;
    @BindView(R.id.tx_ord_price)
    TextView mOrdPrice;
    @BindView(R.id.tx_quan_price)
    TextView mCouponPrice;
    @BindView(R.id.tx_tb_money)
    TextView mTbMoney;
    @BindView(R.id.tx_msg_about)
    TextView mTvMsgAbout;
    @BindView(R.id.expiry_date)
    TextView mTvExpiryDate;
    @BindView(R.id.quan_number)
    TextView mTvCoupon;
    @BindView(R.id.tv_goods_volume)
    TextView mTvGoodsVolume;
    @BindView(R.id.tx_TB)
    TextView mTvTb;
    @BindView(R.id.iv_goods_back)
    ImageView mIvGoodsBack;
    @BindView(R.id.rv_small_pic)
    RecyclerView recyclerView;
    @BindView(R.id.tx_more_picture)
    TextView moreText;
    @BindView(R.id.iv_up_down)
    ImageView mUpAndDown;
    @BindView(R.id.img_iscollect)
    ImageView mIsCollect;
    @BindView(R.id.tx_go_TB)
    TextView mTvGoTb;
    @BindView(R.id.tx_after_quan)
    TextView tx_after_quan;

    JdGoodsBean jdGoodsBean = null;

    KelperTask mKelperTask;
    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_goods;
    }

    @Override
    public void initEventAndData() {

        Eyes.translucentStatusBar(this);
        jdGoodsBean = (JdGoodsBean) getIntent().getBundleExtra(LzyydUtil.TYPEID).getSerializable("jdgoods");
        mTvTitle.setText(jdGoodsBean.getSkuName());
        tx_after_quan.setText(R.string.price_jd);
        Picasso.with(this).load(jdGoodsBean.getImageInfo().getImageList()[0].getUrl()).into(mImgBigIcon);

        Date date = new Date(jdGoodsBean.getCouponInfo().getCouponList()[0].getUseEndTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mTvExpiryDate.setText("有效期至：" + simpleDateFormat.format(date));
        mTvCoupon.setText(jdGoodsBean.getCouponInfo().getCouponList()[0].discount + "元券");
        mTvGoTb.setText(R.string.go_Jd);

        double price = jdGoodsBean.getPriceInfo().getPrice();

        mOrdPrice.setText(price + "");

        if (jdGoodsBean.getCouponInfo().getCouponList()[0].getQuota() >= price){
            price = price - jdGoodsBean.getCouponInfo().getCouponList()[0].getDiscount();
        }

        mTvMoney.setText(price + "");
    }

    @OnClick({R.id.tx_TB,R.id.iv_goods_back,R.id.tx_go_TB})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tx_TB:

                mKelperTask = KeplerApiManager.getWebViewService().openAppWebViewPage(this, jdGoodsBean.getCouponInfo().getCouponList()[0].getLink(), mKeplerAttachParameter, mOpenAppAction);

                break;

            case R.id.iv_goods_back:

                finish();

                break;

            case R.id.tx_go_TB:

                if (jdGoodsBean.getMaterialUrl().startsWith("http://")) {
                    mKelperTask = KeplerApiManager.getWebViewService().openAppWebViewPage(this, jdGoodsBean.getMaterialUrl(), mKeplerAttachParameter, mOpenAppAction);
                }else {
                    mKelperTask = KeplerApiManager.getWebViewService().openAppWebViewPage(this, "http://" + jdGoodsBean.getMaterialUrl(), mKeplerAttachParameter, mOpenAppAction);
                }

                break;
        }
    }

    private OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status,String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (status == OpenAppAction.OpenAppAction_result_APP){
                    }else if (status == OpenAppAction.OpenAppAction_result_NoJDAPP){
                        toast("没有安装京东app");
                    }else if (status == OpenAppAction_result_BlackUrl){
                        toast("不在白名单");
                    }
                }
            });
        }
    };
}
