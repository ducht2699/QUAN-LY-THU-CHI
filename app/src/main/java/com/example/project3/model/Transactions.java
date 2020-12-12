package com.example.project3.model;

import java.io.Serializable;
import java.util.Date;

public class Transactions implements Serializable {
    private int transID;
    private String transDescription;
    private Date transDate;
    private int amountMoney;
    private int IeID;

    public Transactions() {
    }



    public Transactions(int transID, String transDescription, Date transDate, int amountMoney, int IeID) {
        this.transID = transID;
        this.transDescription = transDescription;
        this.transDate = transDate;
        this.amountMoney = amountMoney;
        this.IeID = IeID;
    }

    public int getTransID() {
        return transID;
    }

    public void setTransID(int transID) {
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

    public int getIeID() {
        return IeID;
    }

    public void setIeID(int ieID) {
        this.IeID = ieID;
    }
}
