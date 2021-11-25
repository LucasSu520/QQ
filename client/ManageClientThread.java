package com.dltour.qq.client;

import com.dltour.qq.common.User;

import java.util.HashMap;

public class ManageClientThread {
    private User user;
    private ClientConnectServerThread ccs;
    public static HashMap<User, ClientConnectServerThread> hm=new HashMap<>();

    public ManageClientThread(User user, ClientConnectServerThread ccs) {
        this.user = user;
        this.ccs = ccs;
    }

    public static void add(User user, ClientConnectServerThread ccs){
        hm.put(user,ccs);
    }


}
