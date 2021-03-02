package com.github.wenslo.深入理解JVM.chapter3_8;

/**
 * -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class EdenAllocation {
    private static final int _1MB = 1024 * 1024;

    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[_1MB * 2];
        allocation2 = new byte[_1MB * 2];
        allocation3 = new byte[_1MB * 2];
        allocation4 = new byte[_1MB * 4];
    }

    public static void main(String[] args) {
        EdenAllocation.testAllocation();
    }
}
