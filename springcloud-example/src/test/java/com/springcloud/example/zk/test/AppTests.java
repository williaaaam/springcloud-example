package com.springcloud.example.zk.test;

import com.springcloud.example.zk.App;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;

/**
 * api参考：https://www.cnblogs.com/qingyunzong/p/8666288.html
 *
 * @author Williami
 * @description
 * @date 2021/9/27
 */
@Slf4j
@SpringBootTest(classes = App.class)
public class AppTests {

    @Autowired
    CuratorFramework curator;

    @BeforeEach
    public void connect() {
        //curator.start();
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
        curator.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, "中国上海浦东".getBytes(Charset.forName("UTF-8")));


        byte[] bytes = curator.getData().forPath(path);
        System.out.println("Path[" + path + "] getData = " + new String(bytes));
    }


    @Test
    public void getDataForPath() throws Exception {
        curator.create().forPath("/Russia");
        /*byte[] bytes = curator.getData().forPath("/China");
        System.out.println("从/China节点获取配置信息 = " + new String(bytes, Charset.forName("UTF-8")));*/
    }

    @Test
    public void deleteNodeForPath() throws Exception {
        String path = "/China/Shanghai/Pudong";
        // 只能删除叶子节点，否则会抛异常
        // 加上guaranteed表示强制删除
        // Path必须以'/'开始
        Void forPath = curator.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        System.out.println(path + " 节点删除成功");
    }


    @Test
    public void getStatAndUpdateNodeDataForPath() {
        try {
            String path = "/China/Shanghai/Pudong";
            log.info("开始创建节点[" + path + "] Value = " + "中国上海浦东");
            curator.create().forPath(path, "中国上海浦东".getBytes(Charset.forName("UTF-8")));
            log.info(path + "节点创建成功，开始查询节点状态");
            Stat stat = new Stat();
            curator.getData().storingStatIn(stat).forPath(path);
            log.info("stat = {}", stat);
            log.info("更新{}节点数据为中华人民共和国", path);
            curator.setData().forPath(path, "中华人民共和国".getBytes(Charset.forName("UTF-8")));
            log.info("查询更新后的数据 = {}", new String(curator.getData().forPath(path), Charset.forName("UTF-8")));
            log.info("准备删除该节点 = {}", path);
            curator.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            log.info("节点{}删除成功", path);
        } catch (Exception e) {
            log.error("未知异常", e);
        }
    }

}
