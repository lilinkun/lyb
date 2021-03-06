package com.lzyyd.lyb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.activity.SelfGoodsDetailActivity;
import com.lzyyd.lyb.activity.ShoppingCarActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.entity.GoodsInfo;
import com.lzyyd.lyb.entity.OrderBean;
import com.lzyyd.lyb.entity.OrderChildBean;
import com.lzyyd.lyb.entity.OrderGroupBean;
import com.lzyyd.lyb.entity.OrderListBean;
import com.lzyyd.lyb.entity.StoreInfo;
import com.lzyyd.lyb.util.ActivityUtil;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.UToast;
import com.lzyyd.lyb.util.UiHelper;
import com.lzyyd.lyb.util.UtilTool;
import com.lzyyd.lyb.util.UtilsLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.POST;

/**
 * Created by LG on 2018/12/17.
 */

public class MyShoppingCarAdapter extends BaseExpandableListAdapter {
    private ArrayList<OrderGroupBean<ArrayList<OrderBean>>> groups;
    private  Map<String,ArrayList<OrderChildBean>> map;
    private ArrayList<OrderChildBean> orderChildBeans;
    private Context mcontext;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private GroupEditorListener groupEditorListener;
    private int count = 0;
    private int groupClickId = -1;
    boolean isCheck = false;
    public static final int child_goods_result = 0x3233;
    Activity activity;

    public void setGroupClickId(int groupid,boolean isCheck){
        this.groupClickId = groupid;
        this.isCheck = isCheck;
    }

