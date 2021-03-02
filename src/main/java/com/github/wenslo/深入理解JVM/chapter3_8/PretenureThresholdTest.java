package com.github.wenslo.深入理解JVM.chapter3_8;

/**
 * -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 */
public class PretenureThresholdTest {
    private static final int _1MB = 1024 * 1024;

    public static void testPretenureThreshold() {
        byte[] allocation;
        allocation = new byte[4 * _1MB];
    }

    public static void main(String[] args) {
        PretenureThresholdTest.testPretenureThreshold();
    }
}
