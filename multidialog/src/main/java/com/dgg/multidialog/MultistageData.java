package com.dgg.multidialog;

import androidx.annotation.ColorInt;

/**
 * @Description TODO
 * @Author LiuLang
 * @Date 2019/12/19 9:37
 */
public interface MultistageData {
    //返回显示的字段
    String getValue();

    //选中颜色
    @ColorInt
    int getSelectedColor();

    //正常颜色
    @ColorInt
    int getNormalColor();
}
