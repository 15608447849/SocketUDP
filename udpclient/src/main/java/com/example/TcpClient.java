package com.example;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * Created by user on 2016/12/13.
 */

public class TcpClient extends Thread{
    private Socket socket;
    private String ip;
    private final int port = 9000;
    private boolean isConnected = false;
    private DataInputStream dataInputStream;

    public TcpClient(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5 * 1000);
           if (!isConnected){
                socket = new Socket(ip , port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                isConnected=true;
            }
            //打开接受消息线程

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isConnected){
            String msg;
            while (true){
                try {
                    if (dataInputStream.available() > 0) {
                        msg = dataInputStream.readUTF();
                        System.out.println(" 收到 服务器 参数: " + msg);
                    }
                }catch (Exception e){
                 e.printStackTrace();
                }
            }
        }
    }

}
