package com.example.springcloud.demo.docker.config.resolver;

import com.example.springcloud.demo.docker.annotation.RequestHeader;
import com.example.springcloud.demo.docker.vo.LoginUserVo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义参数解析器
 * @author Williami
 * @description
 * @date 2021/12/31
 */
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeader.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setToken(httpServletRequest.getHeader("token"));
        return loginUserVo;
    }
}
