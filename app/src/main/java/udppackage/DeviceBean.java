package udppackage;

import java.net.Socket;

import tcppackage.TcpSendMessageToClientThread;

/**
     * 设备Bean
     * 只要IP一样，则认为是同一个设备
     */
    public class DeviceBean{
    private String ip;      // IP地址
    private int port;       // 端口
    private String name;    // 设备名称
    private String room;    // 设备所在房间
    private boolean isConnect = false;
    private Socket socket;

    private TcpSendMessageToClientThread sender;
    public  void  sendMessage(String msg){
        if (sender!=null){
            sender.sendMsg(msg);
        }
    }
    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        sender = new TcpSendMessageToClientThread(socket);
        sender.start();
    }

    @Override
        public int hashCode() {
            return ip.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DeviceBean) {
                return this.ip.equals(((DeviceBean)o).getIp());
            }
            return super.equals(o);
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }