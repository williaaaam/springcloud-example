package com.example.springcloud.rocketmq.test;

/**
 * @author Williami
 * @description
 * @date 2021/11/9
 */
public class ClassInitilizationTests {


    private ClassInitilizationTests() {
    }


    private static class LazyHolder {
        static {
            System.out.println("LazyHolder static静态代码块");
        }

        private static final ClassInitilizationTests INSTANCE = new ClassInitilizationTests();
    }

    public static Object getInstance(boolean flag) {
        if (flag) {
            // 新建数组会导致LazyHolder的加载，但不会导致LazyHolder的初始化
            // 新建数组也不会导致LazyHolder的链接
            return new LazyHolder[2];
        }
        return LazyHolder.INSTANCE;
    }

    static {
        System.out.println("ClassInitilizationTests static 静态代码块");
    }


    public static void main(String[] args) {
        getInstance(true);
        System.out.println("****************************");
        getInstance(false);
    }

}
