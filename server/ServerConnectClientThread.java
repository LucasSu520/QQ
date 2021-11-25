package com.dltour.qq.server;

import com.dltour.qq.common.Message;

import java.io.ObjectInputStream;
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
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //后续的message处理
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
