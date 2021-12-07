package com.example.springcloud.demo.docker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Williami
 * @description
 * @date 2021/12/7
 */
@RestController
@RequestMapping("docker")
public class DockerDemoController {

    @RequestMapping("home")
    public String home() {
        return "Hello Docker ~";
    }

}
