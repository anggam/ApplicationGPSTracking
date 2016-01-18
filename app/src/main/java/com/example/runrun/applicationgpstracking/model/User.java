package com.example.runrun.applicationgpstracking.model;

/**
 * Created by Wahid Hasan Ariyanto
 * on 1/8/16.
 */
public class User {
    private String name;
    private int user_id;

    public int getUser_id() {return user_id; }
    public String getName() {
        return name;
    }

    public void setUser_id(int user_id) {this.user_id =user_id; }
    public void setName(String name) {
        this.name = name;
    }
}
