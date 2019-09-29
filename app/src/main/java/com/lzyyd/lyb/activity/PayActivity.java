package com.lzyyd.lyb.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.PayContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.OrderDetailBean;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.interf.IWxLoginListener;
import com.lzyyd.lyb.presenter.PayPresenter;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.UiHelper;
import com.lzyyd.lyb.wxapi.WXEntryActivity;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/9/27.
 */
public class PayActivity extends BaseActivity implements PayContract, IWxLoginListener {
    private PayPresenter payPresenter = new PayPresenter();

    private String orderid;
    private String orderSn;
    private double totalPrice;
    private String whereStr;
    private Dialog payDialog;
    private PopupWindow popupWindow;
    private String point;
    private double infoAmount;
    private double orderAmount;
    private boolean isInfo = false;
    private boolean isOrder = false;
    private boolean isEnough = false;
    private OrderDetailBean orderDetailBean;

    @BindView(R.id.check_wx)
    CheckBox check_wx;
    @BindView(R.id.check_self)
    CheckBox check_self;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_balance_not_enough)
    TextView tv_balance_not_enough;
    @BindView(R.id.ll_pay_order)
    LinearLayout ll_pay_order;
    @BindView(R.id.titlebar)
    CustomTitleBar titlebar;
    @BindView(R.id.tv_point)
    TextView tv_point;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));

        payPresenter.attachView(this);
        payPresenter.onCreate(this);

        WXEntryActivity.setLoginListener(this);

        Bundle bundle = getIntent().getBundleExtra(LzyydUtil.TYPEID);

        if (bundle != null){
            orderid = bundle.getString(LzyydUtil.ORDERID);
            whereStr = bundle.getString(LzyydUtil.WHERE);
        }
        payPresenter.orderDetail(orderid,ProApplication.SESSIONID(this));
        payPresenter.getInfo(ProApplication.SESSIONID(this));

    }

    @OnClick({R.id.rl_wx,R.id.rl_self,R.id.tv_right_now_pay,R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_wx:

                check_wx.setChecked(true);
                check_self.setChecked(false);
                break;

            case R.id.rl_self:

                if (isEnough) {
                    check_wx.setChecked(false);
                    check_self.setChecked(true);
                }

                break;

            case R.id.tv_right_now_pay:

                final SharedPreferences sharedPreferences = getSharedPreferences(LzyydUtil.LOGIN, MODE_PRIVATE);

                if (check_wx.isChecked()) {
                    payPresenter.setWxPay(orderDetailBean.getOrder_sn(),totalPrice+"","30","1","Android","com.lzyyd.lyb",ProApplication.SESSIONID(this));

//                    payPresenter.getWxPayOrderInfo(orderid, sharedPreferences.getString(LzyydUtil.OPENID, ""), totalPrice + "", "11","1",point,ProApplication.SESSIONID(this));

                }else {
//                    toast("暂时不支持余额支付，不要点了");
                    if (tv_balance_not_enough != null && !tv_balance_not_enough.isShown()) {

                        View view1 = LayoutInflater.from(PayActivity.this).inflate(R.layout.dialog_pay,null);

                        payDialog = new Dialog(PayActivity.this);
                        payDialog.setContentView(view1);
                        payDialog.show();

                        payDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });

                        final EditText editText = (EditText) view1.findViewById(R.id.et_pay_psd);
                        TextView textView = (TextView) view1.findViewById(R.id.tv_pay_price);
                        Button btn_sure = (Button) view1.findViewById(R.id.btn_sure);

                        textView.setText(totalPrice+"");

                        btn_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!editText.getText().toString().isEmpty()){
                                    payPresenter.selfPay(editText.getText().toString(),orderSn,ProApplication.SESSIONID(PayActivity.this));
                                }
                            }
                        });
                    }
                }

                break;

            case R.id.ll_back:

                if (whereStr.equals("goods")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("status", 0);
                    bundle.putString("order_sn", orderid);
                    UiHelper.launcherForResultBundle(this, AllOrderActivity.class, 0x0987, bundle);
                }
                setResult(RESULT_OK);
                finish();

                break;

        }
    }

    @Override
    public void selfPaySuccess(CollectDeleteBean collectDeleteBean) {

    }

    @Override
    public void selfPayFail(String msg) {

    }


    public void do_WX(String paramString) {
        loadDialog();
        LzyydUtil.wxProgramPay(LzyydUtil.APP_ID,this,paramString);
    }



    @Override
    public void getInfoSuccess(PersonalInfoBean personalInfoBean) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);

        String wlmCoin = numberFormat.format(personalInfoBean.getBank_data().getAmount());
        tv_balance.setText("余额支付（剩余"+wlmCoin+"）");

        infoAmount = personalInfoBean.getBank_data().getAmount();

        isInfo = true;
        setCheck();
    }

    public void setCheck(){
        if (isInfo && isOrder){
            if (Double.valueOf(totalPrice) <= infoAmount) {
                tv_balance.setTextColor(getResources().getColor(R.color.pay_text));
                tv_balance.setTextSize(16);
                tv_balance_not_enough.setVisibility(View.GONE);

                isEnough = true;
                check_wx.setChecked(false);
                check_self.setChecked(true);
            }else {

                isEnough = false;
                check_wx.setChecked(true);
                check_self.setChecked(false);
            }
        }
    }

    @Override
    public void getInfoFail(String msg) {

    }

    @Override
    public void setDataSuccess(ArrayList<OrderDetailBean> orderDetailBeans) {
        orderDetailBean = orderDetailBeans.get(0);

        orderSn = orderDetailBeans.get(0).getOrder_sn();
        totalPrice = orderDetailBeans.get(0).getOrder_amount();
        isOrder = true;
        setCheck();
        tv_amount.setText(totalPrice+"");
    }


    @Override
    public void setDataFail(String msg) {

    }

    @Override
    public void wxInfoSuccess(CollectDeleteBean wxRechangeBean) {
//        do_WX("pages/index/index");
        do_WX("pages/Payment/Payment?Bill_No="+wxRechangeBean.getMessage() + "&SessionId=" + ProApplication.SESSIONID(this) + "&send=123");
    }

    @Override
    public void wxInfoFail(String msg) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
