package com.humanheima.designpatten.adapter;

/**
 * Created by Administrator on 2016/11/18.
 */
public class Test {
    public void testAdapterPattern() {
        Chinese me = new Chinese();
        Ukrainian ukrainian = new Translator(me);
        ukrainian.sayUkrainian("我爱你");
    }
}
