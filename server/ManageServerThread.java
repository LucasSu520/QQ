package com.dltour.qq.server;

import com.dltour.qq.common.User;

import java.util.HashMap;

public class ManageServerThread {
    public static HashMap<User, ServerConnectClientThread> hm=new HashMap<>();
    public static void add(User user, ServerConnectClientThread scc){
        hm.put(user,scc);
    }
}
