package com.example.springcloud.openfeign;

import com.example.springcloud.openfeign.config.OpenFeignConfig;
import com.example.springcloud.openfeign.service.GitHubService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OpenFeignConfig openFeignConfig;

    @DisplayName("测试GithubService")
    @Test
    public void githubService() {
        // print HardCodedTarget(type=GitHubService, name=github-client, url=https://api.github.com)
        System.out.println(gitHubService);
    }

    @DisplayName("测试 @FeignClient serviceId是否是Bean的名称")
    @Test
    public void githubServiceClientName() {
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println("--------------分割线---------------");
        // githubServiceClient.FeignClientSpecification
        // com.example.springcloud.openfeign.service.GitHubService
        System.out.println(applicationContext.getBean("githubServiceClient.FeignClientSpecification"));
        System.out.println(applicationContext.getBean("com.example.springcloud.openfeign.service.GitHubService"));
    }


    @Test
    public void openFeignConfig() {
        // OpenFeignConfig只被@Configuration注解，则使用CGLIB代理生成子类，对目标方法进行增强
        System.out.println(openFeignConfig);
    }

}
