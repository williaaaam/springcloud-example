package com.example.springcloud.rocketmq.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试类的重载和重写
 *
 * @author Williami
 * @description
 * @date 2021/11/10
 */
@Slf4j
public class ClassMethodTests {

    public static void foo(int i) {
        log.info(">>> invoke foo(int)");
    }

    /*public static int foo(Integer i) {
        log.info(">>> invoke foo(Integer)");
        return -1;
    }*/


    public static int foo(Integer num, Object obj) {
        log.info(">>> invoke foo(Integer,Object)");
        return -1;
    }

    public static int foo(Object num, Object... obj) {
        log.info(">>> invoke foo(Object,Object...)");
        return -1;
    }

    public static int foo(String num, Object obj, Object... args) {
        log.info(">>> invoke foo(String,Object,Object...args)");
        return -1;
    }

    public static int foo(Object num, Object obj, Object... args) {
        log.info(">>> invoke foo(Object,Object,Object...args)");
        return -1;
    }

    public static void main(String[] args) {
        Integer integer = 5;
        foo(integer);
        //foo(null, "zhouxinjian","455555");
    }


    interface Customer {
        boolean isVIP();
    }

    class Merchant {
        public Number actionPrice(double price, Customer customer) {
            return null;
        }
    }

    class NaiveMerchant extends Merchant {
        @Override
        public Double actionPrice(double price, Customer customer) {
            return null;
        }
    }


    // --------------------------------------------------------------------

    class Merchant2<T extends Customer> {
        public double actionPrice(double price, T customer) {
            return 0;
        }
    }

    class VIP implements Customer {

        @Override
        public boolean isVIP() {
            return false;
        }
    }

    class VIPOnlyMerchant2 extends Merchant2<VIP> {
        @Override
        public double actionPrice(double price, VIP customer) {
            return 0;
        }
    }


}
