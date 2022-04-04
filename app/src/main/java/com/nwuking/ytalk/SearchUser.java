package com.nwuking.ytalk;

public class SearchUser {

    public SearchUser(int type, String username) {
        this.type = type;
        this.u_name = username;
    }

    /**
     * type : 1
     * username : zhangyl
     */

    private int type;
    private String u_name;

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
}
