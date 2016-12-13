package tcppackage;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by user on 2016/12/13.
 */

public class TcpSendMessageToClientThread extends Thread{


    private DataOutputStream dataOutputStream;

    public TcpSendMessageToClientThread(Socket socket) {
        try {
            this.dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String msg;
    private boolean isSend;
    public void sendMsg(String msg){
        this.msg = msg;
        isSend = true;
    }
    @Override
    public void run() {
        while (true){
                 if (isSend && msg!=null && dataOutputStream!=null){
                     try {
                         dataOutputStream.writeUTF(msg);
                         dataOutputStream.flush();
                         msg = null;
                         isSend = false;
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
        }
    }
}
