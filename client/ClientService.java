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
    String userId;
    String psw;
    Socket logInSocket;
    Socket logUpSocket;
    boolean logInSuccess;
    boolean logUpSuccess;

    //账号登陆，返回账号登陆是否成功
    public boolean logIn(String userId,String psw) throws IOException {
            logInSuccess=false;
            User user=new User(userId,psw);
            if (!accountExist(user)){
                System.out.println("您的账号或者密码输入错误，请重新输入。");
            }else {
                logInSuccess=true;
                System.out.println("欢迎回来用户（"+userId+"）!");
            }
            return logInSuccess;
        }

    //判断用户键入的账号密码是否存在
    private boolean accountExist(User user) {
        boolean isExist=false;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        try {
            //发送用户到服务端
            logInSocket=new Socket(InetAddress.getLocalHost(),7777);
            System.out.println("登陆接口已经连通");
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
                System.out.println("您输入的账号密码，符合要求，正在注册。。。。");

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
}
