package com.lzyyd.lyb.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Toast;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.activity.Category1Activity;
import com.lzyyd.lyb.activity.GoodsTypeActivity;
import com.lzyyd.lyb.activity.JdGoodActivity;
import com.lzyyd.lyb.activity.LoginActivity;
import com.lzyyd.lyb.activity.MembershipActivity;
import com.lzyyd.lyb.activity.RushBuyActivity;
import com.lzyyd.lyb.activity.SearchActivity;
import com.lzyyd.lyb.activity.SelfGoodsDetailActivity;
import com.lzyyd.lyb.activity.SelfGoodsTypeActivity;
import com.lzyyd.lyb.adapter.HomeFragmentAdapter;
import com.lzyyd.lyb.adapter.TbHotGoodsAdapter;
import com.lzyyd.lyb.base.BaseFragment;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.HomeContract;
import com.lzyyd.lyb.db.DBManager;
import com.lzyyd.lyb.entity.FlashBean;
import com.lzyyd.lyb.entity.HomeCategoryBean;
import com.lzyyd.lyb.entity.HomeHeadBean;
import com.lzyyd.lyb.entity.HotHomeBean;
import com.lzyyd.lyb.entity.UrlBean;
import com.lzyyd.lyb.interf.OnScrollChangedListener;
import com.lzyyd.lyb.presenter.HomePresenter;
import com.lzyyd.lyb.ui.CusPtrClassicFrameLayout;
import com.lzyyd.lyb.ui.GridSpacingItemDecoration;
import com.lzyyd.lyb.ui.MyGridView;
import com.lzyyd.lyb.ui.TranslucentScrollView;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.DownloadUtil;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.PhoneFormatCheckUtils;
import com.lzyyd.lyb.util.UToast;
import com.lzyyd.lyb.util.UiHelper;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

