package com.nwuking.ytalk.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

@Entity
public class Contacts {


    @Keep
    public Contacts(int userid, int type) {
        this.u_id = userid;
        this.type = type;
    }

    @Keep
    public Contacts(int userid, int type, String username, int accept) {
        this.u_id = userid;
        this.type = type;
        this.u_name = username;
        this.accept = accept;
    }

    @Generated(hash = 1287593394)
    public Contacts(long id, int userid, int type, String username, int accept) {
        this.id = id;
        this.u_id = userid;
        this.type = type;
        this.u_name = username;
        this.accept = accept;
    }

    @Generated(hash = 1804918957)
    public Contacts() {
    }




//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof DealUser) {
//            DealUser dealUser = (DealUser) obj;
//
//        }
//
//    }

    /**
     * userid : 9
     * type : 3
     * username : xxx
     * accept : 1
     */



    @Id
    private long id;
    private int u_id;
    private int type;
    private String u_name;
    private int accept;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserid() {
        return u_id;
    }

    public void setUserid(int userid) {
        this.u_id = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return u_name;
    }

    public void setUsername(String username) {
        this.u_name = username;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }


}