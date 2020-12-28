package com.example.project3.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.project3.adapter.ExpensesTypeAdapter;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.IncomesExpenses;
import com.example.project3.model.Transactions;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Tab_ExpensesType_Fragment extends Fragment {
    private View view;
    private RecyclerView rcv;
    private List<IncomesExpenses> IEList;
    private List<Transactions> transactionsList;
    private ExpensesTypeAdapter adapter;
    private FloatingActionButton btnGrid, btnList, btnAdd;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
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
        setIEChildListener();
        setTransListener();
        //set list to recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        adapter = new ExpensesTypeAdapter(getActivity(), R.layout.oneitem_recylerview, IEList, transactionsList, mData);
        rcv.setAdapter(adapter);
        //add button click listener
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Constant.GRID_COLUMN);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new ExpensesTypeAdapter(getActivity(), R.layout.item_girl, IEList, transactionsList, mData);
                rcv.setAdapter(adapter);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new ExpensesTypeAdapter(getActivity(), R.layout.oneitem_recylerview, IEList, transactionsList, mData);
                rcv.setAdapter(adapter);
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
                final EditText edtAddExpenseType = dialog.findViewById(R.id.add_incomes_type);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                final Button btnAdd = dialog.findViewById(R.id.btnAdd);
                final TextView tvAddType = dialog.findViewById(R.id.tvAddType);
                tvAddType.setText("THÊM LOẠI CHI");
                edtAddExpenseType.setHint("Nhập loại chi");
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                rcv.addItemDecoration(dividerItemDecoration);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(rcv);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String expenseType = edtAddExpenseType.getText().toString();
                        String ID = mData.child("incomesExpensesTypes").push().getKey();
                        IncomesExpenses incomesExpenses = new IncomesExpenses(ID, expenseType, Constant.EXPENSES);
                        mData.child("incomesExpensesTypes").child(incomesExpenses.getIeID()).setValue(incomesExpenses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        //add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);
        //add touch action
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);
        return view;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid().toString());
        IEList = new ArrayList<>();
        transactionsList = new ArrayList<>();
        rcv = view.findViewById(R.id.rcv_ExpenesesTypes);
        btnAdd = view.findViewById(R.id.addBtn);
        btnGrid = view.findViewById(R.id.btnGrid);
        btnList = view.findViewById(R.id.btnList);
    }

    private void setTransListener() {
        mData.child("transactions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transactions transactions = snapshot.getValue(Transactions.class);
                if (isMatchIEType(transactions.getIeID()) == true) {
                    transactionsList.add(transactions);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transactions trans = snapshot.getValue(Transactions.class);
                for (Transactions x : transactionsList) {
                    if (x.getTransID().matches(trans.getTransID())) {
                        transactionsList.set(transactionsList.indexOf(x), trans);
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
                    adapter.notifyItemInserted(IEList.indexOf(ie));
                    Log.d(Constant.TAG, "add - " + IEList);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                for (IncomesExpenses x : IEList) {
                    if (x.getIeID().matches(ie.getIeID()) && x.getIeType() == Constant.EXPENSES) {
                        IEList.set(IEList.indexOf(x), ie);
                        adapter.notifyItemChanged(IEList.indexOf(x));
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                for (IncomesExpenses x : IEList) {
                    if (x.getIeID().matches(ie.getIeID()) && x.getIeType() == Constant.EXPENSES) {
                        int pos = IEList.indexOf(x);
                        IEList.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        Log.d(Constant.TAG, "remove child - " + IEList);
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
            Collections.swap(IEList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };
}
