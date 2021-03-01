package com.github.wenslo.深入理解JVM.chapter2;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 直接内存OOM
 */
public class DirectMemoryOOM {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        Field declaredField = Unsafe.class.getDeclaredFields()[0];
        declaredField.setAccessible(true);
        Unsafe unsafe = (Unsafe) declaredField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }
}
