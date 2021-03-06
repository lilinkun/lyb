package com.lzyyd.lyb.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.activity.CartOrderActivity;
import com.lzyyd.lyb.activity.ChooseAddressActivity;
import com.lzyyd.lyb.activity.OrderActivity;
import com.lzyyd.lyb.activity.SelfGoodsDetailActivity;
import com.lzyyd.lyb.activity.SelfGoodsTypeActivity;
import com.lzyyd.lyb.adapter.MyShoppingCarAdapter;
import com.lzyyd.lyb.base.BaseFragment;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.OrderContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.OrderBean;
import com.lzyyd.lyb.entity.OrderChildBean;
import com.lzyyd.lyb.entity.OrderGroupBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.presenter.OrderPresenter;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.UToast;
import com.lzyyd.lyb.util.UiHelper;
import com.lzyyd.lyb.util.UtilTool;
import com.lzyyd.lyb.util.UtilsLog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

/**
 * Created by LG on 2018/11/13.
 */

public class LzyMallFragment extends BaseFragment implements View.OnClickListener,OrderContract, MyShoppingCarAdapter.CheckInterface, MyShoppingCarAdapter.ModifyCountInterface {

    @BindView(R.id.listView)
    ExpandableListView expandableListView;
    @BindView(R.id.all_checkBox)
    CheckBox all_checkBox;
    @BindView(R.id.total_price)
    TextView total_price;
    @BindView(R.id.titlebar)
    CustomTitleBar titlebar;
    @BindView(R.id.go_pay)
    TextView goPay;
    @BindView(R.id.tv_head_right)
    TextView actionBarEdit;
    @BindView(R.id.order_info)
    LinearLayout orderInfo;
    @BindView(R.id.share_info)
    LinearLayout shareInfo;
    @BindView(R.id.mPtrframe)
    PtrFrameLayout mPtrFrame;
    @BindView(R.id.rl_cart_bottom)
    RelativeLayout rl_cart_bottom;
    @BindView(R.id.ll_no_goods)
    LinearLayout linearLayout;
    @BindView(R.id.go_shopping)
    TextView tv_no_shopping;


    private OrderPresenter orderPresenter = new OrderPresenter();
    private MyShoppingCarAdapter myShoppingCarAdapter;
    private ArrayList<OrderGroupBean<ArrayList<OrderBean>>> orderListBeans;
    Map<String,ArrayList<OrderChildBean>> map = new HashMap<>();
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private int order_result = 0x9123;
    private int goods_result = 0x02231;

