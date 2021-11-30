package com.dltour.qq.server;

import com.dltour.qq.common.Message;

import java.util.HashMap;

public class OfflineMessage {
    public static HashMap<String , Message> hm=new HashMap();

    public OfflineMessage(HashMap<String, Message> hm) {
        this.hm = hm;
    }

    public void remove(String userId,Message message){
        hm.remove(userId,message);
    }

    public void add(String userId,Message message){
        hm.put(userId,message);
    }

    public HashMap<String, Message> getHm() {
        return hm;
    }

    public void setHm(HashMap<String, Message> hm) {
        this.hm = hm;
    }
}
