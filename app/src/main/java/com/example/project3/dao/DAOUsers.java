package com.example.project3.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project3.db.Database;
import com.example.project3.model.Users;

import java.util.ArrayList;

public class DAOUsers {
    Database dtbRegister;

    public DAOUsers(Context context) {
        dtbRegister = new Database(context);
    }

    public ArrayList<Users> getALl() {
        ArrayList<Users> listTK = new ArrayList<>();
        SQLiteDatabase dtb = dtbRegister.getReadableDatabase();
        Cursor cs = dtb.rawQuery("SELECT * FROM taiKhoan", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            try {
                String tk = cs.getString(0);
                String mk = cs.getString(1);
                Users t = new Users(tk, mk);
                listTK.add(t);
                cs.moveToNext();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
        cs.close();
        return listTK;
    }
    public boolean addUser(Users tk) {
        SQLiteDatabase db = dtbRegister.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenTaiKhoan", tk.getUsername());
        values.put("matKhau", tk.getPassword());
        long r = db.insert("taiKhoan", null, values);
        if (r <= 0) {
            return false;
        }
        return true;
    }
    public boolean changePass(Users tk) {
        SQLiteDatabase db = dtbRegister.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matKhau", tk.getPassword());
        int r = db.update("taiKhoan", values, "tenTaiKhoan=?", new String[]{tk.getUsername()});
        if (r <= 0) {
            return false;
        }
        return true;
    }
}
