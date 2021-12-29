package com.example.springcloud.demo.docker.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Williami
 * @description
 * @date 2021/12/15
 */
@Component
public class DemoOncePerRequestFilter extends OncePerRequestFilter implements SmartInitializingSingleton, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoOncePerRequestFilter.class);

    private ApplicationContext applicationContext;

    /**
     * 初始化时会被执行两次
     *
     * @throws ServletException
     */
    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        try {
            LOGGER.info(">>> initFilterBean {}", applicationContext.getBean(CharacterEncodingFilter.class));
        } catch (BeansException e) {
            LOGGER.error("", e);
        }
        LOGGER.info(">>> DemoOncePerRequestFilter初始化");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        FilterConfig filterConfig = super.getFilterConfig();
        ServletContext servletContext = super.getServletContext();
        Environment environment = super.getEnvironment();
        try {
            LOGGER.info(">>> doFilterInternal {}", applicationContext.getBean(CharacterEncodingFilter.class));
        } catch (BeansException e) {
            LOGGER.error("", e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void afterSingletonsInstantiated() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
