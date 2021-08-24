package com.example.springcloud.openfeign.consumer.controller;

import com.example.springcloud.openfeign.consumer.service.GitHubClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Williami
 * @description
 * @date 2021/8/20
 */
@RestController
public class GitHubController {

    @Autowired
    private GitHubClient gitHubService;

    @GetMapping(value = "/search/github")
    public String searchGithubRepoByStr(@RequestParam("searchStr") String searchStr) {
        return gitHubService.searchRepo(searchStr);
    }

}
