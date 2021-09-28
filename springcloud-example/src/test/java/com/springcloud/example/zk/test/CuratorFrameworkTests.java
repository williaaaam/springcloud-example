package com.springcloud.example.zk.test;

import com.springcloud.example.zk.App;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * api参考：https://www.cnblogs.com/qingyunzong/p/8666288.html
 *
 * @author Williami
 * @description
 * @date 2021/9/27
 */
@Slf4j
@SpringBootTest(classes = App.class)
public class CuratorFrameworkTests {

    /**
     * Curator连接zk的客户端对象
     */
    @Autowired
    CuratorFramework client;

    @BeforeEach
    public void connect() {
        //client.start();
    }

    /**
     * 同步创建节点
     * 注意：
     * 1.除非指明创建节点的类型,默认是持久节点
     * 2.ZooKeeper规定:所有非叶子节点都是持久节点,所以递归创建出来的节点,
     * 只有最后的数据节点才是指定类型的节点,其父节点是持久节点
     *
     * @throws Exception
     */
    @DisplayName("递归创建节点，zk默认非叶子节点为持久节点")
    @Test
    public void recursiveCreateNode() throws Exception {
        // 创建一个初始内容不为空的临时节点，可以实现递归创建
        String path = "/China/Shanghai/Pudong";
        client.create()
                .creatingParentsIfNeeded()
                //.withMode(CreateMode.PERSISTENT)
                .forPath(path, "中国上海浦东".getBytes(Charset.forName("UTF-8")));


        byte[] bytes = client.getData().forPath(path);
        System.out.println("Path[" + path + "] getData = " + new String(bytes));
    }

    @DisplayName("查询节点信息")
    @Test
    public void getDataForPath() throws Exception {
        byte[] bytes = client.getData().forPath("/China");
        System.out.println("从/China节点获取配置信息 = " + new String(bytes, Charset.forName("UTF-8")));
    }


    @DisplayName("删除节点")
    @Test
    public void deleteNodeForPath() throws Exception {
        String path = "/China/Shanghai/Pudong";
        // 只能删除叶子节点，否则会抛异常
        // 加上guaranteed表示强制删除
        // Path必须以'/'开始
        Void forPath = client
                .delete()
                .guaranteed()
                // 回调
                .inBackground((client, event) -> {
                    System.out.println("我被删除了");
                    System.out.println(event);
                })
                //.deletingChildrenIfNeeded()
                .forPath(path);
        System.out.println(path + " 节点删除成功");
    }

    @DisplayName("create,stat,set,delete,get")
    @Test
    public void getStatAndUpdateNodeDataForPath() {
        try {
            String path = "/China/Shanghai/Pudong";
            log.info("开始创建节点[" + path + "] Value = " + "中国上海浦东");
            client.create()
                    .creatingParentsIfNeeded()
                    .forPath(path, "中国上海浦东".getBytes(Charset.forName("UTF-8")));
            log.info(path + "节点创建成功，开始查询节点状态");
            Stat stat = new Stat();
            client.getData().storingStatIn(stat).forPath(path);
            log.info("stat = {}", stat);
            log.info("更新{}节点数据为中华人民共和国", path);
            client.setData().forPath(path, "中华人民共和国".getBytes(Charset.forName("UTF-8")));
            log.info("查询更新后的数据 = {}", new String(client.getData().forPath(path), Charset.forName("UTF-8")));
            log.info("准备删除该节点 = {}", path);
            client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/China");
            log.info("节点{}删除成功", path);
        } catch (Exception e) {
            log.error("未知异常", e);
        }
    }


