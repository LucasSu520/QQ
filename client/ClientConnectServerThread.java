package com.dltour.qq.client;

import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;


import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ClientConnectServerThread extends  Thread{
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

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
                }else if (ms.getMesType()==MesType.MESSAGE_RECEIVE_CHAT){
                    String chatContent=ms.getContent();
                    String chatSender=ms.getSender();
                    Date day=new Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println(df.format(day)+" "+chatSender);
                    System.out.println(chatContent);
                }else if (ms.getMesType()==MesType.MESSAGE_RECEIVE_ALL_CHAT) {
                    String allChatContent=ms.getContent();
                    String allChatSender=ms.getSender();
                    Date day=new Date();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println(df.format(day)+" "+allChatSender);
                    System.out.println(allChatContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
