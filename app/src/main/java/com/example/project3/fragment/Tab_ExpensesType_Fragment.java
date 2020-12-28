package com.example.project3.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.adapter.ExpensesTypeAdapter;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.IncomesExpenses;
import com.example.project3.model.Transactions;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Tab_ExpensesType_Fragment extends Fragment {
    private View view;
    private RecyclerView rcv;
    private List<IncomesExpenses> IEList = new ArrayList<>();
    private DAOIncomesExpenses daoIncomesExpenses;
    private ExpensesTypeAdapter adapter;
    private FloatingActionButton btnGrid, btnList, btnAdd;

    public Tab_ExpensesType_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab__expenses_type, container, false);
        init();
        daoIncomesExpenses = new DAOIncomesExpenses();
        IEList = daoIncomesExpenses.getIE(Constant.EXPENSES);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        adapter = new ExpensesTypeAdapter(getActivity(), R.layout.oneitem_recylerview, IEList);
        rcv.setAdapter(adapter);
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new ExpensesTypeAdapter(getActivity(), R.layout.item_girl, IEList);
                rcv.setAdapter(adapter);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new ExpensesTypeAdapter(getActivity(), R.layout.oneitem_recylerview, IEList);
                rcv.setAdapter(adapter);
            }
        });
        //add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);
        //add touch action
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_incomes_expenses_type);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText edtAddIncomesType = dialog.findViewById(R.id.add_incomes_type);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                final Button btnAdd = dialog.findViewById(R.id.btnAdd);
                final TextView tvAddType = dialog.findViewById(R.id.tvAddType);
                tvAddType.setText("THÊM LOẠI CHI");
                edtAddIncomesType.setHint("Nhập loại chi");
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                rcv.addItemDecoration(dividerItemDecoration);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(rcv);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String expensesTypeName = edtAddIncomesType.getText().toString();
//                        String ieID TODO: add ieID
                        IncomesExpenses incomesExpenses = new IncomesExpenses("", expensesTypeName, Constant.EXPENSES);
                        if (daoIncomesExpenses.addIE(incomesExpenses) == true) {
                            IEList.clear();
                            IEList.addAll(daoIncomesExpenses.getIE(Constant.EXPENSES));
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    private void init() {
        rcv = view.findViewById(R.id.rcv_LoaiChi);
        btnAdd = view.findViewById(R.id.addBtn);
        btnGrid = view.findViewById(R.id.girdBtn);
        btnList = view.findViewById(R.id.danhsachBtn);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(IEList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };
}