    @DisplayName("查询节点stat")
    @Test
    public void getStat() throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/");
        log.info(">>> stat = {}", stat);
    }

    @DisplayName("查询和修改version保持一致")
    @Test
    public void getDataForVersion() throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/");
        log.info(">>> stat = {}", stat);
        // stat.version等同于 dataVersion
        //client.setData().withVersion(stat.getVersion()).forPath("/", "2021-09-26".getBytes(Charset.forName("UTF-8")));
        client.setData()
                // version 不匹配的话,KeeperErrorCode = BadVersion for /CuratorFramework
                .withVersion(100)
                .forPath("/", "2021-09-26".getBytes(Charset.forName("UTF-8")));
    }


    @DisplayName("查询子节点[名]")
    @Test
    public void getChildren() throws Exception {
        List<String> strings = client.getChildren().forPath("/");
        strings.stream().forEach(System.out::println);
    }


    // ==============================================================Watcher机制===================================================================
    @DisplayName("NodeCache：给指定单个节点注册监听器")
    @Test
    public void nodeCache() throws Exception {
        // 1. 创建NodeCache对象,启用数据压缩
        /**
         * @param client curator client
         * @param path the full path to the node to cache 注册监听路径
         * @param dataIsCompressed if true, data in the path is compressed
         */
        NodeCache nodeCache = new NodeCache(client, "/", false);
        // 2. 注册监听器
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            /**
             * 只有创建、删除该节点、更新该节点的数据时，才会触发监听器
             *
             * @throws Exception
             */
            @Override
            public void nodeChanged() throws Exception {
                log.info("节点变化了");
                // 获取当前数据
                // 1. 如果是修改了节点的数据内容，则data修改之后的值
                // 2. 如果是删除节点，则nodeCache.getCurrentData()会报空指针异常
                byte[] data = nodeCache.getCurrentData().getData();
                log.info(">>>{}", new String(data));
            }
        });
        // 3. 开启监听,buildInitial:true则开启监听时加载缓存数据
        nodeCache.start(true);
        while (true) {

        }

    }

    @DisplayName("监听某个节点下的子节点")
    @Test
    public void pathChildrenCache() {
        // 1. 创建监听对象
        try (PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/", false);) {
            // 2. 注册监听器
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    log.info("event = {}", event);
                    log.info("子节点变化了");
                    if (event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                        log.info(">>> 获取的数据{}", event.getData().getData());
                    }
                }
            });

            pathChildrenCache.start();

            while (true) {
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }


    /**
     * TreeCache = NodeCache+PathChildrenCache
     */
    @DisplayName("监听某个节点自己和所有子节点")
    @Test
    public void treeCache() {
        TreeCache treeCache = new TreeCache(client, "/");

        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {

            }
        });
    }


    /**
     * 分布式锁用来解决跨机器进程之间的数据同步问题
     */
    @DisplayName("zk实现分布式锁")
    @Test
    public void lock() throws InterruptedException {
        String[] channels = {"飞猪", "拼多多", "智行", "携程"};
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        IntStream.range(1, 12).boxed().forEach((i) -> executorService.execute(new Ticket(channels[ThreadLocalRandom.current().nextInt(11) % 4], 1)));
        executorService.awaitTermination(20L, TimeUnit.SECONDS);
    }


    private AtomicInteger total = new AtomicInteger(10);


    /**
     * 模拟12306
     */
    private class Ticket implements Runnable {

        // 资源在哪里，锁就加在那里

        private String channel;

        private int acquire;

        private InterProcessMutex lock;

        public Ticket(String channel, int acquire) {
            this.channel = channel;
            this.acquire = acquire;
            CuratorFramework client = CuratorFrameworkFactory
                    .builder()
                    .connectString("139.196.113.146:2181,139.196.113.146:2182,139.196.113.146:2183")
                    .retryPolicy(new RetryNTimes(5, 1000))
                    .sessionTimeoutMs(60 * 1000)
                    .connectionTimeoutMs(15 * 1000)
                    .namespace("Ticket12306")
                    .build();
            // 开启连接
            client.start();
            lock = new InterProcessMutex(client, "/lock");
        }


        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                if (total.get() <= 0) {
                    log.info(">>> 票已售罄！{}线程退出", Thread.currentThread().getName());
                    break;
                }
                try {
                    // 获取锁;如果获取不到锁，则阻塞直到锁可用，或者超时
                    lock.acquire(2, TimeUnit.SECONDS);
                    log.info(">>> {}获得了锁", Thread.currentThread().getName());
                    if (total.get() > 0) {
                        log.info(">>> [{}],[{}]成功买了{}张票，当前还剩票{}", "", channel, acquire, total.decrementAndGet());
                        // 成功买到了票，退出
                        break;
                    }

                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    // 释放锁
                    lock.release();
                }
            }
        }
    }


    @AfterEach
    public void close() {
        client.close();
    }

}
