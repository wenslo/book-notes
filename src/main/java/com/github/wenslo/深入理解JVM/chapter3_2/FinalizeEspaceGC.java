package com.github.wenslo.深入理解JVM.chapter3_2;

/**
 * @author wenhailin
 * @create 2021/3/25-2:18 下午
 */
public class FinalizeEspaceGC {
    private static FinalizeEspaceGC SAVE_HOOK = null;

    public void isAlive() {
        System.out.println("Yes, i am still alive :)");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method execute.");
        FinalizeEspaceGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEspaceGC();
        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if (null != SAVE_HOOK) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }

        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if (null != SAVE_HOOK) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }
    }

}
