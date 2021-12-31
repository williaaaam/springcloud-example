package com.example.springcloud.demo.docker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author Williami
 * @description
 * @date 2021/12/7
 */
@RestController
@RequestMapping("docker")
public class DockerDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerDemoController.class);

    @Resource
    HttpServletRequest httpServletRequest;

    @RequestMapping("home")
    public String home(HttpServletRequest request) throws InterruptedException {
        LOGGER.info(">>> {} inject httpServletRequest = {}, instanceOf java.lang.reflect = {}", new Object[]{Thread.currentThread().getName(), httpServletRequest.hashCode(), httpServletRequest instanceof Proxy});
        LOGGER.info(">>> {} inject param httpServletRequest = {}, instanceOf java.lang.reflect = {}", new Object[]{Thread.currentThread().getName(), request.hashCode(), httpServletRequest instanceof Proxy});
        TimeUnit.MILLISECONDS.sleep(900L);
        return "Hello Docker ~";
    }

    @RequestMapping("request")
    public String request() throws InterruptedException {
        LOGGER.info(">>> DockerDemoController#request = {}", httpServletRequest.getParameter("health"));
        TimeUnit.MILLISECONDS.sleep(900L);
        return "Hello Docker ~";
    }

    @RequestMapping("request1")
    public int request1(HttpServletRequest httpServletRequest) {
        return httpServletRequest.hashCode();
    }

    @RequestMapping("request2")
    public int request2(HttpServletRequest httpServletRequest) {
        return httpServletRequest.hashCode();
    }



}
