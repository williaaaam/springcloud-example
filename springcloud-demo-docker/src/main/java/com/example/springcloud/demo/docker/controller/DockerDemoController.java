package com.example.springcloud.demo.docker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;

/**
 * @author Williami
 * @description
 * @date 2021/12/7
 */
@RestController
@RequestMapping("docker")
public class DockerDemoController {

    @Resource
    HttpServletRequest httpServletRequest;

    @RequestMapping("home")
    public String home() {
        System.out.println("httpServletRequest ={} instanceOf Proxy is {}" + httpServletRequest + (httpServletRequest instanceof Proxy));
        return "Hello Docker ~";
    }

}
