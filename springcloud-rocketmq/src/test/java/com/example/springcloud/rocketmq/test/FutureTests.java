package com.example.springcloud.rocketmq.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @author Williami
 * @description
 * @date 2021/10/18
 */
public class FutureTests {


    @Test
    public void test() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        UserInfoService userInfoService = new UserInfoService();
        MedalService medalService = new MedalService();
        long userId = 666L;
        long startTime = System.currentTimeMillis();

        //调用用户服务获取用户基本信息
        FutureTask<UserInfo> userInfoFutureTask = new FutureTask<>(new Callable<UserInfo>() {
            @Override
            public UserInfo call() throws Exception {
                return userInfoService.getUserInfo(userId);
            }
        });
        executorService.submit(userInfoFutureTask);

        Thread.sleep(300); //模拟主线程其它操作耗时

        FutureTask<MedalInfo> medalInfoFutureTask = new FutureTask<>(new Callable<MedalInfo>() {
            @Override
            public MedalInfo call() throws Exception {
                return medalService.getMedalInfo(userId);
            }
        });
        executorService.submit(medalInfoFutureTask);
        // 阻塞调用
        UserInfo userInfo = userInfoFutureTask.get();//获取个人信息结果
        MedalInfo medalInfo = medalInfoFutureTask.get();//获取勋章信息结果

        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
    }


    class UserInfoService {

        public UserInfo getUserInfo(Long userId) throws InterruptedException {
            Thread.sleep(300);//模拟调用耗时
            return new UserInfo("666", "捡田螺的小男孩", 27); //一般是查数据库，或者远程调用返回的
        }
    }

    class MedalService {

        public MedalInfo getMedalInfo(long userId) throws InterruptedException {
            Thread.sleep(500); //模拟调用耗时
            return new MedalInfo("666", "守护勋章");
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class MedalInfo {
        String pos;
        String desc;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class UserInfo {
        String pos;
        String desc;
        int age;
    }

}
