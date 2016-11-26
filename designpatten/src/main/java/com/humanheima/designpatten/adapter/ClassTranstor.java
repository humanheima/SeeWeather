package com.humanheima.designpatten.adapter;

/**
 * 2.类适配器，与被适配类是继承关系
 * 采用类适配器模式的翻译软件，继承了被适配类 Chinese，实现了目标接口 Ukrainian，
 * 从而使得原本不能使用的 sayChinese(string) 方法可以被调用。
 * 对象适配器支持传入一个被适配器对象，因此可以做到对多种被适配接口进行适配，
 * 而类适配器直接继承，无法动态修改，所以一般情况下对象适配器使用更广泛。
 *
 * @Test public void testClassAdapterPattern(){
 * Ukrainian ukrainianMan = new ClassTranslator();
 * ukrainianMan.sayUkrainian("刘奶奶找牛奶奶买榴莲牛奶");
 * }
 */
public class ClassTranstor extends Chinese implements Ukrainian {
    @Override
    public void sayUkrainian(String string) {
        sayChinese(string);
    }
}
