package com.example.chatapp.models;

public class UserModel {
    private String username;
    private String userId;


    public UserModel() {
    }

    public UserModel(String nome, String userId) {
        this.username = nome;
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}