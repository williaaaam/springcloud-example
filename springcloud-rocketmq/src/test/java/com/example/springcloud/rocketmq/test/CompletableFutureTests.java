package com.example.springcloud.rocketmq.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Williami
 * @description
 * @date 2021/10/14
 */
@Slf4j
public class CompletableFutureTests {

    /**
     * 异步任务:这种异步设计的方法，可以很好地解决 IO 等待的问题
     */
    @SneakyThrows
    @Test
    public void supplyAsync() {
        // 默认使用内置ForkJoinPool#commonPool，根据supplier构建任务
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "CompletableFutureTest");
        // 使用自定义线程+Supplier构建任务
        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> "CompletableFutureTest2", new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>()));
        TimeUnit.SECONDS.sleep(2);
        String futureNow = completableFuture2.getNow("Now");
        log.info(">>> futureNow = {}", futureNow);
    }

    /**
     * 同步任务
     */
    @SneakyThrows
    @Test
    public void runnableSync() {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("RunnableSync");
        });

        Void unused = completableFuture.get(10L, TimeUnit.SECONDS);
    }


    @Test
    public void syncAndAsync() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Void> rFuture = CompletableFuture
                .runAsync(() -> {
                    System.out.println("hello runAsync");
                    try {
                        TimeUnit.SECONDS.sleep(5L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, executor);

        //supplyAsync的使用
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("hello");
                    try {
                        TimeUnit.SECONDS.sleep(5L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "siting";
                }, executor);

        //阻塞等待，runAsync 的future 无返回值，输出null
        System.out.println(rFuture.join());

        System.out.println("*******************************");


        //阻塞等待
        String name = future.join();
        System.out.println(name);
        executor.shutdown(); // 线程池需要关闭
    }

    @DisplayName("常量值作为CompletableFuture")
    @Test
    public void constantValue() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.completedFuture(null);
        System.out.println(completableFuture.get());
    }

    @SneakyThrows
    @DisplayName("线程串行执行:1. 任务完成则运行action，不关心上一个任务的结果，无返回值")
    @Test
    public void thenRun() {
        CompletableFuture<Void> completableFuture = CompletableFuture
                .supplyAsync(() -> "supplyAsync")
                .thenRunAsync(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(5L);
                        System.out.println("thenRun");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .thenRunAsync(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(5L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thenRunAsync");
                });

        //completableFuture.get();
    }

    @SneakyThrows
    @DisplayName("线程串行执行:2. 任务完成则运行action，依赖上一个任务的结果，无返回值")
    @Test
    public void thenAcceptAsync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> future = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(2L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "hello siting";
                }, executor)
                .thenAcceptAsync(System.out::println, executor);
        future.get();
        executor.shutdown();
    }

    @DisplayName("线程串行：任务完成则运行fn,依赖上一个任务的结果，有返回值")
    @Test
    public void thenApply() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "hello world", executor)
                .thenApplyAsync(data -> {
                    System.out.println(data);
                    return "OK";
                }, executor);
        System.out.println(future.join());
        executor.shutdown();
    }

    @DisplayName("线程串行：3. thenCompose类似thenApply,任务完成则运行fn,依赖上一个任务的结果，有返回值")
    @Test
    public void thenCompose() {
        // 第一个异步任务，常量任务
        CompletableFuture<String> f = CompletableFuture.completedFuture("OK");
        // 第二个异步任务
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "hello world", executor)
                .thenComposeAsync(data -> {
                    System.out.println(data);
                    return f; // 使用第一个任务作为返回
                }, executor);
        System.out.println(future.join());
        executor.shutdown();
    }

    /**
     * 线程并行执行，两个CompletableFuture并行执行完，然后执行action，不依赖上两个任务的结果，无返回值
     */
    @SneakyThrows
    @Test
    public void runAfterBoth() {

        CompletableFuture first = CompletableFuture.completedFuture("First");

        CompletableFuture<Void> completableFuture = CompletableFuture
                // 第二个异步任务
                .supplyAsync(() -> "hhhhhh")
                //.runAsync(() -> System.out.println("Hello"))
                // ()-> System.out.println("RunAfterBothAsync")是第四个任务
                .runAfterBothAsync(first, () -> System.out.println("RunAfterBothAsync"));
        completableFuture.join();
    }

    /**
     * 线程并行执行：两个CompletableFuture并行执行完，然后执行action，依赖上两个任务的结果，无返回值
     */
    @SneakyThrows
    @Test
    public void thenAcceptBoth() {
        //第一个异步任务，常量任务
        CompletableFuture<String> first = CompletableFuture.completedFuture("hello world");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello siting", executor)
                // (w, s) -> System.out.println(s) 是第三个任务
                // 调用方任务和other并行完成后执行action，action再依赖消费两个任务的结果，无返回值
                .thenAcceptBothAsync(first, (s, w) -> System.out.println(s + w), executor);
        executor.shutdown();
    }

    /**
     * 线程并行执行:调用方任务和other并行完成后，执行fn，fn再依赖消费两个任务的结果，有返回值
     */
    @Test
    public void thenCombine() {
        //第一个异步任务，常量任务
        CompletableFuture<String> first = CompletableFuture.completedFuture("hello world");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<String> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello siting", executor)
                // (w, s) -> System.out.println(s) 是第三个任务
                .thenCombineAsync(first, (s, w) -> {
                    System.out.println(s + w);
                    return "OK";
                }, executor);
        System.out.println(future.join());
        executor.shutdown();
    }


    /**
     * 线程并行执行，谁先执行完则谁触发下一任务（二者选其最快）
     */
    @Test
    public void runAfterEither() {
//第一个异步任务，休眠1秒，保证最晚执行晚
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            System.out.println("hello world");
            return "hello world";
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> {
                    System.out.println("hello siting");
                    return "hello siting";
                }, executor)
                //() ->  System.out.println("OK") 是第三个任务
                .runAfterEitherAsync(first, () -> System.out.println("OK"), executor);
        executor.shutdown();
    }

    /**
     * 线程并行执行，谁先执行完则谁触发下一任务（二者选其最快）:上一个任务或者other任务完成, 运行fn，依赖最先完成任务的结果，有返回值
     */
    @Test
    @SneakyThrows
    public void applyToEither() {
        //第一个异步任务，休眠1秒，保证最晚执行晚
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            return "hello world";
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<String> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello siting", executor)
                // data ->  System.out.println(data) 是第三个任务
                .applyToEitherAsync(first, data -> {
                    System.out.println(data);
                    return "OK";
                }, executor);
        TimeUnit.SECONDS.sleep(5L);
        System.out.println(future); // java.util.concurrent.CompletableFuture@2e005c4b[Completed normally]
        executor.shutdown();
    }


    /**
     * 5. 处理任务结果或者异常:如果之前的处理环节有异常问题，则会触发exceptionally的调用相当于 try...catc
     */
    @Test
    public void exceptionally() {
        CompletableFuture<Integer> first = CompletableFuture
                .supplyAsync(() -> {
                    if (true) {
                        throw new RuntimeException("main error!");
                    }
                    return "hello world";
                })
                .thenApply(data -> 1)
                .exceptionally(e -> {
                    e.printStackTrace(); // 异常捕捉处理，前面两个处理环节的日常都能捕获
                    return 0;
                });

    }

    /**
     * handle-任务完成或者异常时运行fn，返回值为fn的返回;相比exceptionally而言，即可处理上一环节的异常也可以处理其正常返回值
     */
    @Test
    public void handle() {
        CompletableFuture<Integer> first = CompletableFuture
                .supplyAsync(() -> {
                    if (true) {
                        throw new RuntimeException("main error!");
                    }
                    return "hello world";
                })
                .thenApply(data -> 1)
                .handleAsync((data, e) -> {
                    //e.printStackTrace(); // 异常捕捉处理
                    return data;
                });
        System.out.println(first.join());
    }


    /**
     * whenComplete与handle的区别在于，它不参与返回结果的处理，把它当成监听器即可
     * 即使异常被处理，在CompletableFuture外层，异常也会再次复现
     * 使用whenCompleteAsync时，返回结果则需要考虑多线程操作问题，毕竟会出现两个线程同时操作一个结果
     * <p>
     * 作者：潜行前行
     * 链接：https://juejin.cn/post/6902655550031413262
     * 来源：稀土掘金
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    @Test
    public void whenComplete() {
        CompletableFuture<AtomicBoolean> first = CompletableFuture
                .supplyAsync(() -> {
                    if (true) {
                        throw new RuntimeException("main error!");
                    }
                    return "hello world";
                })
                .thenApply(data -> new AtomicBoolean(false))
                .whenCompleteAsync((data, e) -> {
                    // 异常捕捉处理, 但是异常还是会在外层复现
                    System.out.println(e.getMessage());
                });
        first.join();
    }


    /**
     * 6. 多个简单任务的组合
     */
    @Test
    public void allOfAnyOf() {
        CompletableFuture<Void> future = CompletableFuture
                // 所有任务需要执行完毕才会触发nextTask执行
                .allOf(CompletableFuture.completedFuture("A"),
                        CompletableFuture.completedFuture("B"));

        //全部任务都需要执行完
        System.out.println(future.join());
        CompletableFuture<Object> future2 = CompletableFuture
                // 任意一条任务执行完毕都会触发nextTask的执行
                .anyOf(CompletableFuture.completedFuture("C"),
                        CompletableFuture.completedFuture("D"));

        //其中一个任务行完即可
        System.out.println(future2.join());

    }

    @Test
    public void cancel() {
        CompletableFuture<Integer> future = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    return "hello world";
                })
                .thenApply(data -> 1);

        System.out.println("任务取消前:" + future.isCancelled());
        // 如果任务未完成,则返回异常,需要对使用exceptionally，handle 对结果处理
        future.cancel(true);
        System.out.println("任务取消后:" + future.isCancelled());
        future = future.exceptionally(e -> {
            e.printStackTrace();
            return 0;
        });
        System.out.println(future.join());
    }


    // ========================================================场景模拟=====================================================================
    @DisplayName("模拟thenCombine")
    @Test
    public void mockThenCombine() {
        CompletableFuture<String> orderCompletable = CompletableFuture
                .runAsync(() -> order())
                .thenCombineAsync(
                        CompletableFuture.supplyAsync(() -> run()),
                        // order()和run()都运行完才去执行sleep()
                        (s, w) -> sleep());

        System.out.println(orderCompletable.join());

    }

    @Test
    public void thenAccept() {
        ExecutorService executorService = new ThreadPoolExecutor(5, 10, 5L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            int a = 0;
            int b = 666;
            int c = b / a;
            return true;
        }, executorService).thenAccept(System.out::println);

        //如果不加 get()方法这一行，看不到异常信息
        //future.get();
    }

    @SneakyThrows
    public static void order() {
        log.info(">>> {}准备买机票", Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(5L);
    }

    @SneakyThrows
    public static String run() {
        log.info(">>> {}下了飞机开跑", Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(4L);
        return "Run";
    }

    @SneakyThrows
    public static String sleep() {
        TimeUnit.SECONDS.sleep(2L);
        log.info(">>> {}到了酒店啦，开始碎觉", Thread.currentThread().getName());
        return "Sleep";
    }

    @Test
    public void supply() {

        CompletableFuture.completedFuture("000")
                .thenApply(r -> r)
                .whenComplete((r, e) -> System.out.println(format(r)));

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
                    return "111";
                })
                .thenApply(r -> r)
                .whenComplete((r, e) -> System.out.println(format(r)));

        completableFuture.join();
    }

    private static String format(String msg) {
        return String.format("[%s] %s", Thread.currentThread().getName(), msg);
    }

    @SneakyThrows
    @Test
    public void completionStack() {
        // 这是一个未完成的CompletableFuture
        CompletableFuture<String> base = new CompletableFuture<>();
        log.info("start another thread to complete it");
        new Thread(
                () -> {
                    log.info("will complete in 1 sec");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //base.complete("completed");
                })
                .start();
        // 将base.complete("completed")注释掉后，主线程将一直卡在这里
        log.info(base.get());

        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello world f1");
            sleep(1); // TimeUnit.SECONDS.sleep(1)
            return "f1";
        });
        CompletableFuture<String> f2 = f1.thenApply(r -> {
            System.out.println(r);
            sleep(1);
            return "f2";
        });

        CompletableFuture<String> f3 = f2.thenApply(r -> {
            System.out.println(r);
            sleep(1);
            return "f3";
        });

        CompletableFuture<String> f4 = f1.thenApply(r -> {
            System.out.println(r);
            sleep(1);
            return "f4";
        });
        CompletableFuture<String> f5 = f4.thenApply(r -> {
            System.out.println(r);
            sleep(1);
            return "f5";
        });
        CompletableFuture<String> f6 = f5.thenApply(r -> {
            System.out.println(r);
            sleep(1);
            return "f6";
        });

    }

    @SneakyThrows
    private static void sleep(long duration) {
        TimeUnit.SECONDS.sleep(duration);
    }

    @SneakyThrows
    @Test
    public void thenAcceptAndThenApply() {
        CompletableFuture<String> base = new CompletableFuture<>();
        CompletableFuture<String> future =
                base.thenApply(
                        s -> {
                            log("2");
                            return s + " 2";
                        });
        base.thenAccept(s -> log(s + "a")).thenAccept(aVoid -> log("b"));
        base.thenAccept(s -> log(s + "c")).thenAccept(aVoid -> log("d"));
        //base.complete("1");

        //log.info("base result: {}", base.get());
        //log.info("future result: {}", future.get());

    }

    private static void log(String msg) {
        log.info(">>> [{}] {}", Thread.currentThread().getName(), msg);
    }

    @SneakyThrows
    @Test
    public void commonSequence() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        completableFuture.thenAccept((s) -> log("Completion 0"));
        completableFuture
                .thenAccept((s) -> log("Completion 3"))
                .thenAccept((s) -> {
                    sleep(2);
                    log("Completion 4");
                })
                .thenAcceptAsync((s) -> log("Completion 5"))
                .thenAcceptAsync((s) -> log("Completion 1"));
        completableFuture
                .thenAcceptAsync((s) -> {
                    sleep(10);
                    log("Completion 6");
                })
                .thenAcceptAsync((s) -> log("Completion 7"))
                .thenAcceptAsync((s) -> {
                    sleep(10);
                    log("Completion 8");
                })
                .thenAcceptAsync((s) -> log("Completion 2"));

        // 标识任务已完成
        completableFuture.complete("base");
        System.out.println(completableFuture.get());
    }

}
