package com.example.springcloud.rocketmq.network.io.java.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author william
 * @title
 * @desc
 * @date 2021/10/27
 **/
@Slf4j
public class OhMySocketClient_DataInput_v3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(OhMySocketClient_DataInput_v3.class);

    @SneakyThrows
    public static void main(String[] args) {

        // socket, bind, listen
        Socket socket = new Socket("localhost", 10240);

        Scanner scanner = new Scanner(System.in);
        try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
            String str = scanner.next();
            outputStream.writeByte(1);
            byte[] data = str.getBytes("UTF-8");
            outputStream.writeInt(data.length);
            outputStream.write(data);
        } catch (Exception e) {

        }

    }
}
