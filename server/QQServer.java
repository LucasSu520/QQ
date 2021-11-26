package com.dltour.qq.server;

import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;
import com.dltour.qq.common.User;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class QQServer extends Thread{

    User checkUser1;
    ServerSocket logUpServerSocket;
    ServerSocket logInServerSocket;
    private static ConcurrentHashMap<String,User> validUsers=new ConcurrentHashMap<>();
    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","1234"));
        validUsers.put("400",new User("400","12345"));
        validUsers.put("至尊宝",new User("至尊宝","1234"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","456789"));
        validUsers.put("菩提老祖",new User("菩提老祖","9876"));
    }


    public QQServer(){
    }

    private boolean checkUser(String id, String password) {
        User user=validUsers.get(id);
        if (user==null){
            return false;
        }
        if (!user.getPassword().equals(password)){
            return false;
        }
        return true;
    }

    public static void main(String[] args)
    {
        new QQServer().start();
    }

    @Override
    public void run() {
        try {
            logUpServerSocket=new ServerSocket(9999);
            logInServerSocket=new ServerSocket(7777);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //新线程启动，一定要用start，别用run
        new LogIn().start();
        new LogUp().start();
    }

    //登陆时，取出文件中的vector，并且通过socket传递出去
    class LogIn extends Thread {
        @Override
        public void run() {
            super.run();
            Socket socket = null;
            System.out.println("7777端口已经创建，等待连接。。。。");
            //检查服务端登陆端口为何无法接通
            while (true) {
                try {
                    socket = logInServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ObjectOutputStream oos;
                ObjectInputStream ois;
                System.out.println("登陆端口已经接通");
                if (socket != null) {
                    try {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        ois=new ObjectInputStream(socket.getInputStream());
                        checkUser1=(User) ois.readObject();
                            if (checkUser(checkUser1.getId(),checkUser1.getPassword())){
                                Message ms =new Message();
                                ms.setMesType(MesType.MESSAGE_LOG_IN_SUCCESS);
                                ServerConnectClientThread scc=new ServerConnectClientThread(socket);
                                scc.start();
                                ManageServerThread.add(checkUser1,scc);
                                oos.writeObject(ms);
                            }else {
                                Message ms=new Message();
                                ms.setMesType(MesType.MESSAGE_LOG_IN_FAIL);
                                oos.writeObject(ms);
                            }
                        } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //接受到了客户端的发送的客户数据检测登陆
        //接受客户端的注册数据，然后写入.dat的文件
    }

    //注册时，读取客户端的socket，然后保存到文件中
    class  LogUp extends Thread{
        @Override
        public void run() {
            super.run();
            Socket socket = null;
            System.out.println("9999端口已经创建，等待接通。。。。");
            while (true) {
                try {
                    socket = logUpServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ObjectInputStream ois;
                ObjectOutputStream oos ;
                if (socket != null) {
                    try {
                        Message ms=new Message();
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        ois = new ObjectInputStream(socket.getInputStream());
                        User user = (User) ois.readObject();
                        //TODO 判断vector中是否有该用户
                        User user1=validUsers.get(user.getId());
                        if (user1!=null){
                            ms.setMesType(MesType.MESSAGE_LOG_UP_FAIL);
                            ms.setContent("该用户名已存在！");
                            System.out.println(validUsers.size());
                        }else {
                            saveUser(user);
                            System.out.println("注册成功!");
                            ms.setMesType(MesType.MESSAGE_LOG_UP_SUCCESS);
                        }
                        oos.writeObject(ms);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //向集合中添加符合条件的用户；
    private void saveUser(User user) {
        //TODO 当学了数据库的时候，会重新完善该数据库；
        validUsers.put(user.getId(),user);
    }
}



