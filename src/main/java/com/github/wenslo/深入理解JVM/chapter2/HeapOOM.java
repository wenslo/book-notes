package com.github.wenslo.深入理解JVM.chapter2;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆内存溢出测试
 * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {
    static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
