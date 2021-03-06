package com.lzyyd.lyb.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.OrderListContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.CountBean;
import com.lzyyd.lyb.entity.SelfOrderBean;
import com.lzyyd.lyb.entity.WxInfoBean;
import com.lzyyd.lyb.entity.WxRechangeBean;
import com.lzyyd.lyb.fragment.AllOrderFragment;
import com.lzyyd.lyb.fragment.CompletedOrderFragment;
import com.lzyyd.lyb.fragment.OverOrderFragment;
import com.lzyyd.lyb.fragment.WaitPayFragment;
import com.lzyyd.lyb.fragment.WaitReceiveFragment;
import com.lzyyd.lyb.interf.IPayOrderClickListener;
import com.lzyyd.lyb.interf.IWxResultListener;
import com.lzyyd.lyb.presenter.OrderListPresenter;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.QRCodeUtil;
import com.lzyyd.lyb.util.UToast;
import com.lzyyd.lyb.util.UiHelper;
import com.lzyyd.lyb.wxapi.WXPayEntryActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by LG on 2018/12/5.
 */
public class OrderListActivity extends BaseActivity implements IPayOrderClickListener,OrderListContract, IWxResultListener {

    @BindView(R.id.order_list_tablayou)
    TabLayout orderListTablayou;
    @BindView(R.id.order_list_vp)
    ViewPager orderListVp;
    @BindView(R.id.ll_choose_order)
    LinearLayout linearLayout;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.rl_order)
    RelativeLayout rl_order;

    private List<String> mTitles;
    private TextView tvSelf,tvTb,tvJd;
    private PopupWindow popupWindow;
    private PopupWindow payPopupWindow;
    private SelfOrderBean selfOrderBean;
    private Dialog payDialog;
    private IPayOrderClickListener payListener;
    private OrderListPresenter orderListPresenter = new OrderListPresenter();
    public final static String mlist = "https://wqs.jd.com/order/orderlist_merge.shtml";

    AllOrderFragment allOrderFragment = new AllOrderFragment();
    WaitPayFragment waitPayFragment = new WaitPayFragment();
    WaitReceiveFragment waitReceiveFragment = new WaitReceiveFragment();
    CompletedOrderFragment completedOrderFragment = new CompletedOrderFragment();
    OverOrderFragment overOrderFragment = new OverOrderFragment();

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        }
    };
    KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();//这个是即时性参数  可以设置
    KelperTask mKelperTask;
    public static final int timeOut = 15;

    OpenAppAction mOpenAppAction = new OpenAppAction() {

        @Override
        public void onStatus(final int status, String s) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//                        dialogShow();
                    } else {
                        mKelperTask = null;
//                        dialogDiss();
                    }
                }
            });
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_orderlist;
    }

    @Override
    public void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));

        orderListPresenter.attachView(this);
        orderListPresenter.onCreate(this);

        initData();

        TabPageAdapter pageAdapter = new TabPageAdapter(getSupportFragmentManager());
        pageAdapter.setTitles(mTitles);
        orderListVp.setAdapter(pageAdapter);
        orderListTablayou.setupWithViewPager(orderListVp);
        orderListTablayou.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));
        orderListVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    allOrderFragment.setData();
                }else if (position == 1){
                    waitPayFragment.setData();
                }else if (position == 2){
                    waitReceiveFragment.setData();
                }else if (position == 3){
                    completedOrderFragment.setData();
                }else if (position == 4){
                    overOrderFragment.setData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        WXPayEntryActivity.setPayListener(this);

        allOrderFragment.setPayListener(this);
        waitPayFragment.setPayListener(this);
        waitReceiveFragment.setPayListener(this);
        completedOrderFragment.setPayListener(this);
    }

    private void initData() {
        mTitles = new ArrayList<>();
        mTitles.add("全部");
        mTitles.add("待支付");
        mTitles.add("待发货");
        mTitles.add("待收货");
        mTitles.add("交易成功");
    }

    @OnClick({R.id.ll_back,R.id.ll_choose_order})
    public void onClick(View view){
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.ll_back:

                    finish();

                    break;

                case R.id.ll_choose_order:
                    if (popupWindow == null) {

                        View popView = LayoutInflater.from(this).inflate(R.layout.popup_search_type, null);
                        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        popupWindow.showAsDropDown(linearLayout);

                        tvSelf = popView.findViewById(R.id.tv_home_self);
                        tvTb = popView.findViewById(R.id.tv_home_tb);
                        tvJd = popView.findViewById(R.id.tv_home_jd);
                        tvSelf.setVisibility(View.GONE);
                        tvSelf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                }
                            }
                        });

                        tvTb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                }
                                try {
                                    if (checkPackage(OrderListActivity.this, "com.taobao.taobao")) {
                                        Intent localIntent = new Intent();
                                        localIntent.setAction("Android.intent.action.VIEW");
                                        localIntent.setData(Uri.parse("http://uland.taobao.com/coupon/edetail?"));
                                        localIntent.setClassName("com.taobao.taobao", "com.taobao.order.list.OrderListActivity");
                                        startActivity(localIntent);
                                    } else {
                                        UToast.show(OrderListActivity.this, "您没有安装淘宝App");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        tvJd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                }

                                try {
//                                    mKelperTask = KeplerApiManager.getWebViewService().openOrderListPage(mKeplerAttachParameter, ProApplication.context(), mOpenAppAction, timeOut);
                                    mKelperTask = KeplerApiManager.getWebViewService().openAppWebViewPage(OrderListActivity.this,mlist,mKeplerAttachParameter,mOpenAppAction);
//                                } catch (KeplerBufferOverflowException e) {
//                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        } else {
                            popupWindow.showAsDropDown(linearLayout);
                        }
                    }

                    break;
            }
        }
    }

    @Override
    public void payMode(SelfOrderBean selfOrderBean, int mode) {
        this.selfOrderBean = selfOrderBean;


        Bundle bundle = new Bundle();
        bundle.putString(LzyydUtil.ORDERID,selfOrderBean.getOrderSn()+"");
        bundle.putString(LzyydUtil.WHERE,"order");
        UiHelper.launcherForResultBundle(OrderListActivity.this,PayActivity.class,0x1231,bundle);

//        orderListPresenter.getOrderData(ProApplication.SESSIONID(this));
    }

    @Override
    public void SureReceive(String orderId) {
        orderListPresenter.sureReceipt(orderId,ProApplication.SESSIONID(this));
    }

    @Override
    public void getQrcode(String orderId) {
        Dialog dialog = new Dialog(this);
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_qrcode,null);
        ImageView imageView = view.findViewById(R.id.iv_qrcode);
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(orderId, 200, 200);
        imageView.setImageBitmap(mBitmap);
        dialog.setContentView(view);
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                allOrderFragment.setData();
                if (waitReceiveFragment != null){
                    waitReceiveFragment.setData();
                }

            }
        });
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void InfoAccountSuccess(CountBean orderDetailBean) {
        showPopup(orderDetailBean);
    }

    @Override
    public void InfoAccountFail(String msg) {

    }

    @Override
    public void selfPaySuccess(CollectDeleteBean collectDeleteBean) {
        if (collectDeleteBean.getStatus() == 0){
            payDialog.dismiss();
            allOrderFragment.setData();
            waitPayFragment.setData();
        }else {
            toast("支付失败");
        }
    }

    @Override
    public void selfPayFail(String msg) {
        toast(msg);
    }

    @Override
    public void sureReceiptSuccess(CollectDeleteBean collectDeleteBean) {
        toast("确认收货成功");
        completedOrderFragment.setData();
        allOrderFragment.setData();
    }

    @Override
    public void sureReceiptFail(String msg) {

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

    @Override
    public void setWxSuccess() {
        if (allOrderFragment!=null) {
            allOrderFragment.setData();
        }
        if (waitReceiveFragment!=null ) {
            waitPayFragment.setData();
        }
    }

    @Override
    public void setWxFail() {
        toast("支付失败");
    }

    class TabPageAdapter extends FragmentPagerAdapter{
        List<Fragment> fragments = new ArrayList<>();
        private List<String> titles;

        public TabPageAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(allOrderFragment);
            fragments.add(waitPayFragment);
            fragments.add(waitReceiveFragment);
            fragments.add(completedOrderFragment);
            fragments.add(overOrderFragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void setTitles(List<String> titles){this.titles = titles;}

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 将实例化的fragment进行显示即可。
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            Fragment fragment = fragments.get(position);// 获取要销毁的fragment
//            getSupportFragmentManager().beginTransaction().hide(fragment).commit();// 将其隐藏即可，并不需要真正销毁，这样fragment状态就得到了保存
        }
    }

    public static boolean checkPackage(Context paramContext, String paramString)
    {
        if ((paramString == null) || ("".equals(paramString))) {
            return false;
        }
        try
        {
            paramContext.getPackageManager().getPackageInfo(paramString, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {}
        return false;
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
        String endTime = selfOrderBean.getEffective_Payment_Time();

        if ((int)selfOrderBean.getMoneyPayid() == 0){
            check_wx.setClickable(false);
            check_wx.setChecked(false);
            check_wx.setEnabled(false);
            check_self.setChecked(true);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        try {
            Date str = simpleDateFormat.parse(endTime);
            long effectiveTime = str.getTime();
            Date date = new Date(System.currentTimeMillis());
            long nowTime = date.getTime();

            long time = effectiveTime - nowTime;

        int now = (int) (System.currentTimeMillis()/1000);
        tv_time.start(time);
//        tv_time.setCountdownTime((int)(time/1000)-((int) (System.currentTimeMillis()/1000)-now),1+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        tv_right_now_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_self.isChecked()){
                    payPopupWindow.dismiss();
                    View view = LayoutInflater.from(OrderListActivity.this).inflate(R.layout.dialog_pay,null);

                    payDialog = new Dialog(OrderListActivity.this);
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

                    textView.setText(selfOrderBean.getMoneyPayid()+"");

                    btn_sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editText.getText().toString().isEmpty()){
                                orderListPresenter.selfPay(editText.getText().toString(),selfOrderBean.getOrderSn(),ProApplication.SESSIONID(OrderListActivity.this));
                            }
                        }
                    });


                }else if (check_wx.isChecked()){
                    payPopupWindow.dismiss();
//                    toast("你瞅我干啥，暂时不能微信支付类");
                    orderListPresenter.setWxPay(selfOrderBean.getOrderSn(),selfOrderBean.getMoneyPayid()+"","29","1","Android","com.lzyyd.lyb",ProApplication.SESSIONID(OrderListActivity.this));
                }else {
                    toast("请选择支付方式");
                }
            }
        });

        tv_price.setText("¥" + selfOrderBean.getMoneyPayid()+"");

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == 0x0987){
                allOrderFragment.setData();
                waitPayFragment.setData();
                completedOrderFragment.setData();
            }else if (requestCode == 0x1231){

                if (allOrderFragment!=null) {
                    allOrderFragment.setData();
                }
                if (waitReceiveFragment!=null ) {
                    waitPayFragment.setData();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
