package com.example.project3.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project3.db.Database;
import com.example.project3.model.Transactions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DAOTransactions {
    Database dtb;
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    SQLiteDatabase dtGD;

    public DAOTransactions(Context context) {
        dtb = new Database(context);

    }


    public ArrayList<Transactions> getTrans(String sql, String... a) {
        ArrayList<Transactions> list = new ArrayList<>();
        dtGD = dtb.getReadableDatabase();
        Cursor cs = dtGD.rawQuery(sql, a);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            try {
                int ma = Integer.parseInt(cs.getString(0));
                String mota = cs.getString(1);
                String ngay = cs.getString(2);
                int tien = Integer.parseInt(cs.getString(3));
                int maK = Integer.parseInt(cs.getString(4));
                Transactions gd = new Transactions(ma, mota, dfm.parse(ngay), tien, maK);
                list.add(gd);
                cs.moveToNext();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cs.close();
        return list;
    }


    public ArrayList<Transactions> getAll() {
        String sql = "SELECT * FROM GIAODICH";
        return getTrans(sql);
    }

    //get trans by type
    public ArrayList<Transactions> getTransByIE(int loaiKhoan) {
        String sql = "SELECT * FROM GIAODICH as gd INNER JOIN THUCHI as tc ON gd.maKhoan = tc.maKhoan WHERE tc.loaiKhoan=?";
        ArrayList<Transactions> list = getTrans(sql, String.valueOf(loaiKhoan));
        return list;
    }

    public boolean addTrans(Transactions gd) {
        dtGD = dtb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("moTaGd", gd.getTransDescription());
        contentValues.put("ngayGd", dfm.format(gd.getTransDate()));
        contentValues.put("soTien", gd.getAmountMoney());
        contentValues.put("maKhoan", gd.getIeID());
        long r = dtGD.insert("GIAODICH", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //edit trans by ID
    public boolean editTrans(Transactions gd) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("moTaGd", gd.getTransDescription());
        contentValues.put("ngayGd", dfm.format(gd.getTransDate()));
        contentValues.put("soTien", gd.getAmountMoney());
        contentValues.put("maKhoan", gd.getIeID());
        int r = db.update("GIAODICH", contentValues, "maGd=?", new String[]{String.valueOf(gd.getTransID())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //delete trans by ID
    public boolean deleteTrans(Transactions gd) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        int r = db.delete("GIAODICH", "maGd=?", new String[]{String.valueOf(gd.getTransID())});
        if (r <= 0) {
            return false;
        }
        return true;
    }


}
