package com.example.springcloud.openfeign.consumer.service;

import com.example.springcloud.openfeign.consumer.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Spring容器中的Bean:
 * FeignClientSpecification{name='githubServiceClient', configuration=[class com.example.springcloud.openfeign.config.OpenFeignConfig]}
 * JDK代理：HardCodedTarget(type=GitHubService, name=github-client, url=https://api.github.com)
 *
 * @author Williami
 * @description
 * @date 2021/8/20
 */
@FeignClient(name = "github-client", url = "https://api.github.com", configuration = OpenFeignConfig.class, contextId = "githubServiceClient")
public interface GitHubService {

    /**
     * @param queryStr
     * @return
     */
    @GetMapping(value = "/search/repositories")
    String searchRepo(@RequestParam("q") String queryStr);


}
