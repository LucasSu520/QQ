package com.dltour.qq.client;

import com.dltour.qq.common.User;

import java.io.IOException;
import java.util.Scanner;


//以后界面和逻辑一定要分开;
public class QQView {

    boolean loop=true;
    boolean secLoop=true;
    boolean thirdLoop;
    private static boolean isExist=false;
    String key="";
    String userId;
    String psw;
    Scanner s;
//TODO list 无法解决无法第二次登陆的问题，第二次登陆会显示出连接失败


    public static void main(String[] args) {

        try {
            new QQView().menuView();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * show the QQ system menu
     */
    private void menuView() throws IOException {

        while (loop){
            System.out.println("==============网络登陆系统==============");
            System.out.println("1 登陆系统");
            System.out.println("9 退出系统");
            s=new Scanner(System.in);
            key=s.next();
            switch (key){
                case "1":
                    //判断是否有账号，如果没有，则去注册
                    while (secLoop) {
                        System.out.println("==============网络登陆系统==============");
                        System.out.println("1 登陆账号");
                        System.out.println("2 注册账号");
                        System.out.println("9 退出系统");
                        s = new Scanner(System.in);
                        key = s.next();
                        switch (key) {
                            case "1":
                                System.out.println("请输入您的账号");
                                s=new Scanner(System.in);
                                userId=s.next();
                                System.out.println("请输入您的密码");
                                s=new Scanner(System.in);
                                psw=s.next();
                                secLoop=!new ClientService().logIn(userId,psw);
                                thirdLoop=!secLoop;
                                break;
                            case "2":
                                new ClientService().logUp();
                                break;
                            case "9":
                                System.out.println("退出系统成功");
                                loop=false;
                                secLoop=false;
                                thirdLoop=false;
                                break;
                            default:
                                System.out.println("输入有误，请重新输入");
                                break;
                        }
                    }
                    //判断出服务器的文件中的用户名和密码是否和用户输入一致
                    while (thirdLoop){
                        System.out.println("==============网络服务二级菜单（用户名："+userId+")==============");
                        System.out.println("1 在线用户");
                        System.out.println("2 私聊用户");
                        System.out.println("3 群发消息");
                        System.out.println("9 退出系统");
                        s=new Scanner(System.in);
                        key=s.next();
                        switch (key){
                            case "1":
                                System.out.println("在线用户列表");
                                break;
                            case "2":
                                System.out.println("私聊用户");
                                break;
                            case "3":
                                System.out.println("群发消息");
                                break;
                            case "9":
                                System.out.println("退出系统成功!");
                                //TODO 当用户选择退出登陆的时候，切断连接的通信线程；
                                secLoop=false;
                                loop=false;
                                thirdLoop=false;
                                break;
                        }
                    }
                    break;
                case "9":
                    System.out.println("退出系统成功!");
                    loop=false;
                    break;
            }

        }
    }

}
