package com.example.project3.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IncomesExpenses implements Serializable {
    private String IeID;
    private String IeName;
    private int IeType;

    public IncomesExpenses() {
    }

    public IncomesExpenses(String IeID, String IeName, int IeType) {
        this.IeID = IeID;
        this.IeName = IeName;
        this.IeType = IeType;
    }

    public int getIeType() {
        return IeType;
    }

    public String getIeID() {
        return IeID;
    }

    public void setIeID(String ieID) {
        this.IeID = ieID;
    }

    public String getIeName() {
        return IeName;
    }

    public void setIeName(String ieName) {
        this.IeName = ieName;
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
