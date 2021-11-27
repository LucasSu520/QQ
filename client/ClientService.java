package com.dltour.qq.client;

import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;
import com.dltour.qq.common.User;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientService {
    Scanner s;
    User user;
    String userId;
    String psw;
    Socket logInSocket;
    Socket logUpSocket;
    boolean logInSuccess;
    boolean logUpSuccess;



    public ClientService() {
    }

    public ClientService(User user) {
        this.user = user;
    }

    public void getOnlineUser() {
        Message ms=new Message();
        ms.setMesType(MesType.MESSAGE_GET_ONLINE_USERS);
        ms.setSender(user.getId());
        try {
            ClientConnectServerThread ccst;
            ccst=ManageClientThread.hm.get(user);
            logInSocket= ccst.getSocket();
            ObjectOutputStream oos=new ObjectOutputStream(logInSocket.getOutputStream());
            oos.writeObject(ms);
            //TODO 如何让主线程在子线程启动后，在特殊位置再次启动主线程
            ccst.join(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //账号登陆，返回账号登陆是否成功
    public boolean logIn(User user) throws IOException {
            logInSuccess=false;
            if (!accountExist(user)){
                System.out.println("您的账号或者密码输入错误，请重新输入。");
            }else {
                logInSuccess=true;
                System.out.println("欢迎回来用户（"+user.getId()+"）!");
            }
            return logInSuccess;
        }

    //判断用户键入的账号密码是否存在
    private boolean accountExist(User user) {
        boolean isExist=false;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        //TODO 禁止登陆两次
        try {
            //发送用户到服务端
            logInSocket=new Socket(InetAddress.getLocalHost(),7777);
            oos=new ObjectOutputStream(logInSocket.getOutputStream());
            oos.writeObject(user);

            //接受服务端的返回消息
            ois=new ObjectInputStream(logInSocket.getInputStream());
            Message ms= (Message)ois.readObject();
            if (ms.getMesType()== MesType.MESSAGE_LOG_IN_SUCCESS){
                ClientConnectServerThread ccs=new ClientConnectServerThread(logInSocket);
                ccs.start();
                ManageClientThread.add(user,ccs);
                isExist=true;
            }else {
                System.out.println(ms.getContent());
                logInSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }


    //TODO 注册功能暂时不能使用
    //根据用户键入来判断用户注册
    public boolean logUp() throws IOException {
        logUpSuccess = false;
        boolean isLoop = true;
        while (isLoop) {
            System.out.println("请输入您注册的账号(1-9位数的纯数字):");
            s = new Scanner(System.in);
            userId = s.next();
            System.out.println("请输入您的密码(1-9位数的纯数字):");
            s = new Scanner(System.in);
            psw = s.next();
            if (String.valueOf(userId).length() > 9 || psw.length() > 9) {
                System.out.println("Id或者密码不符合规范，请重新填写");
                isLoop = true;
            }else {
                isLoop=false;
            }
        }

        //将信息写入到.dat 文件中
        User user = new User(userId, psw);
        try {
            logUpSuccess= saveAccount(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logUpSuccess;
    }

    //TODO 注册功能暂时不能使用
    //将键入的用户推送到服务端
    private boolean saveAccount(User user) {
        boolean logUpSuccess=false;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        try {
            logUpSocket=new Socket(InetAddress.getLocalHost(),9999);
            ois=new ObjectInputStream(logUpSocket.getInputStream());
            oos=new ObjectOutputStream(logUpSocket.getOutputStream());
            oos.writeObject(user);
            Message ms=(Message)ois.readObject();
            if (ms.getMesType()==MesType.MESSAGE_LOG_UP_SUCCESS){
                logUpSuccess=true;
                System.out.println("注册成功!您可以登陆了！");
            }else {
                System.out.println(ms.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logUpSuccess;
    }

    //向服务器发送退出系统的要求
    public void exitLogin() {
        Message message=new Message();
        message.setMesType(MesType.MESSAGE_EXIT_SYSTEM);
        message.setSender(user.getId());
        ClientConnectServerThread ccst=ManageClientThread.hm.get(user);
        logInSocket=ccst.getSocket();
        try {
            ObjectOutputStream oos=new ObjectOutputStream(logInSocket.getOutputStream());
            oos.writeObject(message);
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
