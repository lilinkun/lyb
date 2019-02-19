package com.lzyyd.lyb.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyyd.lyb.R;

/**
 * Created by LG on 2018/11/14.
 */
//public class PagerSlidingTabStrip extends HorizontalScrollView {
public class PagerSlidingTabStrip extends HorizontalScrollView implements ViewPager.OnPageChangeListener{

        private static final int COLOR_TEXT_NORMAL = 0xFF747474;
        private static final int COLOR_INDICATOR_COLOR = Color.BLACK;

        private Context context;
        private  int tabWidth;
        private String[] titles;
        private int count;
        private Paint mPaint;
        private float mTranslationX;
        private ViewPager viewPager;
        private int SCREEN_WIDTH;
        private float lineheight = 2.0f;
        private int position = 0;
        private Handler handler;

        public PagerSlidingTabStrip(Context context) {
            this(context, null);
        }

        public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        private void init(Context context){
            this.context = context;
            mPaint = new Paint();
            mPaint.setColor(getResources().getColor(R.color.main_app_color));
            mPaint.setStrokeWidth(lineheight);//底部指示线的宽度
            setHorizontalScrollBarEnabled(false);
            SCREEN_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
        }

        public void setLineheight(float height){
            this.lineheight = height;
            mPaint.setStrokeWidth(lineheight);//底部指示线的宽度
        }

        public void setViewPager(ViewPager viewPager){
            this.viewPager = viewPager;
            viewPager.addOnPageChangeListener(this);
            viewPager.setCurrentItem(position,false);
        }

        public void setTitles(String[] titles, int position, Handler handler){
            this.position = position;
            this.handler = handler;
            this.titles = titles;
            count = titles.length;
            tabWidth = (int)(SCREEN_WIDTH/5.5);
            generateTitleView();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            tabWidth = (int)(SCREEN_WIDTH/5.5);
        }

        @Override
        protected void dispatchDraw(Canvas canvas)
        {
            super.dispatchDraw(canvas);
            canvas.save();
            canvas.translate(mTranslationX, getHeight() - lineheight+20);
            canvas.drawLine(0, 0, tabWidth, 0, mPaint);//（startX, startY, stopX, stopY, paint）
            canvas.restore();
        }

        public void scroll(int position, float offset)
        {
            mTranslationX = tabWidth * (position + offset);
            scrollTo((int)mTranslationX-(SCREEN_WIDTH-tabWidth)/2, 0);
            invalidate();
        }

        private void generateTitleView()
        {
            if (getChildCount() > 0)
                this.removeAllViews();
            count = titles.length;

            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            for (int i = 0; i < count; i++)
            {
                TextView tv = new TextView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                tv.setGravity(Gravity.CENTER);
                if (i == position){
                    tv.setTextColor(context.getResources().getColor(R.color.main_app_color));
                }else {
                    tv.setTextColor(COLOR_TEXT_NORMAL);
                }
                tv.setText(titles[i]);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);//字体大小
                tv.setPadding(25,15,25,15);
                tv.setLayoutParams(lp);
                final int finalI = i;
                tv.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(viewPager!=null){
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putInt("position",finalI);
                            message.setData(bundle);
                            message.what = 0x110;
                            handler.sendMessage(message);
                            for (int i = 0; i < count; i++) {
                                if ( i == finalI){
                                    String s = ((TextView) linearLayout.getChildAt(finalI)).getText().toString();
                                    ((TextView) linearLayout.getChildAt(i)).setTextColor(context.getResources().getColor(R.color.main_app_color));
                                }else {
                                    ((TextView) linearLayout.getChildAt(i)).setTextColor(COLOR_TEXT_NORMAL);
                                }
                            }
                            viewPager.setCurrentItem(finalI,false);
                        }
                    }
                });
                linearLayout.addView(tv);
            }
            addView(linearLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            scroll(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
