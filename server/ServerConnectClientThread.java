package com.dltour.qq.server;

import com.dltour.qq.client.ManageClientThread;
import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectClientThread extends Thread{
    Socket socket;

    public ServerConnectClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //当循环读取的时候，发送端就不能关闭，如果关闭就会导致其出现异常；
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //后续的message处理
                if (message.getMesType()== MesType.MESSAGE_GET_ONLINE_USERS){
                    String onlineList=ManageServerThread.getOnlineUser();
                    message.setMesType(MesType.MESSAGE_RET_ONLINE_USERS);
                    message.setContent(onlineList);
                    ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
