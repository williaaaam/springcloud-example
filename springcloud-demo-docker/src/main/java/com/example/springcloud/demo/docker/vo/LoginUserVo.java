package com.example.springcloud.demo.docker.vo;

/**
 * @author Williami
 * @description
 * @date 2021/12/31
 */
public class LoginUserVo {

    private String name;

    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginUserVo{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
