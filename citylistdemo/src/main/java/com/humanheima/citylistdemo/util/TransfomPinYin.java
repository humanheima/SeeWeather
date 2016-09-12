package com.humanheima.citylistdemo.util;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * Created by Administrator on 2016/9/12.
 * 把汉字转化为拼音
 */
public class TransfomPinYin {
    public static String transform(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)));
        }
        return buffer.toString();
    }
}
