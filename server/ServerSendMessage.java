package com.dltour.qq.server;

import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;

public class ServerSendMessage implements Runnable{

    @Override
    public void run() {
        while (true) {
            System.out.println("如果服务器要发送信息，请输入Q,输入EXIT退出群发功能");
            Scanner s = new Scanner(System.in);
            String choice = s.next();
            if (choice.equalsIgnoreCase("q")) {
                System.out.println("请输入要发送的内容:");
                Scanner s1=new Scanner(System.in);
                String sendContent=s1.next();
                Iterator iterator = ManageServerThread.hm.keySet().iterator();
                while (iterator.hasNext()) {
                    Socket receiverSocket = ManageServerThread.hm.get(iterator.next()).getSocket();
                    ObjectOutputStream oos;
                    Message message = new Message();
                    try {
                        oos = new ObjectOutputStream(receiverSocket.getOutputStream());
                        message.setMesType(MesType.MESSAGE_RECEIVE_ALL_CHAT);
                        message.setContent(sendContent);
                        message.setSender("服务器群发");
                        oos.writeObject(message);
                        System.out.println("群发成功！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if (choice.equalsIgnoreCase("EXIT")){
                System.out.println("退出群发消息成功！");
                break;
            }
        }
    }
}