//            Bundle bundle = new Bundle();
//            bundle.putInt("position",0);
//            UiHelper.launcherBundle(this, OrderListActivity.class,bundle);

            if (!whereStr.equals("order")) {
                Bundle bundle = new Bundle();
                bundle.putInt("status", 0);
                bundle.putString("order_sn", orderid);
                UiHelper.launcherForResultBundle(this, AllOrderActivity.class, 0x0987, bundle);
            }
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0x0987){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void setWxLoginSuccess(String wxSuccess) {
        Bundle bundle = new Bundle();
        bundle.putString("price",totalPrice+"");
        UiHelper.launcherForResultBundle(this,PayResultActivity.class,0x0987,bundle);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setWxLoginFail(String msg) {

        toast("支付失败");
//        if (!whereStr.equals("order")) {
            Bundle bundle = new Bundle();
            bundle.putInt("status", 0);
            bundle.putString("order_sn", orderid);
            UiHelper.launcherForResultBundle(this, AllOrderActivity.class, 0x0987, bundle);
//        }
        setResult(RESULT_OK);
        finish();
    }

    private void loadDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pay_load,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);

        TextView tv_pay_success = (TextView) view.findViewById(R.id.tv_pay_success);
        TextView tv_pay_fail = (TextView) view.findViewById(R.id.tv_pay_fail);
        tv_pay_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("price",totalPrice+"");
                UiHelper.launcherForResultBundle(PayActivity.this,PayResultActivity.class,0x0987,bundle);
            }
        });
        tv_pay_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}