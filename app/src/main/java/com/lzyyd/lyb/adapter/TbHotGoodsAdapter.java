package com.lzyyd.lyb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.entity.HotHomeBean;
import com.lzyyd.lyb.entity.TbMaterielBean;
import com.lzyyd.lyb.ui.CustomRoundAngleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LG on 2018/11/16.
 */

public class TbHotGoodsAdapter extends RecyclerView.Adapter<TbHotGoodsAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<HotHomeBean> hotHomeBeans = null;
    private OnItemClickListener onItemClickListener;
    private HotHomeBean homeBean;

    public TbHotGoodsAdapter(Context context, ArrayList<HotHomeBean> homeHeadBean, LayoutInflater mInflater) {
        this.hotHomeBeans = homeHeadBean;
        this.context = context;
        this.mInflater = mInflater;
    }

    public void setData(ArrayList<HotHomeBean> hotHomeBeans){
        this.hotHomeBeans = hotHomeBeans;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.adapter_hot_goods_grid, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        view.setOnClickListener(this);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemView.setTag(position);

        homeBean = hotHomeBeans.get(position);

        holder.goodsTitleNameTv.setText(homeBean.getGoods_name());

        holder.goodsPriceTv.setText("¥" + homeBean.getShop_price());
        holder.goodsBuyCountTv.setText(homeBean.getUse_number() + "");
        holder.goodsIntegralTv.setText("可用积分抵扣" + homeBean.getGive_integral());

        if (homeBean.getGoods_img() != null && !homeBean.getGoods_img().isEmpty()) {
            Picasso.with(context).load(ProApplication.HEADIMG + homeBean.getGoods_img()).into(holder.goodsPicImg);
        }
    }

    @Override
    public int getItemCount() {
        return hotHomeBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener!=null){
            onItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView goodsTitleNameTv;
        private TextView goodsPriceTv;
        private TextView goodsBuyCountTv;
        private TextView goodsIntegralTv;
        private CustomRoundAngleImageView goodsPicImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            goodsBuyCountTv = (TextView) itemView.findViewById(R.id.tv_buy_count);
            goodsTitleNameTv = (TextView) itemView.findViewById(R.id.tv_goods_title_name);
            goodsPriceTv = (TextView) itemView.findViewById(R.id.tv_goods_price);
            goodsIntegralTv = (TextView) itemView.findViewById(R.id.tv_integral);
            goodsPicImg = (CustomRoundAngleImageView) itemView.findViewById(R.id.iv_goods_pic);
        }
    }
}
