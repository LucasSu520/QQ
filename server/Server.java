package com.dltour.qq.server;

import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;
import com.dltour.qq.common.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server  extends Thread{

    User checkUser;
    ServerSocket logUpServerSocket;
    ServerSocket logInServerSocket;


    public static void main(String[] args)
    {
        new Server().start();
    }

    @Override
    public void run() {
        super.run();
        new LogIn().run();
        new LogUp().run();
    }

    //登陆时，取出文件中的vector，并且通过socket传递出去
    class LogIn extends Thread {
        @Override
        public void run() {
            super.run();
            Socket socket = null;
            try {
                logInServerSocket=new ServerSocket(7777);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    socket = logInServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ObjectOutputStream oos = null;
                ObjectInputStream ois;
                if (socket != null) {
                    try {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        ois=new ObjectInputStream(socket.getInputStream());
                        checkUser=(User) ois.readObject();
                            if (checkUser.getId().equals("123") && checkUser.getPassword().equals("123")){
                                Message ms =new Message();
                                ms.setMesType(MesType.MESSAGE_LOG_IN_SUCCESS);
                                ms.setContent(Message.USER_EXIST);
                                ServerConnectClientThread scc=new ServerConnectClientThread(socket);
                                scc.start();
                                ManageServerThread.add(checkUser,scc);
                                oos.writeObject(ms);
                            }else {
                                Message ms=new Message();
                                ms.setMesType(MesType.MESSAGE_LOG_UP_SUCCESS);
                                ms.setContent(Message.USER_INEXIST);
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
            while (true) {
                try {
                    logUpServerSocket=new ServerSocket(8888);
                    socket = logUpServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ObjectInputStream ois = null;
                ObjectOutputStream oos = null;
                if (socket != null) {
                    try {
                        ois = new ObjectInputStream(socket.getInputStream());
                        User user = (User) ois.readObject();
                        //TODO 判断vector中是否有该用户
                        oos.flush();
                        socket.shutdownOutput();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        oos.close();
                        ois.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }
}



