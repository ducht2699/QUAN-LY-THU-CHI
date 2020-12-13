package com.example.project3.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IncomesExpenses implements Serializable {
    private int IeID;
    private String IeName;
    private int IeType;

    private List<Transactions> transactionsList = new ArrayList<>();

    public IncomesExpenses() {
    }

    public IncomesExpenses(int IeID, String IeName, int IeType) {
        this.IeID = IeID;
        this.IeName = IeName;
        this.IeType = IeType;
    }


    public int getIeType() {
        return IeType;
    }

    public List<Transactions> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<Transactions> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public int getIeID() {
        return IeID;
    }

    public void setIeID(int ieID) {
        this.IeID = ieID;
    }

    public String getIeName() {
        return IeName;
    }

    public void setIeName(String ieName) {
        this.IeName = ieName;
    }

    public int isLoaiKhoan() {
        return IeType;
    }

    public void setIeType(int ieType) {
        this.IeType = ieType;
    }

    @NonNull
    @Override
    public String toString() {
        return IeName;
    }
}
