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
import com.example.project3.dao.DAOUsers;
import com.github.clans.fab.FloatingActionButton;

import java.util.Collections;


public class AccountTypeFragment extends Fragment {
    private View view;
    private DAOUsers daoUsers;
    private RecyclerView rcv;
    private FloatingActionButton btnGrid, btnList, btnAdd;

    public AccountTypeFragment() {
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
        view = inflater.inflate(R.layout.fragment_account_type, container, false);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(daoUsers.getAccountTypeAdapter());
        //add button click listener
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Constant.GRID_COLUMN);
                rcv.setLayoutManager(gridLayoutManager);
                rcv.setAdapter(daoUsers.getAccountTypeAdapter());
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                rcv.setAdapter(daoUsers.getAccountTypeAdapter());
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_account_type);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText edtAccountType = dialog.findViewById(R.id.edtAccountType),
                        edtAmountMoney = dialog.findViewById(R.id.edtAmountMoney);
                final Button btnCancel = dialog.findViewById(R.id.btnCancel),
                        btnAdd = dialog.findViewById(R.id.btnAdd);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String accountType = edtAccountType.getText().toString(),
                                amountMoney = edtAmountMoney.getText().toString();
                        if (accountType.isEmpty() || amountMoney.isEmpty()) {
                            Toast.makeText(getContext(), "Không được để trống!", Toast.LENGTH_SHORT).show();
                        } else {
                            long money = Long.parseLong(amountMoney);
                            daoUsers.addAccountType(accountType, money, getContext());
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        //add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);
        return view;
    }

    private void init() {
        daoUsers = new DAOUsers();
        daoUsers.addAccountTypeListener();
        daoUsers.createAccountTypeAdapter(view.getContext());
        rcv = view.findViewById(R.id.rcvAccountType);
        btnAdd = view.findViewById(R.id.addBtn);
        btnGrid = view.findViewById(R.id.btnGrid);
        btnList = view.findViewById(R.id.btnList);
    }


}