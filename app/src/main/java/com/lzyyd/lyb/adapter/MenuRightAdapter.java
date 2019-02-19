package com.lzyyd.lyb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.entity.CategoryBean;
import com.lzyyd.lyb.ui.GridViewForScrollView;
import com.lzyyd.lyb.util.UToast;

import java.util.List;

/**
 * 右侧主界面ListView的适配器
 *
 * @author liguo
 */
public class MenuRightAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean.DataBean> foodDatas;

    public MenuRightAdapter(Context context, List<CategoryBean.DataBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }

    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryBean.DataBean dataBean = foodDatas.get(position);
        List<CategoryBean.DataBean.DataListBean> dataList = dataBean.getDataList();
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home, null);
            viewHold = new ViewHold();
            viewHold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.gridView);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
//        MenuItemAdapter adapter = new MenuItemAdapter(context, dataList);
//        viewHold.gridView.setAdapter(adapter);

        return convertView;
    }

    private static class ViewHold {
        private GridViewForScrollView gridView;
    }

}
