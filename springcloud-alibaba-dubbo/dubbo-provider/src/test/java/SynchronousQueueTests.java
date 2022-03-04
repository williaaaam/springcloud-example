import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Williami
 * @description
 * @date 2022/3/2
 */
public class SynchronousQueueTests {


    public static void main(String[] args) throws InterruptedException {

        // true使用TransferQueue;false使用TransferStack
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

        IntStream.rangeClosed(1, 1).forEach(i -> {
            threadPoolExecutor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " 开始执行任务");
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        TimeUnit.SECONDS.sleep(200L);

        IntStream.rangeClosed(1, 1).forEach(i -> {
            threadPoolExecutor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " 开始执行任务");
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        threadPoolExecutor.shutdown();

    }
}