    private OrderChildBean orderBean;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_lzymall;
    }

    @Override
    public void initEventAndData() {
//        Eyes.setStatusBarWhiteColor(getActivity(),getResources().getColor(R.color.white));
        orderPresenter.attachView(this);
        orderPresenter.onCreate(getActivity());
        orderPresenter.getList(ProApplication.SESSIONID(getActivity()));

        initPtrFrame();
    }

    private void initPtrFrame() {
//        final StoreHouseHeader header=new StoreHouseHeader(this);
//        header.setPadding(dp2px(20), dp2px(20), 0, 0);
//        header.initWithString("xiaoma is good");
        final PtrClassicDefaultHeader header=new PtrClassicDefaultHeader(getActivity());
        header.setPadding(dp2px(20), dp2px(20), 0, 0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
               /* mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                },2000);*/
                orderPresenter.getList(ProApplication.SESSIONID(getActivity()));
                updataData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    public void setData(){
        orderPresenter.getList(ProApplication.SESSIONID(getActivity()));
    }

    @OnClick({R.id.all_checkBox,R.id.del_goods,R.id.rl_more,R.id.go_pay,R.id.go_shopping})
    public void onClick(View view){
        AlertDialog dialog;
        if (!ButtonUtils.isFastDoubleClick(view.getId()) && view.getId() != R.id.all_checkBox) {
            switch (view.getId()) {
                case R.id.all_checkBox:
                    doCheckAll();
                    break;

                case R.id.rl_more:
                    flag = !flag;
                    setVisiable();
                    break;

                case R.id.del_goods:
                    if (mtotalCount == 0) {
                        UtilTool.toast(getActivity(), "请选择要删除的商品");
                        return;
                    }
                    dialog = new AlertDialog.Builder(getActivity()).create();
                    dialog.setMessage("确认要删除该商品吗?");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDelete();
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    dialog.show();
                    break;

                case R.id.go_pay:
                    if (mtotalCount == 0) {
                        UtilTool.toast(getActivity(), "请选择要支付的商品");
                        return;
                    }

                    orderPresenter.isUserAddress(ProApplication.SESSIONID(getActivity()));
                    break;
                case R.id.go_shopping:

                    Bundle bundle = new Bundle();
                    bundle.putString("goodsname", "");
                    UiHelper.launcherForResultBundle(this, SelfGoodsTypeActivity.class, goods_result, bundle);

                    break;
            }
        }else {
            if (view.getId() == R.id.all_checkBox){
                doCheckAll();
            }
        }
    }


    private void setVisiable() {
        if (flag) {
            orderInfo.setVisibility(View.GONE);
            shareInfo.setVisibility(View.VISIBLE);
            actionBarEdit.setText("完成");
            goPay.setVisibility(View.GONE);
        } else {
            orderInfo.setVisibility(View.VISIBLE);
            shareInfo.setVisibility(View.GONE);
            goPay.setVisibility(View.VISIBLE);
            actionBarEdit.setText("编辑");
        }
    }

    /**
     * 全选和反选
     * 错误标记：在这里出现过错误
     */
    private void doCheckAll() {
        for (int i = 0; i < orderListBeans.size(); i++) {
            OrderGroupBean group = orderListBeans.get(i);
            group.setChoosed(all_checkBox.isChecked());
            List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
            for (int j = 0; j < child.size(); j++) {
                child.get(j).setChoosed(all_checkBox.isChecked());//这里出现过错误
            }
        }
        myShoppingCarAdapter.notifyDataSetChanged();
        calulate();
    }

    private void updataData(){
        if (orderListBeans != null && orderListBeans.size() > 0) {
            for (int i = 0; i < orderListBeans.size(); i++) {
                OrderGroupBean group = orderListBeans.get(i);
                group.setChoosed(false);
                List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
                for (int j = 0; j < child.size(); j++) {
                    child.get(j).setChoosed(false);//这里出现过错误
                }
            }
            myShoppingCarAdapter.notifyDataSetChanged();
            calulate();
            all_checkBox.setChecked(false);
        }
        mPtrFrame.refreshComplete();
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void OrderListSuccess(ArrayList<OrderListBean<ArrayList<OrderBean>>> orderListBeans) {
        ArrayList<OrderGroupBean<ArrayList<OrderBean>>> orderGroupBeans = new ArrayList<>();

        if (orderListBeans.size() > 0) {
            linearLayout.setVisibility(View.GONE);
            rl_cart_bottom.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.VISIBLE);
            for (OrderListBean<ArrayList<OrderBean>> orderListBean : orderListBeans) {
                OrderGroupBean<ArrayList<OrderBean>> orderGroupBean = new OrderGroupBean<>();
                orderGroupBean.setOrderListBean(orderListBean);
                ArrayList<OrderChildBean> orderChildBeans = new ArrayList<>();
                for (OrderBean orderBean : orderListBean.getGoodsList()) {
                    OrderChildBean orderChildBean = new OrderChildBean();
                    orderChildBean.setOrderBean(orderBean);
                    orderChildBean.setChoosed(false);
                    orderChildBean.setParentId(orderListBean.getStore_id());
                    orderChildBeans.add(orderChildBean);
                    map.put(orderListBean.getStore_id(), orderChildBeans);
                }
                orderGroupBeans.add(orderGroupBean);
                this.orderListBeans = orderGroupBeans;
            }

            myShoppingCarAdapter = new MyShoppingCarAdapter(orderGroupBeans, map, getActivity(),getActivity());
            expandableListView.setAdapter(myShoppingCarAdapter);
            myShoppingCarAdapter.setCheckInterface(this);
            myShoppingCarAdapter.setModifyCountInterface(this); //关键步骤2:设置增删减的接口
            expandableListView.setGroupIndicator(null); //设置属性 GroupIndicator 去掉向下箭头
            for (int i = 0; i < myShoppingCarAdapter.getGroupCount(); i++) {
                expandableListView.expandGroup(i); //关键步骤4:初始化，将ExpandableListView以展开的方式显示
            }

            expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int firstVisiablePostion=view.getFirstVisiblePosition();
                    int top=-1;
                    View firstView=view.getChildAt(firstVisibleItem);
                    if(firstView!=null){
                        top=firstView.getTop();
                    }
                    UtilsLog.i("firstVisiableItem="+firstVisibleItem+",fistVisiablePosition="+firstVisiablePostion+",firstView="+firstView+",top="+top);
                    if(firstVisibleItem==0&&top==0){
                        mPtrFrame.setEnabled(true);
                    }else{
                        mPtrFrame.setEnabled(false);
                    }
                }
            });
        }else {
            linearLayout.setVisibility(View.VISIBLE);
            rl_cart_bottom.setVisibility(View.GONE);
            expandableListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void OrderListFail(String msg) {

    }

    @Override
    public void modifyOrderSuccess(CollectDeleteBean collectDeleteBean,String num,View showCountView) {
        if (collectDeleteBean.getStatus() != 0){
            UToast.show(getActivity(),collectDeleteBean.getMessage());
        }else {
            orderBean.getOrderBean().setNum(num + "");
            ((TextView) showCountView).setText(String.valueOf(num));
            myShoppingCarAdapter.notifyDataSetChanged();
            calulate();
        }
    }

    @Override
    public void modifyOrderFail(String msg) {

    }

    @Override
    public void deleteGoodsSuccess(CollectDeleteBean collectDeleteBean) {
        if (collectDeleteBean.getStatus() == 0) {
            UToast.show(getActivity(),"删除成功");
            calulate();
        }else {
            UToast.show(getActivity(),"删除失败");
        }
    }

    @Override
    public void deleteGoodsFail(String msg) {
        UToast.show(getActivity(),"删除失败");
    }

    @Override
    public void cartOrderBuySuccess() {

    }

    @Override
    public void cartOrderBuyFail() {

    }

    @Override
    public void isAddressSuccess(String msg) {

        Bundle bundle = new Bundle();
        bundle.putInt("type",1);
        bundle.putString("CartId",getOrderList());
        UiHelper.launcherForResultBundle(this, CartOrderActivity.class,order_result,bundle);
    }

    @Override
    public void isAddressFail(String msg) {

        new android.app.AlertDialog.Builder(getActivity()).setMessage("您还没有收货地址，请填写").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UiHelper.launcher(getActivity(),ChooseAddressActivity.class);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * @return 判断组元素是否全选
     */
    private boolean isCheckAll() {
        for (OrderGroupBean<ArrayList<OrderBean>> group : orderListBeans) {
            if (!group.isChoosed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        OrderGroupBean group = orderListBeans.get(groupPosition);
        List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
        for (int i = 0; i < child.size(); i++) {
            child.get(i).setChoosed(isChecked);
        }
        myShoppingCarAdapter.setGroupClickId(groupPosition,isChecked);

        if (isCheckAll()) {
            all_checkBox.setChecked(true);//全选
        } else {
            all_checkBox.setChecked(false);//反选
        }
        myShoppingCarAdapter.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true; //判断该组下面的所有子元素是否处于同一状态
        OrderGroupBean group = orderListBeans.get(groupPosition);
        List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
        for (int i = 0; i < child.size(); i++) {
            //不选全中
            if (child.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }

        if (allChildSameState) {
            group.setChoosed(isChecked);//如果子元素状态相同，那么对应的组元素也设置成这一种的同一状态
        } else {
            group.setChoosed(false);//否则一律视为未选中
        }

        if (isCheckAll()) {
            all_checkBox.setChecked(true);//全选
        } else {
            all_checkBox.setChecked(false);//反选
        }

        myShoppingCarAdapter.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        orderBean = (OrderChildBean)myShoppingCarAdapter.getChild(groupPosition,childPosition);
        int count = Integer.valueOf(orderBean.getOrderBean().getNum());
        count++;
        orderPresenter.modifyOrder(count+"",orderBean.getOrderBean().getCart_id(),showCountView,ProApplication.SESSIONID(getActivity()));
//        orderBean.getOrderBean().setNum(count + "");
//        ((TextView) showCountView).setText(String.valueOf(count));
//        myShoppingCarAdapter.notifyDataSetChanged();
//        calulate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        orderBean = (OrderChildBean)myShoppingCarAdapter.getChild(groupPosition,childPosition);
        int count = Integer.valueOf(orderBean.getOrderBean().getNum());
        if (count == 1) {
            return;
        }
        count--;
        orderPresenter.modifyOrder(count+"",orderBean.getOrderBean().getCart_id(),showCountView,ProApplication.SESSIONID(getActivity()));
//        orderBean.getOrderBean().setNum(count + "");
//        ((TextView) showCountView).setText("" + count);
//        myShoppingCarAdapter.notifyDataSetChanged();
//        calulate();
    }

    @Override
    public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        orderBean = (OrderChildBean) myShoppingCarAdapter.getChild(groupPosition, childPosition);
        int count = Integer.valueOf(orderBean.getOrderBean().getNum());
        orderPresenter.modifyOrder(count + "", orderBean.getOrderBean().getCart_id(),showCountView, ProApplication.SESSIONID(getActivity()));
//        UtilsLog.i("进行更新数据，数量" + count + "");
//        ((TextView) showCountView).setText(String.valueOf(count));
//        myShoppingCarAdapter.notifyDataSetChanged();
//        calulate();
    }

    /**
     * @param groupPosition
     * @param childPosition 思路:当子元素=0，那么组元素也要删除
     */
    @Override
    public void childDelete(int groupPosition, int childPosition) {
        OrderGroupBean group = orderListBeans.get(groupPosition);
        List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
        child.remove(childPosition);
        if (child.size() == 0) {
            orderListBeans.remove(groupPosition);
        }
        myShoppingCarAdapter.notifyDataSetChanged();
        calulate();
    }

    public String getOrderList(){
        String OrderStr = "";
        for (int i = 0; i < orderListBeans.size(); i++) {
            OrderGroupBean group = orderListBeans.get(i);
            List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
            for (int j = 0; j < child.size(); j++) {
                if (child.get(j).isChoosed()) {
                    if (OrderStr.equals("")){
                        OrderStr = child.get(j).getOrderBean().getCart_id();
                    }else {
                        OrderStr = OrderStr + "," + child.get(j).getOrderBean().getCart_id();
                    }
                }
            }
        }
        return OrderStr;
    }

    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        String deleteStr = "";
        List<OrderGroupBean> toBeDeleteGroups = new ArrayList<OrderGroupBean>(); //待删除的组元素
        for (int i = 0; i < orderListBeans.size(); i++) {
            OrderGroupBean group = orderListBeans.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<OrderChildBean> toBeDeleteChilds = new ArrayList<OrderChildBean>();//待删除的子元素
            List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
            for (int j = 0; j < child.size(); j++) {
                if (child.get(j).isChoosed()) {
                    toBeDeleteChilds.add(child.get(j));
                    if (deleteStr.equals("")){
                        deleteStr = child.get(j).getOrderBean().getCart_id();
                    }else {
                        deleteStr = deleteStr + "," + child.get(j).getOrderBean().getCart_id();
                    }

                }
            }
            child.removeAll(toBeDeleteChilds);
        }
        orderListBeans.removeAll(toBeDeleteGroups);
        //重新设置购物车
        setCartNum();
        myShoppingCarAdapter.notifyDataSetChanged();

        orderPresenter.deleteOrder(deleteStr,ProApplication.SESSIONID(getActivity()));
    }

    /**
     * 设置购物车的数量
     */
    private void setCartNum() {
        int count = 0;
        /*for (int i = 0; i < orderListBeans.size(); i++) {
            OrderGroupBean group = orderListBeans.get(i);
            group.setChoosed(all_checkBox.isChecked());
            List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
            for (OrderChildBean childs : child) {
                count++;
            }
        }*/

        //购物车已经清空
        if (count == 0) {
            clearCart();
        } else {
            titlebar.setTileName("购物车(" + count + ")");
        }

    }

    private void clearCart() {
        titlebar.setTileName("购物车");
    }

    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate() {
        mtotalPrice = 0.00;
        mtotalCount = 0;
        for (int i = 0; i < orderListBeans.size(); i++) {
            OrderGroupBean group = orderListBeans.get(i);
            List<OrderChildBean> child = map.get(group.getOrderListBean().getStore_id());
            for (int j = 0; j < child.size(); j++) {
                OrderChildBean good = child.get(j);
                if (good.isChoosed()) {
                    mtotalCount += Integer.valueOf(good.getOrderBean().getNum());
                    mtotalPrice += good.getOrderBean().getShop_price() * Integer.valueOf(good.getOrderBean().getNum());
                }
            }
        }

        BigDecimal b = new BigDecimal(mtotalPrice);
        mtotalPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        total_price.setText("¥" + mtotalPrice + "");
        goPay.setText("去支付(" + mtotalCount + ")");
        if (mtotalCount == 0) {
            setCartNum();
        } else {
            titlebar.setTileName("购物车(" + mtotalCount + ")");
        }

        if (orderListBeans.size() == 0){
           rl_cart_bottom.setVisibility(View.GONE);
           linearLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == order_result){
                orderPresenter.getList(ProApplication.SESSIONID(getActivity()));
            }
            if (requestCode == goods_result){
                orderPresenter.getList(ProApplication.SESSIONID(getActivity()));
            }
        }
    }
}
