package com.lzyyd.lyb.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lzyyd.lyb.R;
import com.lzyyd.lyb.adapter.MenuItemAdapter;
import com.lzyyd.lyb.adapter.MenuLeftAdapter;
import com.lzyyd.lyb.adapter.MenuRightAdapter;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.CategoryContract;
import com.lzyyd.lyb.entity.Category1Bean;
import com.lzyyd.lyb.entity.CategoryListBean;
import com.lzyyd.lyb.entity.HomeCategoryBean;
import com.lzyyd.lyb.presenter.CategoryPresenter;
import com.lzyyd.lyb.ui.GridViewForScrollView;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.UToast;
import com.lzyyd.lyb.util.UiHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/12/16.
 */

public class Category1Activity extends BaseActivity implements CategoryContract, AdapterView.OnItemClickListener {

    @BindView(R.id.lv_menu)
    ListView lv_menu;
    @BindView(R.id.gridView)
    GridViewForScrollView gridView;

    private CategoryPresenter categoryPresenter = new CategoryPresenter();
    private ArrayList<CategoryListBean<ArrayList<Object>>> homeCategoryBeans;
    private MenuItemAdapter adapter;
    private ArrayList<HomeCategoryBean> homeCategorys = new ArrayList<>();
    private MenuLeftAdapter menuLeftAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_category1;
    }

    @Override
    public void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this, getResources().getColor(R.color.white));

        categoryPresenter.attachView(this);
        categoryPresenter.onCreate(this);

        categoryPresenter.getCategoryList(ProApplication.SESSIONID(this));

    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void getDataSuccess(ArrayList<CategoryListBean<ArrayList<Object>>> homeCategoryBeans) {
        this.homeCategoryBeans = homeCategoryBeans;
        menuLeftAdapter = new MenuLeftAdapter(this, homeCategoryBeans);
        lv_menu.setAdapter(menuLeftAdapter);
        lv_menu.setOnItemClickListener(this);

        setGridview(0);
    }

    @Override
    public void getDataFail(String msg) {

    }

    @OnClick({R.id.text_search, R.id.lin_list})
    public void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.text_search:
                    UiHelper.launcher(this, SearchActivity.class);
                    break;

                case R.id.lin_list:

                    finish();

                    break;
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        if (!ButtonUtils.isFastDoubleClick()) {
            menuLeftAdapter.setSelectItem(position);
            menuLeftAdapter.notifyDataSetChanged();
            homeCategorys.clear();
            setGridview(position);
//        }
    }

    private void setGridview(int position) {
        if (homeCategoryBeans.get(position).getSubclass().size() > 0) {
            ArrayList objects = (ArrayList) homeCategoryBeans.get(position).getSubclass();
            Gson gson = new Gson();
            for (int j = 0; j < objects.size(); j++) {
                String str = gson.toJson(objects.get(j));
                HomeCategoryBean homeCategoryBean = gson.fromJson(str, HomeCategoryBean.class);
                homeCategorys.add(homeCategoryBean);

            }
        }
        if (adapter == null) {
            adapter = new MenuItemAdapter(this, homeCategorys);
            gridView.setAdapter(adapter);
        } else {
            adapter.setData(homeCategorys);
        }
    }
}
