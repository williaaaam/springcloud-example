package com.example.springcloud.demo.docker.config;

import com.example.springcloud.demo.docker.config.convert.String2LocalDateTimeConverter;
import com.example.springcloud.demo.docker.config.resolver.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Williami
 * @description
 * @date 2021/12/31
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, LocalDateTime.class, new String2LocalDateTimeConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}
