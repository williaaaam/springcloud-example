package com.example.springcloud.rocketmq.test.thread;

public class NotifyDemo {

    private static void sleep(long sleepVal){
        try{
            Thread.sleep(sleepVal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void log(String desc){
        System.out.println(Thread.currentThread().getName() + " : " + desc);
    }

    Object lock = new Object();

    public void startThreadA(){
        new Thread(() -> {
            synchronized (lock){
                log("get lock");
                startThreadB();
                log("start wait");
                try {
                    lock.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                log("get lock after wait");
                log("release lock");
            }
        }, "thread-A").start();
    }

    public void startThreadB(){
        new Thread(()->{
            synchronized (lock){
                log("get lock");
                startThreadC();
                sleep(100);
                log("start notify");
                lock.notify();
                log("release lock");

            }
        },"thread-B").start();
    }

    public void startThreadC(){
        new Thread(() -> {
            synchronized (lock){
                log("get lock");
                log("release lock");
            }
        }, "thread-C").start();
    }

    public static void main(String[] args){
        //代码反复执行多次，结果都是B释放锁之后A会先得到锁，这又是为什么呢？C为何不能先拿到锁呢？
        new NotifyDemo().startThreadA();
    }
}
