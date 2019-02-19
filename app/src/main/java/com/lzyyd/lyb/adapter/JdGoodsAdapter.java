package com.lzyyd.lyb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.entity.JdGoodsBean;
import com.lzyyd.lyb.entity.JdImageBean;
import com.lzyyd.lyb.ui.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jd.union.open.goods.jingfen.query.response.JFGoodsResp;

/**
 * Created by LG on 2019/1/29.
 */

public class JdGoodsAdapter extends RecyclerView.Adapter<JdGoodsAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<JdGoodsBean> jdGoodsBeans;
    private OnItemClickListener mItemClickListener;

    public JdGoodsAdapter(Context context,ArrayList<JdGoodsBean> jdGoodsBeans){
        this.context = context;
        this.jdGoodsBeans = jdGoodsBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_goods_item,null);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        double price = jdGoodsBeans.get(position).getPriceInfo().getPrice();

        Picasso.with(this.context).load((jdGoodsBeans.get(position).getImageInfo().getImageList()[0]).getUrl()).resize(300, 300).centerCrop().config(Bitmap.Config.RGB_565).into(holder.img_goods_icon);
        holder.tx_goods_title.setText(jdGoodsBeans.get(position).getSkuName());
        holder.tv_goods_sales_volume.setText(jdGoodsBeans.get(position).getInOrderCount30Days() + "人已买");
        if (jdGoodsBeans.get(position).getCouponInfo()!= null) {
            if (jdGoodsBeans.get(position).getCouponInfo().getCouponList() != null && jdGoodsBeans.get(position).getCouponInfo().getCouponList().length > 0) {
                holder.tv_coupon_price.setText(jdGoodsBeans.get(position).getCouponInfo().getCouponList()[0].getDiscount() + "元券");
                if (jdGoodsBeans.get(position).getCouponInfo().getCouponList()[0].getQuota() >= price){
                    price = price - jdGoodsBeans.get(position).getCouponInfo().getCouponList()[0].getDiscount();
                }
            }
        }
        holder.tx_goods_msg.setText("¥" + price);
    }

    @Override
    public int getItemCount() {
        return jdGoodsBeans.size();
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
        private ImageView img_goods_icon;
        private TextView tx_goods_title, tv_original_price, tv_coupon_price, tv_goods_type_price, tv_goods_sales_volume;
        private MyTextView tx_goods_msg;

        public ViewHolder(View itemView) {
            super(itemView);
            img_goods_icon = (ImageView) itemView.findViewById(R.id.iv_goods_pic);
            tx_goods_msg = (MyTextView) itemView.findViewById(R.id.tx_goods_msg);
            tx_goods_title = (TextView) itemView.findViewById(R.id.tv_goods_title);
            tv_original_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tv_coupon_price = (TextView) itemView.findViewById(R.id.tv_coupon_price);
            tv_goods_type_price = (TextView) itemView.findViewById(R.id.tv_goods_type_price);
            tv_goods_sales_volume = (TextView) itemView.findViewById(R.id.tv_goods_sales_volume);
        }
    }
}
