package com.github.wenslo.深入理解JVM.chapter7;

/**
 *
 **/
public class NotInitialization {
    public static void main(String[] args) {
        /**
         * 非主动使用类字段演示
         */
//        System.out.println(SubClass.value);

        /**
         * 被动使用类字段演示二:
         * 通过数组定义来引用类，不会触发此类的初始化
         */
//        SuperClass[] sca = new SuperClass[10];


        /**
         * 被动使用类字段演示三:
         * 常量在编译阶段会存入调用类的常量池中，本质上没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化
         **/
        System.out.println(ConstClass.HELLO_WORLD);
    }
}
