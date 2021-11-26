package com.dltour.qq.server;

import com.dltour.qq.common.User;

import java.util.HashMap;
import java.util.Iterator;

public class ManageServerThread {
    public static HashMap<User, ServerConnectClientThread> hm=new HashMap<>();
    public static void add(User user, ServerConnectClientThread scc){
        hm.put(user,scc);
    }
    public static String getOnlineUser(){
        String onlineList="";
        Iterator<User> iterator=hm.keySet().iterator();
        while (iterator.hasNext()){
            onlineList +=iterator.next().getId()+" ";
        }
        return onlineList;
    }
}
