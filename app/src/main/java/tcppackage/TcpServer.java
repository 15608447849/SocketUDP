package tcppackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by user on 2016/12/13.
 */

public class TcpServer extends Thread {

    private final int port = 9000;
    private ServerSocket serverSocket;//创建ServerSocket
    @Override
    public void run() {
        try {
            if (serverSocket == null) {
                serverSocket = new ServerSocket(port);//监听8080端口，这个程序的通信端口就是8080了
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                //监听连接 ，如果无连接就会处于阻塞状态，一直在这等着
                addClient(serverSocket.accept());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //添加客户端
    public void addClient(Socket client) {

    }
}
