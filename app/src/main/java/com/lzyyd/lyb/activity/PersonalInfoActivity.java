package com.lzyyd.lyb.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzyyd.lyb.R;
import com.lzyyd.lyb.base.BaseActivity;
import com.lzyyd.lyb.base.ProApplication;
import com.lzyyd.lyb.contract.PersonalInfoContract;
import com.lzyyd.lyb.entity.CollectDeleteBean;
import com.lzyyd.lyb.entity.ImageUploadResultBean;
import com.lzyyd.lyb.entity.PersonalInfoBean;
import com.lzyyd.lyb.http.RetrofitHelper;
import com.lzyyd.lyb.interf.OnTitleBarClickListener;
import com.lzyyd.lyb.presenter.PersonalInfoPresenter;
import com.lzyyd.lyb.ui.CustomTitleBar;
import com.lzyyd.lyb.ui.RoundImageView;
import com.lzyyd.lyb.util.Eyes;
import com.lzyyd.lyb.util.FileImageUpload;
import com.lzyyd.lyb.util.PhoneFormatCheckUtils;
import com.lzyyd.lyb.util.UiHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by LG on 2018/11/19.
 */

public class PersonalInfoActivity extends BaseActivity implements OnTitleBarClickListener, View.OnClickListener,PersonalInfoContract {

    @BindView(R.id.titlebar)
    CustomTitleBar customTitleBar;
    @BindView(R.id.riv_info)
    RoundImageView roundImageView;
    @BindView(R.id.tv_nickname)
    TextView nickName;
    @BindView(R.id.tv_phone)
    TextView tv_phone;

    private PopupWindow popupWindow = null;
    private static final int IMAGEBUNDLE = 0x221;
    private static final int REQUEST_CAMERA = 0x222;
    private static final int RESULT_MYNICK = 0x112;
    private boolean isChangeSuccess = false;
    private String mFilePath;
    private  Uri cropImageUri;
    private PersonalInfoPresenter personalInfoPresenter = new PersonalInfoPresenter();


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123){
                String str = msg.getData().getString("str");
                if (str!= null && str.length() > 0) {
                    Gson gson = new Gson();
                    ImageUploadResultBean imageUploadResultBean = gson.fromJson(str, ImageUploadResultBean.class);

                    personalInfoPresenter.uploadImage(imageUploadResultBean.getUrl().get(0),ProApplication.SESSIONID(PersonalInfoActivity.this));
                }
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public void initEventAndData() {
        Eyes.setStatusBarWhiteColor(this,getResources().getColor(R.color.white));
        customTitleBar.SetOnTitleClickListener(this);

        mFilePath = Environment.getExternalStorageDirectory()+ "/test/" + "temp.jpg";// 获取SD卡路径
//        mFilePath = mFilePath + "/test/" + "temp.jpg";// 指定路径

        personalInfoPresenter.onCreate(this);
        personalInfoPresenter.attachView(this);
        personalInfoPresenter.getInfo(ProApplication.SESSIONID(this));

    }

    @OnClick({R.id.rl_head_title_info,R.id.rl_nickname_info,R.id.iv_head_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_head_title_info:

                View v = LayoutInflater.from(this).inflate(R.layout.pop_head_info,null);

                popupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);

                popupWindow.showAsDropDown(customTitleBar);

                TextView tvExit = (TextView) v.findViewById(R.id.tv_pop_exit);
                TextView tvTakePhoto = (TextView) v.findViewById(R.id.tv_takephoto);
                TextView tvSystemPhoto = (TextView) v.findViewById(R.id.tv_systemphoto);

                tvExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                tvTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                        if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(PersonalInfoActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(PersonalInfoActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PersonalInfoActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);} else {

                            File outputImage  = new File(mFilePath);
                            File str = outputImage.getParentFile();
                            if (!outputImage.getParentFile().exists()){
                                outputImage.getParentFile().mkdirs();
                            }

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
                                //改变Uri  com.lzyyd.lyb.fileprovider注意和xml中的一致
                                Uri photoUri = FileProvider.getUriForFile(PersonalInfoActivity.this, "com.lzyyd.lyb.fileprovider", outputImage);
                                //添加权限
                                intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
//                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径  
                            }else {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
                            }
                            startActivityForResult(intent, REQUEST_CAMERA);
                        }
                    }
                });
                tvSystemPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        //调用相册
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, IMAGEBUNDLE);
                    }
                });


                break;

            case R.id.rl_nickname_info:
                Bundle bundle = new Bundle();
                bundle.putString("nick",nickName.getText().toString());
                UiHelper.launcherForResultBundle(this,MyNickNameActivity.class,RESULT_MYNICK,bundle);

                break;

            case R.id.iv_head_right:

//                personalInfoPresenter.getInfo();

                break;
        }
    }

    @Override
    public void onBackClick() {
        if (isChangeSuccess) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isChangeSuccess) {
                setResult(RESULT_OK);
            }
            finish();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGEBUNDLE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();

            startPhotoZoom(selectedImage);
        }else if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri selectedImage = Uri.fromFile(new File(mFilePath));
                Bitmap bm = BitmapFactory.decodeFile(new File(mFilePath).getAbsolutePath());
//            roundImageView.setImageBitmap(bm);
                Uri photoUri = FileProvider.getUriForFile(PersonalInfoActivity.this, "com.lzyyd.lyb.fileprovider", new File(mFilePath));
                startPhotoZoom(photoUri);
            }else {
                Uri inputUri = Uri.fromFile(new File(mFilePath));
                startPhotoZoom(inputUri);
            }
        }else if (requestCode == 4 && resultCode == Activity.RESULT_OK){
            final File file = new File(getExternalCacheDir(), "crop.jpg");
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            roundImageView.setImageBitmap(bm);

            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {

                            String str = FileImageUpload.uploadFile(file, RetrofitHelper.ImageUrl);
                            Bundle bundle = new Bundle();
                            bundle.putString("str",str);
                            Message message = new Message();
                            message.setData(bundle);
                            message.what = 123;
                            handler.sendMessage(message);
                        }
                    }
            ).start();

        }else if (requestCode == RESULT_MYNICK && resultCode == Activity.RESULT_OK){
            String account = data.getStringExtra("account");
            nickName.setText(account);
        }
    }
    //加载图片
    private void showImage(String imagePath){
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
//        roundImageView.setImageBitmap(bm);
    }

    public void startPhotoZoom(Uri paramUri)
    {
        File headFile = new File(getExternalCacheDir(), "crop.jpg");
        try
        {
            if ((headFile).exists()) {
                (headFile).delete();
            }
            (headFile).createNewFile();
        }catch (IOException e)
            {
                    e.printStackTrace();
            }
        this.cropImageUri = Uri.fromFile(headFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(paramUri, "image/*");
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("output", this.cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 4);
    }


    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void modifySuccess() {

    }

    @Override
    public void modifyFail(String msg) {

    }

    @Override
    public void getInfoSuccess(PersonalInfoBean loginBean) {
        if (loginBean.getUser_data().getHeadPic() != null){
            Picasso.with(this).load(loginBean.getUser_data().getHeadPic()).into(roundImageView);
        }
        nickName.setText(loginBean.getUser_data().getNikeName());
        tv_phone.setText(PhoneFormatCheckUtils.phoneAddress(loginBean.getUser_data().getMobile()));
    }

    @Override
    public void uploadImageSuccess(CollectDeleteBean msg) {
        toast(msg.getMessage());
        if (msg.getStatus() == 0) {
            isChangeSuccess = true;
        }

    }

    @Override
    public void uploadImageFail(String msg) {
        toast(msg + "");
    }
}
