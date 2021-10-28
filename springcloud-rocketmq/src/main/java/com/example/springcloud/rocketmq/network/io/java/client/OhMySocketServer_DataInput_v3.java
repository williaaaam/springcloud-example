package com.example.springcloud.rocketmq.network.io.java.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
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
public class OhMySocketServer_DataInput_v3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(OhMySocketServer_DataInput_v3.class);

    @SneakyThrows
    public static void main(String[] args) {

        // socket, bind, listen
        ServerSocket serverSocket = new ServerSocket(10240, 3);

        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        while (true) {
            // 等待客户端连接 accept
            Socket connection = serverSocket.accept();
            executorService.execute(() -> {
                try (DataInputStream in = new DataInputStream(connection.getInputStream())) {
                    byte type = in.readByte();
                    int len = in.readInt();
                    byte[] data = new byte[len];
                    in.readFully(data);
                    log.info(">>> type={}, len={}, data={}", type, len, new String(data));
                } catch (Exception e) {
                    log.error("", e);
                }
                ;
            });
        }
    }
}
