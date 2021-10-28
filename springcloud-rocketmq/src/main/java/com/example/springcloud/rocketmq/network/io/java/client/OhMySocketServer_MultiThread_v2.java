package com.example.springcloud.rocketmq.network.io.java.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author william
 * @title
 * @desc
 * @date 2021/10/27
 **/
@Slf4j
public class OhMySocketServer_MultiThread_v2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(OhMySocketServer_MultiThread_v2.class);

    @SneakyThrows
    public static void main(String[] args) {

        // socket, bind, listen
        ServerSocket serverSocket = new ServerSocket(10240, 3);

        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        while (true) {
            // 等待客户端连接 accept
            Socket connection = serverSocket.accept();
            executorService.execute(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                     PrintWriter out = new PrintWriter(connection.getOutputStream(), true);) {
                    String request;
                    while ((request = in.readLine()) != null) {
                        log.info("[Client]: {}", request);
                        out.println("Hello, " + request);
                        out.flush();
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        }
    }
}
