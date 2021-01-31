package com.example.project3.model;

import java.util.Comparator;

public class IECalculation {
    private int timeType;
    private long incomes;
    private long expense;
    private int time;

    public IECalculation(int timeType, long incomes, long expense, int time) {
        this.timeType = timeType;
        this.incomes = incomes;
        this.expense = expense;
        this.time = time;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public long getIncomes() {
        return incomes;
    }

    public void setIncomes(long incomes) {
        this.incomes = incomes;
    }

    public long getExpense() {
        return expense;
    }

    public void setExpense(long expense) {
        this.expense = expense;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }



}
