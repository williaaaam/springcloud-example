import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2022/3/4
 */
public class ScheduledExecutorServiceTests {


    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledExecutorServiceTests.class);

    public static void main(String[] args) throws InterruptedException {

        // maxPoolSize = Integer.MAX_VALUE
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        long initialDelay = 3L;
        long period = 3;

        // 1. 测试scheduledAtFixedRate

        // 1.1 任务执行时间 < Period: 任务执行时间分别为：initialDelay, initialDelay+period,initialDelay+2*period,...
        Runnable command = new OhMyTask();
        LOGGER.info(">>> {}开始执行任务,{}", Thread.currentThread().getName(), new Date().toLocaleString());
        //scheduledAtFixedRate(scheduledExecutorService, command, initialDelay, period);
        // 1.2 任务执行时间 >= Period 任务执行时间分别为：initialDelay,initialDelay+executionTime,initialDelay+2*executionTime,...
        //period = 2L;
        scheduledAtFixedRate(scheduledExecutorService, command, initialDelay, period);

        // 2. 测试scheduleWithFixedDelay
        // 不管executionTime与period大小关系，任务执行时间分别为：initialDelay, initialDelay+executionTime+period, initialDelay+2*(executionTime+period)
        //scheduledWithFixedDelay(scheduledExecutorService, command, initialDelay, period);

        scheduledExecutorService.awaitTermination(100, TimeUnit.SECONDS);
    }


    private static void scheduledAtFixedRate(ScheduledExecutorService executorService, Runnable command, long initialDelay, long period) {
        executorService.scheduleAtFixedRate(command, initialDelay, period, TimeUnit.SECONDS);
    }

    private static void scheduledWithFixedDelay(ScheduledExecutorService executorService, Runnable command, long initialDelay, long period) {
        executorService.scheduleWithFixedDelay(command, initialDelay, period, TimeUnit.SECONDS);
    }


    static class OhMyTask implements Runnable {
        @Override
        public void run() {
            LOGGER.info(">>> {}开始执行任务,{}", Thread.currentThread().getName(), new Date().toLocaleString());
            int sleepTimeInSeconds = ThreadLocalRandom.current().nextInt(1, 5);
            //int sleepTimeInSeconds = 5;
            try {
                TimeUnit.SECONDS.sleep(sleepTimeInSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOGGER.info(">>> {}休眠{}s后将结束执行任务,{}", Thread.currentThread().getName(), sleepTimeInSeconds, new Date().toLocaleString());

            }
        }
    }
}
