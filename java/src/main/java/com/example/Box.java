package com.example;

import static com.sun.javafx.fxml.expression.Expression.add;

/**
 * /**
 * 关于泛型
 * 1：使用泛型可以使错误在编译时被检测到，从而增加程序的健壮性
 */
public class Box {
    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public static void main(String[] args) {
        /**
         * 其中的set()方法可以接受任何java对象作为参数（任何对象都是Object的子类），
         * 假如在某个地方使用该类，set()方法预期的输入对象为Integer类型，
         * 但是实际输入的却是String类型，就会抛出一个运行时错误，
         * 这个错误在编译阶段是无法检测的。
         */
        Box box = new Box();
        box.setObject("abs");
        Integer integer = (Integer) box.getObject();//编译时不会报错，但是运行时会报ClassCastException

        Box1<Integer> box1 = new Box1<>();//制定类型为Integer
        //box1.setT("abc");编译的时候就会报错
        box1.setT(2);
        Integer a = box1.getT();//不用转换类型，因为box1里面放的就是Integer

        add(1, 2);
        add(3.1f, 4.2f);
        add("abc", "def");
    }

    static class Box1<T> {
        private T t;

        public void setT(T t) {
            this.t = t;
        }

        public T getT() {
            return t;
        }
    }
}