    public MyShoppingCarAdapter(ArrayList<OrderGroupBean<ArrayList<OrderBean>>> groups, Map<String,ArrayList<OrderChildBean>> map, Context mcontext, Activity activity) {
        this.groups = groups;
        this.mcontext = mcontext;
        this.map = map;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        orderChildBeans = map.get(groups.get(groupPosition).getOrderListBean().getStore_id());
        return orderChildBeans.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        orderChildBeans = map.get(groups.get(groupPosition).getOrderListBean().getStore_id());
        return orderChildBeans.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (groupPosition == 0){
            groupViewHolder.view.setVisibility(View.GONE);
        }else {
            groupViewHolder.view.setVisibility(View.VISIBLE);
        }
//        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        final OrderGroupBean<ArrayList<OrderBean>> group = (OrderGroupBean<ArrayList<OrderBean>>)getGroup(groupPosition);
        groupViewHolder.storeName.setText(group.getOrderListBean().getShop_name());
        groupViewHolder.storeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());
            }
        });
        groupViewHolder.storeCheckBox.setChecked(group.isChoosed());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_product, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        final OrderChildBean child = (OrderChildBean) getChild(groupPosition, childPosition);
        if (child != null) {
            childViewHolder.goodsName.setText(child.getOrderBean().getGoods_name());
            childViewHolder.goodsPrice.setText("¥" + child.getOrderBean().getShop_price() + "");
            childViewHolder.goodsNum.setText(String.valueOf(child.getOrderBean().getNum()));
            childViewHolder.goods_size1.setText( child.getOrderBean().getGoods_attr_one());
            childViewHolder.goods_size2.setText( child.getOrderBean().getGoods_attr_two());

            Picasso.with(mcontext).load(ProApplication.HEADIMG + child.getOrderBean().getGoods_img()).into(childViewHolder.goodsImage);

            /*if (groupClickId != -1){
                if(groupPosition == groupClickId){
                    childViewHolder.singleCheckBox.setChecked(isCheck);
                }
            }*/
            childViewHolder.singleCheckBox.setChecked(child.isChoosed());
            childViewHolder.singleCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    child.setChoosed(((CheckBox) v).isChecked());
                    childViewHolder.singleCheckBox.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());
                }
            });
            childViewHolder.increaseGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked());
                }
            });
            childViewHolder.reduceGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked());
                }
            });
            childViewHolder.goodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(groupPosition,childPosition,childViewHolder.goodsNum,childViewHolder.singleCheckBox.isChecked(),child);
                }
            });

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ButtonUtils.isFastDoubleClick()) {
                    if (ActivityUtil.activityList.size() > 3) {
                        ActivityUtil.removeOldActivity();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("goodsid", child.getOrderBean().getGoods_id());
                    bundle.putString("type", "cart");
                    UiHelper.launcherBundle(activity, SelfGoodsDetailActivity.class, bundle);
                }
            }
        });
        return convertView;
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     */
    private void showDialog(final int groupPosition, final int childPosition, final View showCountView, final  boolean isChecked,final  OrderChildBean child) {
        final AlertDialog.Builder alertDialog_Builder=new AlertDialog.Builder(mcontext);
        View view= LayoutInflater.from(mcontext).inflate(R.layout.dialog_change_num,null);
        final AlertDialog dialog=alertDialog_Builder.create();
        dialog.setView(view);//errored,这里是dialog，不是alertDialog_Buidler
        count=Integer.valueOf(child.getOrderBean().getNum());
        final EditText num= (EditText) view.findViewById(R.id.dialog_num);
        num.setText(count+"");
        //自动弹出键盘
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                UtilTool.showKeyboard(mcontext,showCountView);
            }
        });
        final TextView increase= (TextView) view.findViewById(R.id.dialog_increaseNum);
        final TextView DeIncrease=(TextView)view.findViewById(R.id.dialog_reduceNum);
        final TextView pButton= (TextView) view.findViewById(R.id.dialog_Pbutton);
        final TextView nButton= (TextView) view.findViewById(R.id.dialog_Nbutton);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(num.getText().toString().trim());
                if(number==0){
                    dialog.dismiss();
                }else{
                    UtilsLog.i("数量="+number+"");
                    if (number > child.getOrderBean().getGoods_number()){
                        UToast.show(mcontext,"库存不足");
                    }else {
                        num.setText(String.valueOf(number));
                        child.getOrderBean().setNum(number + "");
                        modifyCountInterface.doUpdate(groupPosition, childPosition, showCountView, isChecked);
                        dialog.dismiss();
                    }
                }
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                num.setText(String.valueOf(count));
            }
        });
        DeIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1){
                    count--;
                    num.setText(String.valueOf(count));
                }
            }
        });
        dialog.show();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public GroupEditorListener getGroupEditorListener() {
        return groupEditorListener;
    }

    public void setGroupEditorListener(GroupEditorListener groupEditorListener) {
        this.groupEditorListener = groupEditorListener;
    }

    public CheckInterface getCheckInterface() {
        return checkInterface;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public ModifyCountInterface getModifyCountInterface() {
        return modifyCountInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }


    static class GroupViewHolder {
        @BindView(R.id.store_checkBox)
        CheckBox storeCheckBox;
        @BindView(R.id.store_name)
        TextView storeName;
        @BindView(R.id.view_line)
        View view;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 店铺的复选框
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param isChecked     组元素的选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param isChecked     子元素的选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子Item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEditorListener {
        void groupEditor(int groupPosition);
    }

    /**
     * 使某个小组处于编辑状态
     */
    private class GroupViewClick implements View.OnClickListener {
        private StoreInfo group;
        private int groupPosition;
        private TextView editor;

        public GroupViewClick(StoreInfo group, int groupPosition, TextView editor) {
            this.group = group;
            this.groupPosition = groupPosition;
            this.editor = editor;
        }

        @Override
        public void onClick(View v) {
            if (editor.getId() == v.getId()) {
                groupEditorListener.groupEditor(groupPosition);
                if (group.isEditor()) {
                    group.setEditor(false);
                } else {
                    group.setEditor(true);
                }
                notifyDataSetChanged();
            }
        }
    }


    static class ChildViewHolder {
        @BindView(R.id.single_checkBox)
        CheckBox singleCheckBox;
        @BindView(R.id.goods_image)
        ImageView goodsImage;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.goods_spec1)
        TextView goods_size1;
        @BindView(R.id.goods_spec2)
        TextView goods_size2;
        @BindView(R.id.goods_price)
        TextView goodsPrice;
        @BindView(R.id.goods_data)
        RelativeLayout goodsData;
        @BindView(R.id.reduce_goodsNum)
        ImageView reduceGoodsNum;
        @BindView(R.id.goods_Num)
        TextView goodsNum;
        @BindView(R.id.increase_goods_Num)
        ImageView increaseGoodsNum;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}