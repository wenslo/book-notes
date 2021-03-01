package com.github.wenslo.深入理解JVM.chapter2;

import java.util.HashSet;
import java.util.Set;

/**
 * -Xmx6m
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        short i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }
}
