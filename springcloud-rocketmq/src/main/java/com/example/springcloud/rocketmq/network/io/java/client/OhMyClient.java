package com.example.springcloud.rocketmq.network.io.java.client;

import io.netty.handler.codec.string.LineSeparator;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystem;

/**
 * @author william
 * @title
 * @desc
 * @date 2021/10/27
 **/
public class OhMyClient {


    @SneakyThrows
    public static void main(String[] args) {

        Socket socket = new Socket("127.0.0.1", 1024);

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        // 通过标准输入获取字符流
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

        while (true) {
            String message = in.readLine();
            out.write(message);
            // 告诉服务端本次数据发送完成
            out.write(System.lineSeparator());
            out.flush();
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
