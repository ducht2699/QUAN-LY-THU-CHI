package com.example.project3.fragment.income_expense;

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

import com.example.project3.Constants;
import com.example.project3.R;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.dao.DAOUsers;
import com.example.project3.model.AccountType;
import com.example.project3.model.Transactions;
import com.example.project3.model.IncomesExpenses;
import com.github.clans.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;


public class Tab_Expenses_Fragment extends Fragment {
    private View view;
    private RecyclerView rcv;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private DatePickerDialog datePickerDialog;
    private FloatingActionButton btnGrid, btnList, btnAdd;
    private DAOIncomesExpenses daoIncomesExpenses;
    private DAOUsers daoUsers;

    public Tab_Expenses_Fragment() {
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
        view = inflater.inflate(R.layout.fragment_tab__expenses, container, false);
        init();
        //set list to recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(daoIncomesExpenses.getExpensesAdapter());
        //add button click listener
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Constants.GRID_COLUMN);
                rcv.setLayoutManager(gridLayoutManager);
                rcv.setAdapter(daoIncomesExpenses.getExpensesAdapter());
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                rcv.setAdapter(daoIncomesExpenses.getExpensesAdapter());
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_incomes_expenses);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final TextView tvTransDes = dialog.findViewById(R.id.add_trans_description);
                final TextView tvTransDate = dialog.findViewById(R.id.add_trans_date);
                final TextView tvTransMoney = dialog.findViewById(R.id.add_trans_money);
                final Spinner spnTransType = dialog.findViewById(R.id.spnTransType);
                final Spinner spnWalletType = dialog.findViewById(R.id.spnWalletType);
                final TextView tvTitle = dialog.findViewById(R.id.titleAddAccount);
                final Button btnCancel = dialog.findViewById(R.id.btnCancelTrans);
                final Button btnAdd = dialog.findViewById(R.id.btnAddTrans);
                final TextView tvTitleIEType = dialog.findViewById(R.id.tvTitleIEType);
                tvTitleIEType.setText("Loại Chi");
                tvTitle.setText("THÊM KHOẢN CHI");
                //click on date show date chooser
                tvTransDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int d = calendar.get(Calendar.DAY_OF_MONTH);
                        int m = calendar.get(Calendar.MONTH);
                        int y = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String tempTransDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                tvTransDate.setText(tempTransDate);
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });
                //pour data to spinner IEType
                final ArrayAdapter spnAdapter = new ArrayAdapter(getActivity(), R.layout.spiner, daoIncomesExpenses.getIEList());
                spnTransType.setAdapter(spnAdapter);
                //pour data to spinner Wallet Type
                final ArrayAdapter spnAdapter2 = new ArrayAdapter(getActivity(), R.layout.spiner, daoUsers.getAccountTypeList());
                spnWalletType.setAdapter(spnAdapter2);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String transDes = tvTransDes.getText().toString();
                        String transDate = tvTransDate.getText().toString();
                        String transMoney = tvTransMoney.getText().toString();
                        if (spnTransType.getSelectedItem() != null && spnWalletType.getSelectedItem() != null) {
                            IncomesExpenses ie = (IncomesExpenses) spnTransType.getSelectedItem();
                            String ieID = ie.getIeID();
                            AccountType accountType = (AccountType) spnWalletType.getSelectedItem();
                            String accountTypeID = accountType.getAccountTypeID();
                            if (transDes.isEmpty() || transDate.isEmpty() || transMoney.isEmpty()) {
                                Toast.makeText(getActivity(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    Transactions transaction = new Transactions("", transDes, dfm.parse(transDate), Integer.parseInt(transMoney), ieID, accountTypeID);
                                    daoIncomesExpenses.addTransaction(getActivity(), dialog, transaction);
                                    daoUsers.notifyTransactionChange(transaction, Constants.EXPENSES);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Tạo loại chi/ví trước!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        //add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);
        //add touch action
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);
        return view;
    }

    private void init() {
        daoIncomesExpenses = new DAOIncomesExpenses(getActivity(), Constants.EXPENSES_ADAPTER, Constants.EXPENSES);
        daoUsers = new DAOUsers();
        daoUsers.addAccountTypeListener(true);
        daoUsers.createAccountTypeAdapter(view.getContext());
        rcv = view.findViewById(R.id.rcvAccountType);
        btnAdd = view.findViewById(R.id.addBtn);
        btnGrid = view.findViewById(R.id.btnGrid);
        btnList = view.findViewById(R.id.btnList);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(daoIncomesExpenses.getTransactionsList(), fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };
}
