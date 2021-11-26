package com.dltour.qq.client;

import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;


import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends  Thread{
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

//    public void addThread(Thread){
//        this.thread=thread;
//    }
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true){
            try {
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                Message ms=(Message) ois.readObject();
                if (ms.getMesType()== MesType.MESSAGE_RET_ONLINE_USERS){
                    String[] onlineList=ms.getContent().split(" ");
                    for (int i=0;i<onlineList.length;i++){
                        System.out.println("用户："+onlineList[i]);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
