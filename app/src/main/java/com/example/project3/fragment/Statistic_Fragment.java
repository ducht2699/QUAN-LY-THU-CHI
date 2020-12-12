package com.example.project3.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.project3.R;
import com.example.project3.dao.DAOTransactions;
import com.example.project3.model.Transactions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Statistic_Fragment extends Fragment {
    private TextView tungay, denngay, thu, chi, conlai;
    private Button btnShow;
    private DatePickerDialog datePickerDialog;
    private DAOTransactions daoTransactions;
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");

    public Statistic_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        tungay = view.findViewById(R.id.tungay);
        denngay = view.findViewById(R.id.denngay);
        thu = view.findViewById(R.id.tienThu);
        chi = view.findViewById(R.id.tienChi);
        conlai = view.findViewById(R.id.tienConLai);
        btnShow = view.findViewById(R.id.btnShow);
        daoTransactions = new DAOTransactions(getActivity());
        //Format dạng tiền
        final NumberFormat fm = new DecimalFormat("#,###");
        final ArrayList<Transactions> listThu = daoTransactions.getTransByIE(0);
        final ArrayList<Transactions> listChi = daoTransactions.getTransByIE(1);
        int tongThu = 0, tongChi = 0;
        for (int i = 0; i < listThu.size(); i++) {
            tongThu += listThu.get(i).getAmountMoney();
        }
        for (int i = 0; i < listChi.size(); i++) {
            tongChi += Math.abs(listChi.get(i).getAmountMoney());
        }
        thu.setText(fm.format(tongThu) + " VND");
        chi.setText(fm.format(tongChi) + " VND");
        conlai.setText(fm.format(tongThu - tongChi) + " VND");
        //choose from date
        tungay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int m = calendar.get(Calendar.MONTH);
                int y = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String NgayDau = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tungay.setText(NgayDau);
                    }
                }, y, m, d);
                datePickerDialog.show();
            }
        });

        denngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int m = calendar.get(Calendar.MONTH);
                int y = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String NgayCuoi = dayOfMonth + "/" + (month + 1) + "/" + year;
                        denngay.setText(NgayCuoi);
                    }
                }, y, m, d);
                datePickerDialog.show();
                //click on show sort IE by day
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int Thu = 0, Chi = 0;
                        String bd = tungay.getText().toString();
                        String kt = denngay.getText().toString();
                        //calculate money by day
                        for (int i = 0; i < listThu.size(); i++) {
                            try {
                                if (listThu.get(i).getTransDate().compareTo(dfm.parse(bd)) >= 0 && listThu.get(i).getTransDate().compareTo(dfm.parse(kt)) <= 0) {
                                    Thu += listThu.get(i).getAmountMoney();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        for (int i = 0; i < listChi.size(); i++) {
                            try {
                                if (listChi.get(i).getTransDate().compareTo(dfm.parse(bd)) >= 0 && listChi.get(i).getTransDate().compareTo(dfm.parse(kt)) <= 0) {
                                    Chi += listChi.get(i).getAmountMoney();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        thu.setText(fm.format(Thu) + " VND");
                        chi.setText(fm.format(Chi) + " VND");
                        conlai.setText(fm.format(Thu - Chi) + " VND");
                    }
                });
            }
        });
        return view;
    }
}
