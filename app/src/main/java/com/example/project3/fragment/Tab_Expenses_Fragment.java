package com.example.project3.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.adapter.ExpensesAdapter;
import com.example.project3.dao.DAOTransactions;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.Transactions;
import com.example.project3.model.IncomesExpenses;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class Tab_Expenses_Fragment extends Fragment {
    private View view;
    private RecyclerView rcv;
    private ArrayList<Transactions> transactionsList;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private DatePickerDialog datePickerDialog;
    private ArrayList<IncomesExpenses> IEList;
    private ExpensesAdapter adapter;
    private FloatingActionButton btnGrid, btnList, btnAdd;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;

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
        setIEChildListener();
        setTransChildListener();
        //set list to recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        adapter = new ExpensesAdapter(getActivity(), R.layout.oneitem_recylerview, transactionsList, IEList, mData, mAuth);
        rcv.setAdapter(adapter);
        //add button click listener
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Constant.GRID_COLUMN);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new ExpensesAdapter(getActivity(), R.layout.item_girl, transactionsList, IEList, mData, mAuth);
                rcv.setAdapter(adapter);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new ExpensesAdapter(getActivity(), R.layout.oneitem_recylerview, transactionsList, IEList, mData, mAuth);
                rcv.setAdapter(adapter);
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
                final TextView tvTitle = dialog.findViewById(R.id.titleAddTrans);
                final Button btnCancel = dialog.findViewById(R.id.btnCancelTrans);
                final Button btnAdd = dialog.findViewById(R.id.btnAddTrans);
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
                //pour data to spinner
                final ArrayAdapter spnAdapter = new ArrayAdapter(getActivity(), R.layout.spiner, IEList);
                spnTransType.setAdapter(spnAdapter);
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
                        if (spnTransType.getSelectedItem() != null) {
                            IncomesExpenses ie = (IncomesExpenses) spnTransType.getSelectedItem();
                            String ieID = ie.getIeID();
                            if (transDes.isEmpty() || transDate.isEmpty() || transMoney.isEmpty()) {
                                Toast.makeText(getActivity(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    String transID = mData.push().getKey();
                                    Transactions gd = new Transactions(transID, transDes, dfm.parse(transDate), Integer.parseInt(transMoney), ieID);
                                    mData.child("transactions").child(transID).setValue(gd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Tạo loại chi trước!", Toast.LENGTH_SHORT).show();
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
        IEList = new ArrayList<>();
        transactionsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid().toString());
        rcv = view.findViewById(R.id.rcv_KhoanChi);
        btnAdd = view.findViewById(R.id.addBtn);
        btnGrid = view.findViewById(R.id.btnGrid);
        btnList = view.findViewById(R.id.btnList);
    }

    private void setTransChildListener() {
        mData.child("transactions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transactions transactions = snapshot.getValue(Transactions.class);
                if (isMatchIEType(transactions.getIeID()) == true) {
                    transactionsList.add(transactions);
                    adapter.notifyItemInserted(transactionsList.indexOf(transactions));
                    Log.d(Constant.TAG, "add trans" + transactionsList);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transactions trans = snapshot.getValue(Transactions.class);
                for (Transactions x : transactionsList) {
                    if (x.getTransID().matches(trans.getTransID())) {
                        transactionsList.set(transactionsList.indexOf(x), trans);
                        adapter.notifyItemChanged(transactionsList.indexOf(x));
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Transactions trans = snapshot.getValue(Transactions.class);
                for (Transactions x : transactionsList) {
                    if (x.getTransID().matches(trans.getTransID())) {
                        int pos = transactionsList.indexOf(x);
                        transactionsList.remove(pos);
                        adapter.notifyDataSetChanged();
                        Log.d(Constant.TAG, "trans removed by deleting incomesType - " + transactionsList);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setIEChildListener() {
        mData.child("incomesExpensesTypes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                if (ie.getIeType() == Constant.EXPENSES) {
                    IEList.add(ie);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                for (IncomesExpenses x : IEList) {
                    if (x.getIeID().matches(ie.getIeID()) && ie.getIeType() == Constant.INCOME) {
                        IEList.set(IEList.indexOf(x), ie);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                for (IncomesExpenses x : IEList) {
                    if (x.getIeID().matches(ie.getIeID()) && ie.getIeType() == Constant.INCOME) {
                        int pos = IEList.indexOf(x);
                        IEList.remove(pos);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private boolean isMatchIEType(String ieID) {
        boolean check = false;
        for (IncomesExpenses x : IEList) {
            if (x.getIeID().matches(ieID)) {
                check = true;
                break;
            }
        }
        return check;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(transactionsList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };
}
