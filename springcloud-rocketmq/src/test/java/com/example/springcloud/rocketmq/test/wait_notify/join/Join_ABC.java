package com.example.springcloud.rocketmq.test.wait_notify.join;

/**
 * @author Williami
 * @description
 * @date 2022/1/17
 */
class Join_ABC {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(new printABC(null), "A");
            Thread t2 = new Thread(new printABC(t1), "B");
            Thread t3 = new Thread(new printABC(t2), "C");
            t1.start();
            t2.start();
            t3.start();
            //Thread.sleep(10); //这里是要保证只有t1、t2、t3为一组，进行执行才能保证t1->t2->t3的执行顺序。
            t3.join();
            System.out.println("********");
        }

    }

    static class printABC implements Runnable {
        private Thread beforeThread;

        public printABC(Thread beforeThread) {
            this.beforeThread = beforeThread;
        }

        @Override
        public void run() {
            if (beforeThread != null) {
                try {
                    beforeThread.join();
                    System.out.println(Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName());
            }

        }
    }
}

