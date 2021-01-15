package com.example.project3.fragment.income_expense;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constants;
import com.example.project3.R;
import com.example.project3.dao.DAOIncomesExpenses;
import com.github.clans.fab.FloatingActionButton;

import java.util.Collections;


public class Tab_IncomesType_Fragment extends Fragment {
    private View view;
    private RecyclerView rcv;
    private FloatingActionButton btnGrid, btnList, btnAdd;
    private DAOIncomesExpenses daoIncomesExpenses;

    public Tab_IncomesType_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab__incomes_type, container, false);
        init();
        //set list to recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(daoIncomesExpenses.getIncomesTypeAdapter());
        //add button click listener
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Constants.GRID_COLUMN);
                rcv.setLayoutManager(gridLayoutManager);
                rcv.setAdapter(daoIncomesExpenses.getIncomesTypeAdapter());
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                rcv.setAdapter(daoIncomesExpenses.getIncomesTypeAdapter());
            }
        });
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
                Button cancel = dialog.findViewById(R.id.btnCancel);
                final Button add = dialog.findViewById(R.id.btnAdd);
                edtAddIncomesType.setHint("Thêm loại thu");
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String IEType = edtAddIncomesType.getText().toString();
                        daoIncomesExpenses.addIEType(getContext(), IEType, dialog, Constants.INCOME);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        // add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);
        //add touch action
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);
        return view;
    }

    private void init() {
        daoIncomesExpenses = new DAOIncomesExpenses(getActivity(), Constants.INCOMES_TYPE_ADAPTER, Constants.INCOME);
        rcv = view.findViewById(R.id.rcv_LoaiThu);
        btnAdd = view.findViewById(R.id.addBtn);
        btnGrid = view.findViewById(R.id.btnGrid);
        btnList = view.findViewById(R.id.btnList);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(daoIncomesExpenses.getIEList(), fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };

}
