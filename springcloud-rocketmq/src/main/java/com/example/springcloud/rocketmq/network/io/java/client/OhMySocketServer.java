package com.example.springcloud.rocketmq.network.io.java.client;

import com.alibaba.druid.sql.PagerUtils;
import groovy.util.logging.Log;
import groovy.util.logging.Slf4j;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author william
 * @title
 * @desc
 * @date 2021/10/27
 **/
public class OhMySocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OhMySocketServer.class);

    @SneakyThrows
    public static void main(String[] args) {

        // socket, bind, listen
        ServerSocket serverSocket = new ServerSocket(1024, 3);
        // accept
        Socket connection = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        //
        PrintWriter out = new PrintWriter(connection.getOutputStream(), true);

        String request, response;

        while ((request = in.readLine()) != null) {
            System.out.println(request);
            if ("OK".equalsIgnoreCase(request)) {
                break;
            }
        }

        response = "服务端即将关闭该连接";
        out.println(response);
    }
}
