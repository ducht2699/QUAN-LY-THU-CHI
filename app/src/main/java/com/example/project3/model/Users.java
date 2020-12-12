package com.example.project3.model;

import java.io.Serializable;

public class Users implements Serializable {
    private String Username, password;

    public Users() {
    }

    public Users(String Username, String password) {
        this.Username = Username;
        this.password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
