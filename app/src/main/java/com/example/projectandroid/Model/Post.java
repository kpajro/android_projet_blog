package com.example.projectandroid.Model;

public class Post {
    private String id;
    private String content;

    private User user;

    public Post() {
    }

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user;}
    public void setUser(User user) { this.user = user;}

    public String getContent() { return content; }
}