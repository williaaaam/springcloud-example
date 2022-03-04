import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2022/3/1
 */
public class ArrayBlockingQueueTests {


    public static void main(String[] args) throws InterruptedException {
        /*ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2, true);
        blockingQueue.offer(1);
        blockingQueue.offer(1);
        System.out.println(blockingQueue.remainingCapacity());
        // 自旋
        blockingQueue.put(1);*/

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        threadPoolExecutor.execute(() -> {
            try {
                synchronousQueue.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TimeUnit.SECONDS.sleep(5);

        threadPoolExecutor.submit(() -> {
            try {
                System.out.println(synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println();
        TimeUnit.SECONDS.sleep(2);

        threadPoolExecutor.shutdown();
    }
}
