package com.example.springcloud.rocketmq.network.io.java.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author william
 * @title
 * @desc
 * @date 2021/10/27
 **/
@Slf4j
public class OhMyClient {


    @SneakyThrows
    public static void main(String[] args) {

        Socket socket = new Socket("127.0.0.1", 10240);

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // 通过标准输入获取字符流
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

        BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        while (true) {
            String message = in.readLine();
            out.write(message);
            // 告诉服务端本次数据发送完成
            out.write(System.lineSeparator());
            TimeUnit.SECONDS.sleep(1L);
            out.flush();
            socket.shutdownOutput();
            log.info("[Server]:{}", incoming.readLine());
        }

        /*String message = "Hello, Server";

        out.write(message);

        // 刷新输出流
        out.flush();
        // 主动关闭发送方的单向连接,此时客户端还是能接收到服务端反馈的消息
        socket.shutdownOutput();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println(bufferedReader.readLine());*/
    }
}
