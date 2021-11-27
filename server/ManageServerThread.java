package com.dltour.qq.server;

import com.dltour.qq.common.User;

import java.util.HashMap;
import java.util.Iterator;

public class ManageServerThread {
    public static HashMap<String, ServerConnectClientThread> hm=new HashMap<>();
    public static void add(String userId, ServerConnectClientThread scc){
        hm.put(userId,scc);
    }
    public static String getOnlineUser(){
        String onlineList="";
        Iterator<String> iterator=hm.keySet().iterator();
        while (iterator.hasNext()){
            onlineList +=iterator.next()+" ";
        }
        return onlineList;
    }

    public static void remove(String userId) {
        hm.remove(userId);
    }
}
