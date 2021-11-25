package com.dltour.qq.client;

import com.dltour.qq.common.Message;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends  Thread{
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true){
            try {
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                Message ms=(Message) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
