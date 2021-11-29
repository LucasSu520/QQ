package com.dltour.qq.common;

public class MesType {
    public static int MESSAGE_LOG_IN_SUCCESS=0;     //登陆成功
    public static int MESSAGE_LOG_IN_FAIL=1;        //登陆失败
    public static int MESSAGE_ALREADY_LOG_IN=2;     //已经登陆
    public static int MESSAGE_LOG_UP_SUCCESS=3;     //注册成功
    public static int MESSAGE_LOG_UP_FAIL=4;        //注册失败
    public static int MESSAGE_GET_ONLINE_USERS=5;   //获得用户
    public static int MESSAGE_RET_ONLINE_USERS=6;   //返回用户
    public static int MESSAGE_EXIT_SYSTEM=7;        //退出系统
    public static int MESSAGE_SEND_CHAT=8;          //发送私聊
    public static int MESSAGE_RECEIVE_CHAT=9;       //接受私聊
    public static int MESSAGE_SEND_ALL_CHAT=10;      //发送群发
    public static int MESSAGE_RECEIVE_ALL_CHAT=11;  //接受群发
    public static int MESSAGE_SEND_FILE=12;         //发送文件
}
