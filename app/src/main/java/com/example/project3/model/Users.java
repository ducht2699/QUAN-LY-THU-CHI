package com.example.project3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Users implements Serializable {
    private String Username, password;
    private String UID;
    private boolean isLoggedIn = false;

    private List<IncomesExpenses> IEList = new ArrayList<>();

    public Users() {
    }

    public Users(String username, String password, String UID) {
        Username = username;
        this.password = password;
        this.UID = UID;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public List<IncomesExpenses> getIEList() {
        return IEList;
    }

    public void setIEList(List<IncomesExpenses> IEList) {
        this.IEList = IEList;
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
