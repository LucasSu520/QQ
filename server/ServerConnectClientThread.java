package com.dltour.qq.server;


import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectClientThread extends Thread{
    private Socket socket;

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
                    //获得用户在线列表
                    String onlineList=ManageServerThread.getOnlineUser();
                    message.setMesType(MesType.MESSAGE_RET_ONLINE_USERS);
                    message.setContent(onlineList);
                    ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    System.out.println("用户:"+message.getSender()+" 索要在线用户列表，已经返回");
                }else if (message.getMesType()==MesType.MESSAGE_EXIT_SYSTEM){
                    //用户退出系统
                    ManageServerThread.remove(message.getSender());
                    System.out.println("用户:"+message.getSender()+" 退出系统！");
                    break;
                }else if (message.getMesType()==MesType.MESSAGE_SEND_CHAT){
                    //TODO 用户不在线可以先保存到数据库
                     Socket socket= ManageServerThread.hm.get(message.getReceiver()).getSocket();
                     ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                     message.setMesType(MesType.MESSAGE_RECEIVE_CHAT);
                     oos.writeObject(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
