package com.lzyyd.lyb.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Description:
 * Data：5/10/2018-10:17 AM
 *
 * @author lilinkun 类似3D效果
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        float alpha = 0.0f;
        if (0.0f <= position && position <= 1.0f) {
            alpha = 1.0f - position;
        } else if (-1.0f <= position && position < 0.0f) {
            alpha = position + 1.0f;
        }
        page.setAlpha(alpha);
    }
}