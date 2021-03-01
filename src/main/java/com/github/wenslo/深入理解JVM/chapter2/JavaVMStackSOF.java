package com.github.wenslo.深入理解JVM.chapter2;

/**
 * -Xss160k
 */
public class JavaVMStackSOF {
    private int stackLength = 1;

    private void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF stackSOF = new JavaVMStackSOF();
        try {
            stackSOF.stackLeak();
        } catch (Throwable t) {
            System.out.println("stack length:" + stackSOF.stackLength);
            throw t;
        }
    }

}
