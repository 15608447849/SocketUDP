package com.example;

import java.net.InetSocketAddress;

public class MyClass {

        public static void main(String [] args){

                new DeviceWaitingSearch("平哥的电脑", "颖网研发部"){
                    @Override
                    public void onDeviceSearched(InetSocketAddress socketAddr) {
                        System.out.println("已上线，搜索主机：" + socketAddr.getAddress().getHostAddress() + ":" + socketAddr.getPort());
                        //进行tcp连接
                        new TcpClient(socketAddr.getAddress().getHostAddress()).start();
                    }
                }.start();

        }
}
