package com.example.projectandroid.Model;

public class User {
    private String username;

    private String userId;

    public User(){

    }

    public User(String username){
        this.username = username;
    }
    public User(String username, String userId){
        this.username = username;
        this.userId = userId;
    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getUserId(){
        return this.userId;
    }
}
