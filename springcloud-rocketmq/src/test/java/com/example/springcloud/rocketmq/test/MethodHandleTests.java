package com.example.springcloud.rocketmq.test;

import lombok.SneakyThrows;

import java.lang.invoke.*;

/**
 * @author Williami
 * @description
 * @date 2021/11/11
 */
public class MethodHandleTests {


    private String name = "2333";

    public void foo(Object object) {
        new Exception().printStackTrace();
    }

    /**
     * 方法句柄的权限检查发生在创建过程中，相较于反射调用节省了调用时反复权限检查的开销
     *
     * @return
     */
    public static MethodHandles.Lookup lookup() {
        return MethodHandles.lookup();
    }

    @SneakyThrows
    public static void main(String[] args) {
       /* MethodType methodType = MethodType.methodType(void.class, Object.class);
        MethodHandle methodHandle = lookup().findVirtual(MethodHandleTests.class, "foo", methodType);
        methodHandle.invokeExact(new MethodHandleTests(), new Object());*/

        startRace(new Horse());
    }

    /**
     * 适配器是一个 LambdaForm，我们可以通过添加虚拟机参数将之导出成 class 文件（-Djava.lang.invoke.MethodHandle.DUMP_CLASS_FILES=true
     * -XX:+UnlockDiagnosticVMOptions -XX:+ShowHiddenFrames参数打印被jvm隐藏了的栈信息
     *
     * @param mh
     * @param s
     * @throws Throwable
     */
    public void test(MethodHandle mh, String s) throws Throwable {
        /**
         *方法句柄 API 有一个特殊的注解类 @PolymorphicSignature。在碰到被它注解的方法调
         * 用时，Java 编译器会根据所传入参数的声明类型来生成方法描述符，而不是采用目标方法
         * 所声明的描述符。
         */
        mh.invokeExact(s);
        mh.invokeExact((Object) s);
    }


    static class Horse {
        public void race() {
            System.out.println("Horse.race()");
        }
    }

    static class Deer {
        public void race() {
            System.out.println("Deer.race()");
        }
    }


    public static void startRace(Object obj) {
        /**
         * 对应的字节码，invokedynamic指令最终调用的是方法句柄，方法句柄把调用者当成第一个参数
         *  0: aload_0
         *  1: invokedynamic #80, 0 // race:(Ljava/lang/Object;)V
         * 6: return
         */
        // aload obj
        // invokedynamic race()
    }

    /**
     * 启动方法:jvm将方法调用和方法句柄的链接暴露给应用程序
     *
     * @param l            lookup实例
     * @param methodName   目标方法名
     * @param callSiteType 调用点能够链接的方法句柄的类型
     * @return
     */
    @SneakyThrows
    public static CallSite bootstrap(MethodHandles.Lookup l, String methodName, MethodType callSiteType) {
        // 当前方法句柄
        MethodHandle mh = l.findVirtual(Horse.class, methodName, MethodType.methodType(void.class));
        // CallSite链接至新的方法句柄
        return new ConstantCallSite(mh.asType(callSiteType));
    }

}
