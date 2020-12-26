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
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class Tab_Incomes_Fragment extends Fragment {
    View view;
    private RecyclerView rcv;
    private List<Transactions> transactionsList = new ArrayList<>();
    private List<IncomesExpenses> IEList = new ArrayList<>();
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");

    private DAOTransactions daoTransactions;
    private DAOIncomesExpenses daoIncomesExpenses;

    private DatePickerDialog datePickerDialog;
    IncomesAdapter adapter;
    FloatingActionButton btnGrid, btnList, btnAdd;

    DatabaseReference mData;
    FirebaseAuth mAuth;

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
        init();

        getTrans_IEList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(layoutManager);
        adapter = new IncomesAdapter(getActivity(), R.layout.oneitem_recylerview, transactionsList, IEList, mData, mAuth);
        rcv.setAdapter(adapter);

        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new IncomesAdapter(getActivity(), R.layout.item_girl, transactionsList, IEList, mData, mAuth);
                rcv.setAdapter(adapter);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new IncomesAdapter(getActivity(), R.layout.oneitem_recylerview, transactionsList, IEList, mData, mAuth);
                rcv.setAdapter(adapter);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);

        addListener();

        btnAdd.setOnClickListener(new View.OnClickListener() {
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
                final EditText edtTransDescription = dialog.findViewById(R.id.add_trans_description);
                final TextView transDate = dialog.findViewById(R.id.add_trans_date);
                final EditText transMoney = dialog.findViewById(R.id.add_trans_money);
                final Spinner spnTransType = dialog.findViewById(R.id.spnTransType);
                final TextView tvTitleAddTrans = dialog.findViewById(R.id.titleAddTrans);
                final Button btnCancel = dialog.findViewById(R.id.btnCancelTrans);
                final Button btnAdd = dialog.findViewById(R.id.btnAddTrans);

                daoIncomesExpenses = new DAOIncomesExpenses();
                IEList = daoIncomesExpenses.getIE(0);
                //Set title
                tvTitleAddTrans.setText("THÊM KHOẢN THU");

                //click on date show date chooser
                transDate.setOnClickListener(new View.OnClickListener() {
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
                                transDate.setText(NgayGD);
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });

                //pour data to spinner
                final ArrayAdapter sp = new ArrayAdapter(getActivity(), R.layout.spiner, IEList);
                spnTransType.setAdapter(sp);

                //click on cancel button
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //click on add button
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String transDes = edtTransDescription.getText().toString();
                        String txtTransDate = transDate.getText().toString();
                        String txtTransMoney = transMoney.getText().toString();

                        if (spnTransType.getSelectedItem() != null) {
                            IncomesExpenses incomesExpenses = (IncomesExpenses) spnTransType.getSelectedItem();
                            String IeID = incomesExpenses.getIeID();

                            if (transDes.isEmpty() && txtTransDate.isEmpty() && txtTransMoney.isEmpty()) {
                                Toast.makeText(getActivity(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    String transID = mData.push().getKey();
                                    Transactions gd = new Transactions(transID, transDes, dfm.parse(txtTransDate), Integer.parseInt(txtTransMoney), IeID);
                                    mData.child(gd.getIeID()).push().setValue(gd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                transactionsList.clear();
                                                transactionsList.addAll(daoTransactions.getTransByIE(0));
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                            else {
                                                Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
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

    private void addListener() {
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                IncomesExpenses ieTemp = snapshot.getValue(IncomesExpenses.class);
                IEList.add(ieTemp);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTrans_IEList() {
        //get IE
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    IncomesExpenses ie = dataSnapshot.getValue(IncomesExpenses.class);
                    IEList.add(ie);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid().toString()).child("incomesExpenses");
        rcv = view.findViewById(R.id.rcv_KhoanThu);
        btnAdd = view.findViewById(R.id.addBtn);
        btnGrid = view.findViewById(R.id.girdBtn);
        btnList = view.findViewById(R.id.danhsachBtn);
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