/**
 * Created by LG on 2018/11/10.
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnScrollChangedListener, HomeContract, TbHotGoodsAdapter.OnItemClickListener, OnBannerListener {

    @BindView(R.id.title_layout_search)
    LinearLayout linearLayout;
    @BindView(R.id.gv_goos_type)
    MyGridView gridView;
    @BindView(R.id.bannerView)
    Banner banner;
    @BindView(R.id.tsv_home)
    TranslucentScrollView translucentScrollView;
    @BindView(R.id.gv_hot_commodities)
    RecyclerView mHotGridView;
    @BindView(R.id.iv_home_advertisement1)
    ImageView iv_home_advertisement1;
    @BindView(R.id.iv_home_advertisement2)
    ImageView iv_home_advertisement2;
    @BindView(R.id.iv_home_advertisement3)
    ImageView iv_home_advertisement3;
    @BindView(R.id.ll_membership)
    LinearLayout ll_membership;
    @BindView(R.id.mPtrframe)
    CusPtrClassicFrameLayout mPtrFrame;

    private PopupWindow popupWindow;
    private int PAGE_INDEX = 1;
    int mAlpha = 0;
    private TbHotGoodsAdapter tbHotGoodsAdapter;
    private ArrayList<HomeCategoryBean> homeCategoryBeans;
    private ArrayList<HotHomeBean> hotHomeBeans;
    private HomeFragmentAdapter homeFragmentAdapter;
    private HomeHeadBean homeHeadBean;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_homepage;
    }

    HomePresenter homePresenter = new HomePresenter();

    @Override
    public void initEventAndData() {
//        Eyes.setStatusBarColor(getActivity(),getResources().getColor(R.color.home_bg));
        Eyes.translucentStatusBar(getActivity());
        translucentScrollView.init(this);
        initPtrFrame();

        homePresenter.attachView(this);
        homePresenter.onCreate(getActivity());
        homePresenter.getUrl(ProApplication.SESSIONID(getActivity()));
        homePresenter.setFlash(ProApplication.SESSIONID(getActivity()));

        gridView.setOnItemClickListener(this);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        int spanCount = 2; // 2 columns
        int spacing = 20; // 50px
//        api.registerApp("wx3686dfb825618610");

        boolean includeEdge = false;
        mHotGridView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mHotGridView.setLayoutManager(gridLayoutManager);
//        mBanner.setImageLoader(new PicassoImageLoader());
//        mBanner.setImages();

    }

    private void initPtrFrame() {
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        header.setPadding(dp2px(20), dp2px(20), 0, 0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                homePresenter.getUrl(ProApplication.SESSIONID(getActivity()));
                homePresenter.setFlash(ProApplication.SESSIONID(getActivity()));

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    @OnClick({R.id.lin_list, R.id.text_search, R.id.ll_home_tb, R.id.ll_home_tm, R.id.ll_home_jd, R.id.iv_home_advertisement1,R.id.iv_home_advertisement3})
    public void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick(view.getId())) {
            switch (view.getId()) {
                case R.id.lin_list:

//                showPopuWindow();
                    UiHelper.launcher(getActivity(), Category1Activity.class);

                    break;

                case R.id.text_search:

                    UiHelper.launcher(getActivity(), SearchActivity.class);

                    break;

                case R.id.ll_home_tb:
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", 0);
                    bundle.putInt("isMall",0);
                    UiHelper.launcherBundle(getActivity(), GoodsTypeActivity.class, bundle);

                    break;

                case R.id.ll_home_tm:
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("position", 0);
                    bundle1.putInt("isMall",1);
                    UiHelper.launcherBundle(getActivity(), GoodsTypeActivity.class, bundle1);

                    break;

                case R.id.ll_home_jd:

//                    UiHelper.launcher(getActivity(), JdGoodActivity.class);

                    break;

                case R.id.iv_home_advertisement1:


                    UiHelper.launcher(getActivity(), MembershipActivity.class);

                    break;

                case R.id.iv_home_advertisement3:


                    UiHelper.launcher(getActivity(), RushBuyActivity.class);

                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (!ButtonUtils.isFastDoubleClick()) {
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("home", homeCategoryBeans.get(position));
            UiHelper.launcherBundle(getActivity(), SelfGoodsTypeActivity.class, bundle);
        }
    }

    @Override
    public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        /**  ScrollView 滚动动态改变标题栏 */
        // 滑动的最小距离（自行定义，you happy jiu ok）
        int minHeight = 10;
        // 滑动的最大距离（自行定义，you happy jiu ok）
        int maxHeight = 150;

        // 滑动距离小于定义得最小距离
        if (scrollView.getScrollY() <= minHeight) {
            mAlpha = 0;
        }
        // 滑动距离大于定义得最大距离
        else if (scrollView.getScrollY() > maxHeight) {
            mAlpha = 255;
        }
        // 滑动距离处于最小和最大距离之间
        else {
            // （滑动距离 - 开始变化距离）：最大限制距离 = mAlpha ：255
            mAlpha = (scrollView.getScrollY() - minHeight) * 255 / (maxHeight - minHeight);
        }
        // 初始状态 标题栏/导航栏透明等
        if (mAlpha <= 0) {
            setViewBackgroundAlpha(linearLayout, 0);
            linearLayout.setBackgroundColor(Color.argb(0, 252, 55, 125));

        }
        //  终止状态：标题栏/导航栏 不在进行变化
        else if (mAlpha >= 255) {
            setViewBackgroundAlpha(linearLayout, 255);
            linearLayout.setBackgroundColor(Color.argb(255, 252, 55, 125));

        }
        // 变化中状态：标题栏/导航栏随ScrollView 的滑动而产生相应变化
        else {
            setViewBackgroundAlpha(linearLayout, mAlpha);
            linearLayout.setBackgroundColor(Color.argb(y, 252, 55, 125));
        }
    }

    @Override
    public void loadMore() {
    }


    /**
     * 设置View的背景透明度
     *
     * @param view
     * @param alpha
     */
    public void setViewBackgroundAlpha(View view, int alpha) {
        if (view == null) return;

        Drawable drawable = view.getBackground();
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void getUrlSuccess(UrlBean urlBean) {
        ProApplication.HEADIMG = urlBean.getGoodsImgUrl();
        ProApplication.BANNERIMG = urlBean.getShopImgUrl();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("img", ProApplication.HEADIMG).putString("shop", ProApplication.BANNERIMG).commit();
    }

    @Override
    public void getUrlFail(String msg) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        ProApplication.HEADIMG = sharedPreferences.getString("img", ProApplication.HEADIMG);
        ProApplication.BANNERIMG = sharedPreferences.getString("shop", ProApplication.BANNERIMG);
    }

    @Override
    public void onFlashSuccess(HomeHeadBean homeHeadBean) {

        this.homeHeadBean = homeHeadBean;

        if (mPtrFrame.isRefreshing()) {
            mPtrFrame.refreshComplete();
        }

        homeCategoryBeans = homeHeadBean.getCategory_list();
        DBManager.getInstance(getActivity()).insertCategoryList(homeCategoryBeans);

        if (homeFragmentAdapter == null) {
            homeFragmentAdapter = new HomeFragmentAdapter(getActivity(), homeCategoryBeans);
            gridView.setAdapter(homeFragmentAdapter);
        } else {
            homeFragmentAdapter.setData(homeCategoryBeans);
        }

        DBManager.getInstance(getActivity()).deleteCategoryListBean();
        DBManager.getInstance(getActivity()).insertCategoryList(homeCategoryBeans);


        startBanner(homeHeadBean.getFlash_list());

        Picasso.with(getActivity()).load(ProApplication.BANNERIMG + homeHeadBean.getZysc_list().get(0).getFlashpic()).into(iv_home_advertisement1);
        Picasso.with(getActivity()).load(ProApplication.BANNERIMG + homeHeadBean.getZysc_list().get(1).getFlashpic()).into(iv_home_advertisement2);
        Picasso.with(getActivity()).load(ProApplication.BANNERIMG + homeHeadBean.getZysc_list().get(2).getFlashpic()).into(iv_home_advertisement3);

        hotHomeBeans = (ArrayList<HotHomeBean>) homeHeadBean.getHot_goods();
        tbHotGoodsAdapter = new TbHotGoodsAdapter(getActivity(), hotHomeBeans, getLayoutInflater());
        mHotGridView.setAdapter(tbHotGoodsAdapter);
        tbHotGoodsAdapter.setItemClickListener(this);
    }

    @Override
    public void onFlashFail(String msg) {
//        UToast.show(getActivity(), msg);
        if (msg.contains("网络异常")) {
        }else {
            UiHelper.launcher(getActivity(), LoginActivity.class);
            getActivity().finish();
        }
    }

    private void startBanner(final ArrayList<FlashBean> flashBeans) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < flashBeans.size(); i++) {
            strings.add("asdads" + i);
        }

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new GlideImageLoader());
        //设置图片网址或地址的集合
        banner.setImages(flashBeans);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(strings);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();

    }


    @Override
    public void onItemClick(int position) {

        if (!ButtonUtils.isFastDoubleClick()) {
            Bundle bundle = new Bundle();
            bundle.putString("goodsid", hotHomeBeans.get(position).getGoods_id());
            UiHelper.launcherBundle(getActivity(), SelfGoodsDetailActivity.class, bundle);
        }
    }

    @Override
    public void OnBannerClick(int position) {
        FlashBean flashBean = homeHeadBean.getFlash_list().get(position);
        if (flashBean.getUrl() != null && flashBean.getUrl().length() > 0  && PhoneFormatCheckUtils.isNumeric(flashBean.getUrl())){
            if (Integer.valueOf(flashBean.getUrl()) > 0){
                Bundle bundle = new Bundle();
                bundle.putString("goodsid",flashBean.getUrl());
                UiHelper.launcherBundle(getActivity(),SelfGoodsDetailActivity.class,bundle);
            }
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            Picasso.with(context).load(ProApplication.BANNERIMG + ((FlashBean) path).getFlashpic()).error(R.mipmap.ic_family_girl).into(imageView);
        }
    }

}
