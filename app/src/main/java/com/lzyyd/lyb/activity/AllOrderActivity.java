package com.lzyyd.lyb.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.adapter.OrderAdapter;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.AllOrderContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.CountBean;
import com.lzyyd.lyb.entity.OrderDetailBean;
import com.lzyyd.lyb.entity.WxInfoBean;
import com.lzyyd.lyb.entity.WxRechangeBean;
import com.lzyyd.lyb.interf.IWxResultListener;
import com.lzyyd.lyb.presenter.AllOrderPresenter;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.MessageEvent;
import com.lzyyd.lyb.util.UToast;
import com.lzyyd.lyb.util.UiHelper;
import com.lzyyd.lyb.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by LG on 2018/12/19.
 */

public class AllOrderActivity extends BaseActivity implements AllOrderContract, IWxResultListener {

    @BindView(R.id.order_sn)
    TextView order_sn;
    @BindView(R.id.order_date)
    TextView order_date;
    @BindView(R.id.goods_total_price)
    TextView goods_total_price;
    @BindView(R.id.tv_use_point)
    TextView tv_use_point;
    @BindView(R.id.tv_fare)
    TextView tv_fare;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.rv_order)
    RecyclerView recyclerView;
    @BindView(R.id.tv_consignee_address)
    TextView tv_consignee_address;
    @BindView(R.id.tv_consignee_name)
    TextView tv_consignee_name;
    @BindView(R.id.tv_consignee_phone)
    TextView tv_consignee_phone;
    @BindView(R.id.tv_exit_order)
    TextView tv_exit_order;
    @BindView(R.id.tv_pay_order)
    TextView tv_pay_order;
    @BindView(R.id.tv_order_pay_price)
    TextView tv_order_pay_price;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.rl_order)
    RelativeLayout rl_order;
    @BindView(R.id.ll_price_status)
    LinearLayout ll_price_status;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.tv_pay_style)
    TextView tv_pay_style;
    @BindView(R.id.tv_pay_message)
    TextView tv_pay_message;
    @BindView(R.id.pay_date)
    TextView tv_pay_date;
    @BindView(R.id.send_out_date)
    TextView send_out_date;
    @BindView(R.id.logistics_information)
    TextView logistics_information;

    AllOrderPresenter allOrderPresenter = new AllOrderPresenter();
    private OrderDetailBean orderDetailBean;
    private ArrayList<OrderDetailBean> orderDetailBeans;
    private PopupWindow payPopupWindow;
    private String orderId = "";
    private String orderSn = "";
    private int status = 0;
    private Dialog payDialog ;
    double payid = 0;
    double useIntegral = 0;
    double shipping_fee = 0;
    double order_amount = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_all_order;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
        orderSn = getIntent().getBundleExtra(LzyydUtil.TYPEID).getString("order_sn");
        status = getIntent().getBundleExtra(LzyydUtil.TYPEID).getInt("status");
        order_sn.setText(orderSn);

        allOrderPresenter.attachView(this);
        allOrderPresenter.onCreate(this);

        allOrderPresenter.cartBuy(orderSn, ProApplication.SESSIONID(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        WXPayEntryActivity.setPayListener(this);

        if (status == 1 ){
            tv_pay_style.setText("未发货");
            tv_pay_message.setText("买家已付款，等待发货");
            rl_bottom.setVisibility(View.GONE);
        }else if (status == 2){
            tv_exit_order.setVisibility(View.GONE);
            tv_pay_order.setText("确认收货");
            tv_pay_style.setText("已发货");
            tv_pay_message.setText("您的商品正在运输中");
        }else if (status == 0){
            tv_exit_order.setText("取消订单");
            tv_pay_order.setText("立即付款");
            tv_pay_style.setText("未付款");
        } else if(status == 4){
            tv_pay_style.setText("交易完成");
            tv_pay_message.setText("您的交易已经完成");
            tv_pay_order.setText("删除订单");
            ll_price_status.setVisibility(View.GONE);
            tv_exit_order.setVisibility(View.GONE);
        } else if(status == 5){
            tv_pay_style.setText("交易失效");
            tv_pay_message.setText("");
            rl_bottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @OnClick(R.id.ll_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:

                setResult(RESULT_OK);
                finish();

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            setResult(RESULT_OK);
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setDataSuccess(final ArrayList<OrderDetailBean> orderDetailBeans) {
        this.orderDetailBean = orderDetailBeans.get(0);
        this.orderDetailBeans = orderDetailBeans;
        OrderAdapter orderAdapter = new OrderAdapter(this,orderDetailBeans);
        recyclerView.setAdapter(orderAdapter); //添加自定义分割线

        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);
        order_date.setText(orderDetailBean.getConfirm_time());

        for(int i = 0;i < orderDetailBeans.size();i++){
            if (orderId.equals("")){
                orderId = orderDetailBeans.get(i).getOrder_id();
            }else {
                orderId = orderId + "," + orderDetailBeans.get(i).getOrder_id();
            }
            order_amount += orderDetailBean.getMoney_payid();


            BigDecimal b = new BigDecimal(order_amount);
            order_amount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            useIntegral+= orderDetailBean.getUseIntegral();
            shipping_fee += orderDetailBean.getShipping_fee();
            payid+= orderDetailBean.getOrder_amount();
        }

        goods_total_price.setText("¥"+payid + "");
        tv_use_point.setText("" + useIntegral);
        tv_fare.setText(shipping_fee + "");
        tv_total.setText("¥"+ order_amount + "");
        tv_order_pay_price.setText("¥"+ order_amount+"");

        tv_consignee_address.setText(orderDetailBean.getAddressName() + orderDetailBean.getAddress());
        tv_consignee_name.setText(orderDetailBean.getConsignee());
        tv_consignee_phone.setText(orderDetailBean.getMobile());

        tv_pay_date.setText(orderDetailBean.getPay_time()+"");
        send_out_date.setText(orderDetailBean.getShipping_time()+"");
        logistics_information.setText(orderDetailBean.getLgs_name() + " " + orderDetailBean.getLgs_number());

        tv_exit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ButtonUtils.isFastDoubleClick()) {
                    new AlertDialog.Builder(AllOrderActivity.this).setTitle("温馨提示").setMessage("您确定要取消订单？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            allOrderPresenter.exitOrder(orderId, ProApplication.SESSIONID(AllOrderActivity.this));
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }

            }
        });
        tv_pay_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick()) {
                    tv_pay_order.setClickable(false);
                    if (status == 0) {
                        allOrderPresenter.getOrderData(ProApplication.SESSIONID(AllOrderActivity.this));
                    } else if (status == 2) {
                        allOrderPresenter.sureReceipt(orderDetailBean.getOrder_id(), ProApplication.SESSIONID(AllOrderActivity.this));
                    } else if (status == 4) {
                        allOrderPresenter.deleteOrder(orderDetailBean.getOrder_id(), ProApplication.SESSIONID(AllOrderActivity.this));
                    }
                }
            }
        });

    }

    @Override
    public void setDataFail(String msg) {
        toast(msg);
    }

    @Override
    public void exitOrderSuccess(CollectDeleteBean collectDeleteBean) {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }
        if (collectDeleteBean.getStatus() == 0){
            toast("取消订单成功");
            setResult(RESULT_OK);
            finish();
        }else {
            toast(collectDeleteBean.getMessage());
        }
    }

    @Override
    public void exitOrderFail(String msg) {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }
        toast(msg);
    }

    @Override
    public void InfoAccountSuccess(CountBean orderDetailBean) {
//        showPopup(orderDetailBean);

        Bundle bundle = new Bundle();
        bundle.putString(LzyydUtil.ORDERID,orderSn+"");
        bundle.putString(LzyydUtil.WHERE,"order");
        UiHelper.launcherForResultBundle(AllOrderActivity.this,PayActivity.class,0x1231,bundle);
    }

    @Override
    public void InfoAccountFail(String msg) {
        toast(msg);
    }

    @Override
    public void selfPaySuccess(CollectDeleteBean collectDeleteBean) {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }
        if (collectDeleteBean.getStatus() == 0){
            payDialog.dismiss();
            toast("支付成功");
            setResult(RESULT_OK);
            finish();
        }else {
            toast("支付失败");
        }
    }

    @Override
    public void selfPayFail(String msg) {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }
        toast(msg);
    }

    @Override
    public void sureReceiptSuccess(CollectDeleteBean collectDeleteBean) {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }
        if (collectDeleteBean.getStatus() == 0){
            toast("收货成功");
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void sureReceiptFail(String msg) {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }
        toast(msg);
    }

    @Override
    public void wxInfoSuccess(WxRechangeBean wxRechangeBean) {
        WxInfoBean wxInfoBean = wxRechangeBean.getData();
        LzyydUtil.wxPay(wxInfoBean.getAppid(),wxInfoBean.getPartnerid(),wxInfoBean.getPrepayid(),wxInfoBean.getNoncestr(),wxInfoBean.getTimestamp(),wxInfoBean.getSign(),this);
    }

    @Override
    public void wxInfoFail(String msg) {
        toast(msg);
    }


    public void showPopup(CountBean countBean){
        iv_bg.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_order,null);
        payPopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,true);
        payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        payPopupWindow.setFocusable(true);
        payPopupWindow.setOutsideTouchable(true);
        payPopupWindow.setAnimationStyle(R.style.popwin_anim_style);

        payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if (iv_bg != null){
                    iv_bg.setVisibility(View.GONE);
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.iv_right_delete);
        final CheckBox check_self = view.findViewById(R.id.check_self);
        final CheckBox check_wx = view.findViewById(R.id.check_wx);

        RelativeLayout rl_self = view.findViewById(R.id.rl_self);
        RelativeLayout rl_wx = view.findViewById(R.id.rl_wx);
        TextView tv_price = view.findViewById(R.id.tv_price);
        TextView tv_pay_self = view.findViewById(R.id.tv_pay_self);
        CountdownView tv_time = view.findViewById(R.id.tv_time);
        TextView tv_right_now_pay = view.findViewById(R.id.tv_right_now_pay);
        tv_pay_self.setText(countBean.getAmount() + "");
        String endTime = orderDetailBean.getEffective_Payment_Time();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        try {
            Date str = simpleDateFormat.parse(endTime);
            long effectiveTime = str.getTime();
            Date date = new Date(System.currentTimeMillis());
            long nowTime = date.getTime();

            long time = effectiveTime - nowTime;

//            int now = (int) (System.currentTimeMillis()/1000);
//            tv_time.setCountdownTime((int)(time/1000)-((int) (System.currentTimeMillis()/1000)-now),1+"");
            tv_time.start(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_right_now_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_self.isChecked()){
                    payPopupWindow.dismiss();
                    View view = LayoutInflater.from(AllOrderActivity.this).inflate(R.layout.dialog_pay,null);

                    payDialog = new Dialog(AllOrderActivity.this);
                    payDialog.setContentView(view);
                    payDialog.show();

                    payDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });

                    final EditText editText = (EditText) view.findViewById(R.id.et_pay_psd);
                    TextView textView = (TextView) view.findViewById(R.id.tv_pay_price);
                    Button btn_sure = (Button) view.findViewById(R.id.btn_sure);

                    textView.setText(order_amount+"");

                    btn_sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editText.getText().toString().isEmpty()){
                                allOrderPresenter.selfPay(editText.getText().toString(),orderSn,ProApplication.SESSIONID(AllOrderActivity.this));
                            }
                        }
                    });


                }else if (check_wx.isChecked()){
                    payPopupWindow.dismiss();
//                    toast("你瞅我干啥，暂时不能微信支付类");
                    allOrderPresenter.setWxPay(orderDetailBean.getOrder_sn(),order_amount+"","29","1","Android","com.lzyyd.lyb",ProApplication.SESSIONID(AllOrderActivity.this));


                }else {
                    toast("请选择支付方式");
                }
            }
        });

        tv_price.setText("¥" + order_amount+"");

        rl_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_self.setChecked(true);
                check_wx.setChecked(false);
            }
        });

        rl_wx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                check_wx.setChecked(true);
                check_self.setChecked(false);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                payPopupWindow.dismiss();
            }
        });

        payPopupWindow.showAtLocation(rl_order, Gravity.CENTER | Gravity.CENTER, 0, 0);
    }

    @Override
    public void setWxSuccess() {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }
        if (payDialog!= null && payDialog.isShowing()) {
            payDialog.dismiss();
        }
            toast("支付成功");
            setResult(RESULT_OK);
            finish();

    }

    @Override
    public void setWxFail() {
        if (!tv_pay_order.isClickable()){
            tv_pay_order.setClickable(true);
        }

        toast("支付失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 0x1231){
            finish();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
