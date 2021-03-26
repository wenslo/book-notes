package com.github.wenslo.深入理解JVM.chapter7;

/**
 * @author wenhailin
 * @create 2021/3/26-10:44 上午
 */
public class ConstClass {
    static {
        System.out.println("ConstClass init!");
    }

    public static final String HELLO_WORLD = "hello wolrd";

}
