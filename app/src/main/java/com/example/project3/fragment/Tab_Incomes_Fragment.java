package com.example.project3.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.R;
import com.example.project3.adapter.IncomesAdapter;
import com.example.project3.dao.DAOTransactions;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.Transactions;
import com.example.project3.model.IncomesExpenses;
import com.github.clans.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class Tab_Incomes_Fragment extends Fragment {
    View view;
    private RecyclerView rcv;
    private ArrayList<Transactions> list = new ArrayList<>();
    private ArrayList<IncomesExpenses> listTC = new ArrayList<>();
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private DAOTransactions daoTransactions;
    private DAOIncomesExpenses daoIncomesExpenses;
    private DatePickerDialog datePickerDialog;
    IncomesAdapter adapter;
    FloatingActionButton girdBtn, danhsachBtn, addBtn;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(list, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    public Tab_Incomes_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_incomes, container, false);
        rcv = view.findViewById(R.id.rcv_KhoanThu);
        addBtn = view.findViewById(R.id.addBtn);
        girdBtn = view.findViewById(R.id.girdBtn);
        danhsachBtn = view.findViewById(R.id.danhsachBtn);

        daoTransactions = new DAOTransactions();

        list = daoTransactions.getTransByIE(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        adapter = new IncomesAdapter(getActivity(), R.layout.oneitem_recylerview, list);
        rcv.setAdapter(adapter);

        girdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new IncomesAdapter(getActivity(), R.layout.item_girl, list);
                rcv.setAdapter(adapter);
            }
        });
        danhsachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new IncomesAdapter(getActivity(), R.layout.oneitem_recylerview, list);
                rcv.setAdapter(adapter);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_incomes_expenses);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText moTaGd = dialog.findViewById(R.id.them_mota_gd);
                final TextView ngayGd = dialog.findViewById(R.id.them_ngay_gd);
                final EditText tienGd = dialog.findViewById(R.id.them_tien_gd);
                final Spinner spLoaiGd = dialog.findViewById(R.id.spLoaiGd);
                final TextView title = dialog.findViewById(R.id.titleThemKhoan);
                final Button huy = dialog.findViewById(R.id.huyThemGD);
                final Button them = dialog.findViewById(R.id.btnThemGD);
                daoIncomesExpenses = new DAOIncomesExpenses();
                listTC = daoIncomesExpenses.getIE(0);
                //Set title
                title.setText("THÊM KHOẢN THU");

                //click on date show date chooser
                ngayGd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int d = calendar.get(Calendar.DAY_OF_MONTH);
                        int m = calendar.get(Calendar.MONTH);
                        int y = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String NgayGD = dayOfMonth + "/" + (month + 1) + "/" + year;
                                ngayGd.setText(NgayGD);
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });

                //pour data to spinner
                final ArrayAdapter sp = new ArrayAdapter(getActivity(), R.layout.spiner, listTC);
                spLoaiGd.setAdapter(sp);

                //click on delete button
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(), "Xóa",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                //click on add button
                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mota = moTaGd.getText().toString();
                        String ngay = ngayGd.getText().toString();
                        String tien = tienGd.getText().toString();

                        if (spLoaiGd.getSelectedItem() != null) {
                            IncomesExpenses tc = (IncomesExpenses) spLoaiGd.getSelectedItem();
                            int ma = tc.getIeID();

                            if (mota.isEmpty() && ngay.isEmpty() && tien.isEmpty()) {
                                Toast.makeText(getActivity(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    Transactions gd = new Transactions(0, mota, dfm.parse(ngay), Integer.parseInt(tien), ma);
                                    if (daoTransactions.addTrans(gd) == true) {
                                        list.clear();
                                        list.addAll(daoTransactions.getTransByIE(0));
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Tạo loại thu trước!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                dialog.show();
            }
        });
        return view;

    }

}
