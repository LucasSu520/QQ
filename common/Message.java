package com.dltour.qq.common;

import java.io.Serializable;

public class Message implements Serializable {
    public static String USER_EXIST="YES";
    public static String USER_INEXIST="NO";
    private String content;
    private String time;
    private String sender;
    private String receiver;
    private int mesType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getMesType() {
        return mesType;
    }

    public void setMesType(int mesType) {
        this.mesType = mesType;
    }
}
