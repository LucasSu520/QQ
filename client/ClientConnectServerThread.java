package com.dltour.qq.client;

import com.dltour.qq.common.MesType;
import com.dltour.qq.common.Message;


import java.io.FileOutputStream;
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
                }else if (ms.getMesType()==MesType.MESSAGE_SEND_FILE){
                    //TODO how let the thread to change this when receive the Message_SEND_file
                    //TODO let the receiver decide where to save
//                        System.out.println("请输入您要保存的文件地址:");
//                        Scanner s=new Scanner(System.in);
//                        String fileAddress=s.next();
                    String fileAddress="test1.txt";
                        Date date=new Date();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        System.out.println(sdf.format(date)+ms.getSender());
                        fileAddress="E:\\"+fileAddress;
                    System.out.println("文件开始接受");
                        FileOutputStream fos=new FileOutputStream(fileAddress,true);
                    System.out.println("文件正在接受");
                        fos.write(ms.getFileBytes());
                        fos.close();
                        System.out.println("===== 文件接受成功 ===== [File Name:e:\\"+fileAddress+"]");
                    }else {
                    System.out.println(ms.getContent());
                }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
