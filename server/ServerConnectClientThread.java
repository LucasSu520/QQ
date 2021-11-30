package com.dltour.qq.server;


import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;
import com.dltour.qq.common.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;

    public ServerConnectClientThread(Socket socket,String userId) {
        this.socket = socket;
        this.userId=userId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //判断离线列表中有没有离线消息
                ObjectOutputStream oos=null;
                if (OfflineMessage.hm.get(userId)!=null){
                    System.out.println("fa song li xian xiao xi");
                    oos=new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(OfflineMessage.hm.get(userId));
                    OfflineMessage.hm.remove(userId);
                }
                //当循环读取的时候，发送端就不能关闭，如果关闭就会导致其出现异常；
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //后续的message处理
                if (message.getMesType()== MesType.MESSAGE_GET_ONLINE_USERS){
                    //获得用户在线列表
                    String onlineList=ManageServerThread.getOnlineUser();
                    message.setMesType(MesType.MESSAGE_RET_ONLINE_USERS);
                    message.setContent(onlineList);
                    oos=new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    System.out.println("用户:"+message.getSender()+" 索要在线用户列表，已经返回");
                }else if (message.getMesType()==MesType.MESSAGE_EXIT_SYSTEM){
                    //用户退出系统
                    ManageServerThread.remove(message.getSender());
                    System.out.println("用户:"+message.getSender()+" 退出系统！");
                    break;
                }else if (message.getMesType()==MesType.MESSAGE_SEND_CHAT){
                    Socket socket;
                    if (null==ManageServerThread.hm.get(message.getReceiver())){
                        if (QQServer.validUsers.get(message.getReceiver())!=null){
                            System.out.println("该用户已离线，以保存到离线服务器");
                            Date date=new Date();
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                            sdf.format(date);
                            message.setMesType(MesType.MESSAGE_RECEIVE_CHAT);
                            message.setDate(date.toString());
                            OfflineMessage.hm.put(message.getReceiver(),message);
                            continue;
                        }
                        ObjectOutputStream oos1=new ObjectOutputStream(ManageServerThread.hm.get(message.getSender()).socket.getOutputStream());
                        message.setContent("用户不存在，发送失败！");
                        oos1.writeObject(message);
                        continue;
                    }
                    socket=ManageServerThread.hm.get(message.getReceiver()).getSocket();
                    oos=new ObjectOutputStream(socket.getOutputStream());
                     message.setMesType(MesType.MESSAGE_RECEIVE_CHAT);
                     oos.writeObject(message);
                }else if (message.getMesType()==MesType.MESSAGE_SEND_ALL_CHAT){
                    Iterator<String> iterator=ManageServerThread.hm.keySet().iterator();
                    System.out.println("用户:"+message.getSender()+" 群发消息:"+message.getContent());
                    message.setMesType(MesType.MESSAGE_RECEIVE_ALL_CHAT);
                    while (iterator.hasNext()){
                        String onLineUserId = iterator.next();
                        if (!onLineUserId.equals(message.getSender())) {//排除群发消息的这个用户

                            //进行转发message
                            ObjectOutputStream oos1 =
                                    new ObjectOutputStream(ManageServerThread.hm.get(onLineUserId).getSocket().getOutputStream());
                            oos1.writeObject(message);
                        }
                    }
                }else if (message.getMesType()==MesType.MESSAGE_SEND_FILE){
                    Socket socket=null;
                    if (null==ManageServerThread.hm.get(message.getReceiver())){
                        if (QQServer.validUsers.get(message.getReceiver())!=null){
                            System.out.println("该用户已离线，已保存到离线服务器");
                            Date date=new Date();
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                            sdf.format(date);
                            message.setMesType(MesType.MESSAGE_SEND_FILE);
                            message.setDate(date.toString());
                            OfflineMessage.hm.put(message.getReceiver(),message);
                            continue;
                        }
                        ObjectOutputStream oos1=new ObjectOutputStream(ManageServerThread.hm.get(message.getSender()).socket.getOutputStream());
                        message.setContent("用户不存在，发送失败！");
                        oos1.writeObject(message);
                        continue;
                    }
                    socket= ManageServerThread.hm.get(message.getReceiver()).getSocket();
                    oos=new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("接受到用户:"+message.getSender()+"发送的文件，开始向用户:"+message.getReceiver()+"发送文件");
                    oos.writeObject(message);
                    System.out.println("文件发送成功！");
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
