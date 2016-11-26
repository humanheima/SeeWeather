package com.humanheima.designpatten.adapter;

/**
 * Created by Administrator on 2016/11/18.
 * 有道词典来了。他想把中文翻译成Ukrainian,得实现Ukrainian接口
 * 持有一个只会中文小伙的引用,在需要说乌克兰语的时候，
 * 经过语法翻译最终调用小伙的说中文
 * <p>
 * 1.这是一个对象适配器，与被适配类Chinese是关联关系
 * Adapter 中持有一个被适配类对象的引用，因此叫做对象适配器。
 */
public class Translator implements Ukrainian {

    //持有小伙的引用
    private Chinese chinese;

    public Translator(Chinese chinese) {
        this.chinese = chinese;
    }

    @Override
    public void sayUkrainian(String string) {
        //省略了复杂的语法翻译过程，想象一下
        chinese.sayChinese(string);
    }
}
