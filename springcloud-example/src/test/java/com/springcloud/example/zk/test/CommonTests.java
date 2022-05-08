package com.springcloud.example.zk.test;

/**
 * @author Williami
 * @description
 * @date 2022/2/26
 */
public class CommonTests {


    public static void main(String[] args) {
        int[] array = m3();
        System.out.println();
    }


    public static int[] m3() {
        int[] array = new int[]{4, 6, 9, 10};
        int k = 5;
        m4(array, k);
        System.out.println(">>> " + k); // 5
        return array;
    }

    public static void m4(int[] array, int k) {
        array[0] = -1;
        k = -1;
    }

    public static Integer[] m1() {
        Integer[] array = new Integer[]{2, 3, 4};
        m2(array);
        return array;
    }

    public static void m2(Integer[] m2) {
        m2[0] = null;
        m2[1] = -1;
    }
}
