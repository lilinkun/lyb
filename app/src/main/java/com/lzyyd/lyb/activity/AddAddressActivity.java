package com.lzyyd.lyb.activity;

import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.lzyyd.lyb.R;
import com.lzyyd.lyb.adressselectorlib.AddressPickerView;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.AddAddressContract;
import com.lzyyd.lyb.entity.AddressBean;
import com.lzyyd.lyb.entity.ProvinceBean;
import com.lzyyd.lyb.presenter.AddAddressPresenter;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.util.ButtonUtils;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.LzyydUtil;
import com.lzyyd.lyb.util.PhoneFormatCheckUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lzyyd.lyb.adressselectorlib.AddressPickerView.TYPE_PROVINCE;

/**
 * Created by LG on 2018/12/10.
 */

public class AddAddressActivity extends BaseActivity implements AddAddressContract {

    @BindView(R.id.titlebar)
    CustomTitleBar customTitleBar;
    @BindView(R.id.im_bg)
    ImageView imageView;
    @BindView(R.id.tv_local_address)
    TextView mLocalTv;
    @BindView(R.id.et_address_details)
    EditText addressDetails;
    @BindView(R.id.et_address_phone)
    EditText phoneAddress;
    @BindView(R.id.et_address_consignee)
    EditText consigneeAddress;
    @BindView(R.id.switch_turn)
    Switch mSwitch;
    @BindView(R.id.ll_address_details)
    LinearLayout ll_address_details;

    private AddressPickerView addressView;
    private AddAddressPresenter addAddressPresenter = new AddAddressPresenter();
    private String mProvinceCode;
    private String mCityCode;
    private String mAreaCode;
    private String mZipCode;
    private String isDefault = "0";
    private AddressBean addressBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initEventAndData() {

        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
        addAddressPresenter.attachView(this);
        addAddressPresenter.onCreate(this);
        addAddressPresenter.getLocalData("1", TYPE_PROVINCE);

        if(getIntent() != null){
            if (getIntent().getBundleExtra(LzyydUtil.TYPEID) != null) {
                addressBean = (AddressBean) getIntent().getBundleExtra(LzyydUtil.TYPEID).getSerializable("addressBean");
            }
        }

        if (addressBean != null){
            mSwitch.setChecked(addressBean.getIs_default() == 1 ? true : false);
            phoneAddress.setText(addressBean.getMobile());
            addressDetails.setText(addressBean.getAddress());
            consigneeAddress.setText(addressBean.getConsignee());
            mLocalTv.setText(addressBean.getAddressName());
            customTitleBar.setRightText("修改");

            mProvinceCode = addressBean.getProvince();
            mCityCode = addressBean.getCity();
            mAreaCode = addressBean.getDistrict();
            mZipCode = addressBean.getZipcode();
            isDefault = addressBean.getIs_default() + "";
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDefault = "1";
                } else {
                    isDefault = "0";
                }
            }
        });

    }

    @OnClick({R.id.ll_province, R.id.tv_head_right,R.id.ll_back})
    public void onClick(View view) {

        if (!ButtonUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.ll_province:
                    if (imageView != null) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                    final PopupWindow popupWindow = new PopupWindow(this);
                    View rootView = LayoutInflater.from(this).inflate(R.layout.pop_address_picker, null, false);
                    addressView = rootView.findViewById(R.id.apvAddress);
                    addressView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
                        @Override
                        public void onSureClick(String address, String provinceCode, String cityCode, String districtCode, String zipCode) {
                            mLocalTv.setText(address);
                            mProvinceCode = provinceCode;
                            mCityCode = cityCode;
                            mAreaCode = districtCode;
                            mZipCode = zipCode;
                            popupWindow.dismiss();
                        }
                    });
                    popupWindow.setContentView(rootView);
                    popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.showAsDropDown(ll_address_details);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (imageView != null && imageView.isShown()) {
                                imageView.setVisibility(View.GONE);
                            }
                        }
                    });

                    break;

                case R.id.tv_head_right:

                    if (TextUtils.isEmpty(consigneeAddress.getText().toString())) {
                        toast(R.string.input_address_consignee);
                        return;
                    } else if (consigneeAddress.getText().toString().length() < 2) {
                        toast(R.string.input_length_limit);
                        return;
                    } else if (TextUtils.isEmpty(phoneAddress.getText().toString()) || !PhoneFormatCheckUtils.isChinaPhoneLegal(phoneAddress.getText().toString())) {
                        toast(R.string.prompt_phone_number_invalid);
                        return;
                    } else if (TextUtils.isEmpty(addressDetails.getText().toString())) {
                        toast(R.string.input_detail_address);
                        return;
                    }

                    if (addressBean == null) {
                        addAddressPresenter.getSaveAddress(consigneeAddress.getText().toString(), mProvinceCode, mCityCode, mAreaCode, addressDetails.getText().toString(), mZipCode, phoneAddress.getText().toString(), isDefault, ProApplication.SESSIONID(this));
                    } else {
                        addAddressPresenter.modifyAddress(addressBean.getAddress_id(), consigneeAddress.getText().toString(), mProvinceCode, mCityCode, mAreaCode, addressDetails.getText().toString(), mZipCode, phoneAddress.getText().toString(), isDefault, ProApplication.SESSIONID(this));
                    }
                    break;

                case R.id.ll_back:

                    finish();

                    break;
            }
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void getDataSuccess(ArrayList<ProvinceBean> provinceBeans, int id) {
        addressView.getDataSuccess(provinceBeans, id);
    }

    @Override
    public void getDataFail(String msg) {
    }

    @Override
    public void getSaveSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void getSaveFail(String msg) {
        toast(msg);
    }

    @Override
    public void modifySuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void modifyFail(String msg) {
        toast(msg);
    }
}
