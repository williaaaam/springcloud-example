package com.example.springcloud.rocketmq.test;

import org.junit.jupiter.api.Test;

import java.io.Flushable;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试泛型方法和无界通配符?
 *
 * @author Williami
 * @description
 * @date 2021/10/15
 */
public class GenericMethodAndWildcardTests {

    /**
     * @param dest
     * @param src
     * @param <T>
     */
    public static <T> void copy1(List<T> dest, List<? extends T> src) {
    }

    public static <T, S extends T> void copy2(List<T> dest, List<S> src) {
    }

    public static <T, S extends T> void copy3(List<?> dest) {
        Object o = dest.get(0);
    }

    public static <T, S extends T> void copy4(List dest) {
        Object o = dest.get(0);
    }

    public static <T, S extends T> void copy5(List<T> dest) {
        T t = dest.get(0);
    }

    @Test
    public void test1() {

        List<Number> list = new ArrayList<>();

        List<Integer> integerList = new ArrayList<>();

        list.add(1);
        integerList.add(1);

        copy1(list, integerList);

        copy2(list, integerList);

        //copy1(integerList, list); compile error

        //copy2(integerList, list); compile error


        List<? extends Number> foo3 = new ArrayList<Number>();  // Number "extends" Number (in this context)
        //List<? extends Number> foo3 = new ArrayList<Integer>(); // Integer extends Number
        //List<? extends Number> foo3 = new ArrayList<Double>();  // Double extends Number
        //List<? super Integer> foo3 = new ArrayList<Integer>();  // Integer is a "superclass" of Integer (in this context)
        //List<? super Integer> foo3 = new ArrayList<Number>();   // Number is a superclass of Integer
        //List<? super Integer> foo3 = new ArrayList<Object>();   // Object is a superclass of Integer

        copy3(list);
        copy4(list);
        copy5(list);
    }

    public static <T> void copy(List<? super T> dest, List<? extends T> src) {  // bounded wildcard parameterized types
        for (int i = 0; i < src.size(); i++)
            dest.set(i, src.get(i));
    }

    public static void copy6(List<?> dest, List<?> src) {  // uses unbounded wildcards
        for (int i = 0; i < src.size(); i++) {
            // the compiler issues an error message:  get() returns an Object and set() asks for a more specific, yet unknown type.
            //dest.set(i, src.get(i)); // error: illegal argument types
        }
    }

    class Box<T extends Appendable & Flushable> {

        private T theObject;

        public Box(T arg) {
            theObject = arg;
        }

        public Box(Box<? extends T> box) {
            theObject = box.theObject;
        }

    }


}
