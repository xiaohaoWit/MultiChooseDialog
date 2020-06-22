package com.dgg.multidialog;

/**
 * @Description TODO
 * @Author LiuLang
 * @Date 2019/12/19 9:42
 */
public interface MultistageChooseListener<T extends MultistageData> {
    void onChoose(int position, T data);
}
