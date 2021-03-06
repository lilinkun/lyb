package com.lzyyd.lyb.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.entity.SelfOrderBean;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.ShipStatusEnum;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by LG on 2018/12/18.
 */

public class SelfOrderAdapter extends RecyclerView.Adapter<SelfOrderAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<SelfOrderBean> selfOrderBeans ;
    private OnItemClick onItemClick;
    private OnItemClickListener mItemClickListener;

    public SelfOrderAdapter(Context context,ArrayList<SelfOrderBean> selfOrderBeans,OnItemClick onItemClick){
        this.context = context;
        this.selfOrderBeans = selfOrderBeans;
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_self_orderlist,null);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setTag(position);
        holder.tv_store.setText(selfOrderBeans.get(position).getStoreModel().getShop_name()+"");
        holder.tv_ship_pay.setText(ShipStatusEnum.getPageByValue(selfOrderBeans.get(position).getOrderStatus()).getStatusMsg()+"");
        double price = selfOrderBeans.get(position).getOrderAmount() - selfOrderBeans.get(position).getUseIntegral();

        BigDecimal b = new BigDecimal(price);
        price = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        holder.tv_integral.setText("(含积分抵扣"+ selfOrderBeans.get(position).getUseIntegral() + "、运费¥" + selfOrderBeans.get(position).getShippingFee() + ")");
        holder.tv_order_amount.setText("¥" + price);
        int num = 0;
        for (int i = 0;i<selfOrderBeans.get(position).getOrderGoodsItem().size();i++){
            num += selfOrderBeans.get(position).getOrderGoodsItem().get(i).getGoods_number();
        }
        holder.tv_goods_num.setText("共" + num +"件商品");

        if (selfOrderBeans.get(position).getOrderStatus() == 1){
            holder.tv_exit_order.setVisibility(View.GONE);
            if (selfOrderBeans.get(position).getErm() != null  && selfOrderBeans.get(position).getErm().length() > 0) {
                holder.tv_go_pay.setText("现场提货");
            }else {
                holder.tv_go_pay.setVisibility(View.GONE);
            }

        }else if (selfOrderBeans.get(position).getOrderStatus() == 0){
            holder.tv_exit_order.setVisibility(View.VISIBLE);
            holder.tv_go_pay.setVisibility(View.VISIBLE);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            Date date = new Date(System.currentTimeMillis());
            Date str = new Date();
            try {
                str = simpleDateFormat.parse(selfOrderBeans.get(position).getEffective_Payment_Time());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long newTime = date.getTime();
            long oldTime = str.getTime();
            if (newTime > oldTime){
                holder.tv_exit_order.setVisibility(View.GONE);
                holder.tv_go_pay.setVisibility(View.GONE);
                holder.tv_ship_pay.setText("已失效");
            }

        }else if (selfOrderBeans.get(position).getOrderStatus() == 2){
            holder.tv_go_pay.setVisibility(View.VISIBLE);
            holder.tv_go_pay.setText("确认收货");
            holder.tv_exit_order.setVisibility(View.GONE);
        }else if (selfOrderBeans.get(position).getOrderStatus() == 4){
            holder.tv_go_pay.setVisibility(View.GONE);
            holder.tv_exit_order.setVisibility(View.GONE);
        }else if (selfOrderBeans.get(position).getOrderStatus() == 5){
            holder.tv_exit_order.setVisibility(View.GONE);
            holder.tv_go_pay.setVisibility(View.GONE);
        }

        holder.tv_exit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick()) {
                    new AlertDialog.Builder(context).setMessage("确认删除此订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onItemClick.exit_order(selfOrderBeans.get(position).getOrderId());
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
        holder.tv_go_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonUtils.isFastDoubleClick()) {
                    if (selfOrderBeans.get(position).getOrderStatus() == 0) {
                        onItemClick.go_pay(selfOrderBeans.get(position));
                    } else if (selfOrderBeans.get(position).getOrderStatus() == 2) {
                        onItemClick.sureReceipt(selfOrderBeans.get(position).getOrderId());
                    } else if(selfOrderBeans.get(position).getOrderStatus() == 1){
                        onItemClick.getQrcode(selfOrderBeans.get(position).getErm());
                    }
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.tv_order_rv.setLayoutManager(linearLayoutManager);

        SelfOrderListAdapter selfOrderListAdapter = new SelfOrderListAdapter(context,selfOrderBeans.get(position).getOrderGoodsItem());

        holder.tv_order_rv.setAdapter(selfOrderListAdapter);
        holder.tv_order_rv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.itemView.performClick();  //模拟父控件的点击
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return selfOrderBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(ArrayList<SelfOrderBean> selfOrderBeans){
        this.selfOrderBeans = selfOrderBeans;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_store;
        private TextView tv_ship_pay;
        private TextView tv_order_amount;
        private RecyclerView tv_order_rv;
        private TextView tv_goods_num;
        private TextView tv_exit_order;
        private TextView tv_go_pay;
        private TextView tv_integral;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_store = (TextView) itemView.findViewById(R.id.tv_store);
            tv_ship_pay = (TextView) itemView.findViewById(R.id.tv_ship_pay);
            tv_order_amount = (TextView) itemView.findViewById(R.id.tv_order_amount);
            tv_order_rv = (RecyclerView)itemView.findViewById(R.id.rv_order_goods);
            tv_goods_num = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tv_exit_order = (TextView) itemView.findViewById(R.id.tv_exit_order);
            tv_go_pay = (TextView) itemView.findViewById(R.id.tv_go_pay);
            tv_integral = (TextView) itemView.findViewById(R.id.tv_integral);
        }
    }

    public interface OnItemClick{
        public void exit_order(String orderId);
        public void go_pay(SelfOrderBean orderId);
        public void sureReceipt(String orderId);
        public void getQrcode(String orderId);
    }
}
