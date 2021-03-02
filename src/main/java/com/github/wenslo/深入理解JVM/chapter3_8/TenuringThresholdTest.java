package com.github.wenslo.深入理解JVM.chapter3_8;

public class TenuringThresholdTest {
    private static final int _1MB = 1024 * 1024;

    /**
     * -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
     */
    public static void testTenuringThreshold() {
        byte[] allocate1, allocate2, allocate3;
        allocate1 = new byte[_1MB / 4];
        allocate2 = new byte[_1MB * 4];
        allocate3 = new byte[_1MB * 4];
        allocate3 = null;
        allocate3 = new byte[_1MB * 4];
    }

    /**
     * -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:+PrintTenuringDistribution
     */
    public static void testTenuringThreshold2() {
        byte[] allocate1, allocate2, allocate3, allocate4;
        allocate1 = new byte[_1MB / 4];
        allocate2 = new byte[_1MB / 4];
        allocate3 = new byte[_1MB * 4];
        allocate4 = new byte[_1MB * 4];
        allocate4 = null;
        allocate4 = new byte[_1MB * 4];
    }

    public static void main(String[] args) {
//        TenuringThresholdTest.testTenuringThreshold();
        TenuringThresholdTest.testTenuringThreshold2();
    }
}
