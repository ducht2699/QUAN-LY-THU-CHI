package com.example.project3.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.project3.model.IncomesExpenses;

import java.util.ArrayList;

public class DAOIncomesExpenses {


    public DAOIncomesExpenses() {

    }
    // Incomes & Expenses == IE


    public ArrayList<IncomesExpenses> getIE(String sql, String... a) {
        ArrayList<IncomesExpenses> list = new ArrayList<>();
//        Cursor cs = dtTC.rawQuery(sql, a);
//        cs.moveToFirst();
//        while (!cs.isAfterLast()) {
//            int maTc = Integer.parseInt(cs.getString(0));
//            String tenTc = cs.getString(1);
//            int loaiTc = Integer.parseInt(cs.getString(2));
//
//            IncomesExpenses tc = new IncomesExpenses(maTc, tenTc, loaiTc);
//            list.add(tc);
//            cs.moveToNext();
//        }
//        cs.close();
        return list;
    }

    //add incomes & expenses
    public boolean addIE(IncomesExpenses tc) {
//        SQLiteDatabase db = dtb.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("tenKhoan", tc.getIeName());
//        contentValues.put("loaiKhoan", tc.isLoaiKhoan());
//        long r = db.insert("THUCHI", null, contentValues);
//        if (r <= 0) {
//            return false;
//        }
        return true;
    }

    //delete incomes&expenses by id, when delete it, datas in transactions must be deleted as well
    public boolean deleteIE(IncomesExpenses tc) {
//        SQLiteDatabase db = dtb.getWritableDatabase();
//        int r = db.delete("THUCHI", "maKhoan=?", new String[]{String.valueOf(tc.getIeID())});
//        int s = db.delete("GIAODICH", "maKhoan=?", new String[]{String.valueOf(tc.getIeID())});
//        if (r <= 0) {
//            return false;
//        }
        return true;
    }

    //edit incomes&expenses by IE-id
    public boolean editIE(IncomesExpenses tc) {
//        SQLiteDatabase db = dtb.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("tenKhoan", tc.getIeName());
//        contentValues.put("loaiKhoan", tc.isLoaiKhoan());
//        int r = db.update("THUCHI", contentValues, "maKhoan=?", new String[]{String.valueOf(tc.getIeID())});
//        if (r <= 0) {
//            return false;
//        }
        return true;
    }

    //get incomes&expenses list
    public ArrayList<IncomesExpenses> getAllIE() {
        String sql = "SELECT * FROM THUCHI";
        return getIE(sql);
    }

    //show list by incomes or expenses
    public ArrayList<IncomesExpenses> getIE(int loaiKhoan) {
        String sql = "SELECT * FROM THUCHI WHERE loaiKhoan=?";
        ArrayList<IncomesExpenses> list = getIE(sql, String.valueOf(loaiKhoan));
        return list;
    }

    //get name by id
    public String getNameIE(int maKhoan) {
        String name = "";

        String sql = "SELECT * FROM THUCHI WHERE maKhoan=?";
        ArrayList<IncomesExpenses> list = getIE(sql, String.valueOf(maKhoan));
        return list.get(0).getIeName();
    }
}
