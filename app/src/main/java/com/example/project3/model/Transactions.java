package com.example.project3.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transactions implements Serializable {
    private String transID;
    private String transDescription;
    private Date transDate;
    private int amountMoney;
    private String IeID;

    public Transactions() {
    }

    public Transactions(String transID, String transDescription, Date transDate, int amountMoney, String IeID) {
        this.transID = transID;
        this.transDescription = transDescription;
        this.transDate = transDate;
        this.amountMoney = amountMoney;
        this.IeID = IeID;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getTransDescription() {
        return transDescription;
    }

    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public int getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(int amountMoney) {
        this.amountMoney = amountMoney;
    }

    public String getIeID() {
        return IeID;
    }

    public void setIeID(String ieID) {
        this.IeID = ieID;
    }

    @NonNull
    @Override
    public String toString() {
        return this.transDescription;
    }
}
