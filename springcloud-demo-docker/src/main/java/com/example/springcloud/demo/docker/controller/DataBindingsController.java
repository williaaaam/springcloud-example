package com.example.springcloud.demo.docker.controller;

import com.example.springcloud.demo.docker.annotation.RequestHeader;
import com.example.springcloud.demo.docker.enums.TAHITI;
import com.example.springcloud.demo.docker.vo.LoginUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Williami
 * @description
 * @date 2021/12/31
 */
@RestController
@RequestMapping("bind")
public class DataBindingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBindingsController.class);

    /**
     * Spring默认不支持将String转换为LocalDateTime类型，因此需要自定义转换器并注册到上下文中
     *
     * @param localDateTime
     */
    @RequestMapping("date/{date_str}")
    public void dateBinds(@PathVariable("date_str") LocalDateTime localDateTime) {
        LOGGER.info("{}", localDateTime);
    }

    @RequestMapping("enum/{enum}")
    public void enumBind(@PathVariable("enum") TAHITI tahiti) {
        LOGGER.info("{}", tahiti);
    }


    @RequestMapping("header")
    public void loginUser(@RequestHeader LoginUserVo loginUser) {
        LOGGER.info("{}", loginUser);
    }

}
