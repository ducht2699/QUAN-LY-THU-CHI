package com.example.project3.dao;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.adapter.ExpensesAdapter;
import com.example.project3.adapter.ExpensesTypeAdapter;
import com.example.project3.adapter.IncomesAdapter;
import com.example.project3.adapter.IncomesTypeAdapter;
import com.example.project3.database.Database;
import com.example.project3.model.IncomesExpenses;
import com.example.project3.model.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class DAOIncomesExpenses {
    private Database database;
    private List<IncomesExpenses> IEList;
    private List<Transactions> transactionsList;
    private IncomesTypeAdapter incomesTypeAdapter;
    private IncomesAdapter incomesAdapter;
    private ExpensesAdapter expensesAdapter;
    private ExpensesTypeAdapter expensesTypeAdapter;

    public DAOIncomesExpenses(Activity activity, String adapterType, int IEType) {
        this.database = new Database();
        IEList = new ArrayList<>();
        transactionsList = new ArrayList<>();
        setListener(IEType, adapterType);
        createAdapter(activity, adapterType);
    }

    private void setListener(int IEType, String adapterType) {
        if (adapterType.matches(Constant.EXPENSES_TYPE_ADAPTER) || adapterType.matches(Constant.INCOMES_TYPE_ADAPTER)) {
            setIETypeListener(IEType, adapterType, Constant.IS_NOTIFY);
            setTransactionListener(adapterType, Constant.NO_NOTIFY);
        } else if (adapterType.matches(Constant.EXPENSES_ADAPTER) || adapterType.matches(Constant.INCOMES_ADAPTER)) {
            setIETypeListener(IEType, adapterType, Constant.NO_NOTIFY);
            setTransactionListener(adapterType, Constant.IS_NOTIFY);
        }
    }

    public void createAdapter(Activity activity, String adapterType) {
        switch (adapterType) {
            case Constant.INCOMES_ADAPTER:
                incomesAdapter = new IncomesAdapter(activity, R.layout.oneitem_recylerview, transactionsList, IEList, DAOIncomesExpenses.this);
                break;
            case Constant.INCOMES_TYPE_ADAPTER:
                incomesTypeAdapter = new IncomesTypeAdapter(activity, R.layout.oneitem_recylerview, IEList, transactionsList, DAOIncomesExpenses.this);
                break;
            case Constant.EXPENSES_ADAPTER:
                expensesAdapter = new ExpensesAdapter(activity, R.layout.oneitem_recylerview, transactionsList, IEList, DAOIncomesExpenses.this);
                break;
            case Constant.EXPENSES_TYPE_ADAPTER:
                expensesTypeAdapter = new ExpensesTypeAdapter(activity, R.layout.oneitem_recylerview, IEList, transactionsList, DAOIncomesExpenses.this);
                break;
        }
    }

    public IncomesTypeAdapter getIncomesTypeAdapter() {
        return incomesTypeAdapter;
    }

    public IncomesAdapter getIncomesAdapter() {
        return incomesAdapter;
    }

    public ExpensesAdapter getExpensesAdapter() {
        return expensesAdapter;
    }

    public ExpensesTypeAdapter getExpensesTypeAdapter() {
        return expensesTypeAdapter;
    }

    public List<IncomesExpenses> getIEList() {
        return IEList;
    }

    public List<Transactions> getTransactionsList() {
        return transactionsList;
    }

    //IE
    public void editIEType(String ieID, String incomeTypeNewName, final Context context, final Dialog dialog) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("incomesExpensesTypes").child(ieID)
                .child("ieName").setValue(incomeTypeNewName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addIEType(final Context context, String IEName, final Dialog dialog, int IEType) {
        String ID = database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("incomesExpensesTypes").push().getKey();
        IncomesExpenses incomesExpenses = new IncomesExpenses(ID, IEName, IEType);
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("incomesExpensesTypes")
                .child(incomesExpenses.getIeID()).setValue(incomesExpenses).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText((Activity) context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText((Activity) context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteIEType(final String ieID, final TextView tvMessage, final ProgressBar progressBar, final Dialog dialog, final Context context) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("incomesExpensesTypes").child(ieID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    deleteChildWithIEIDEquals(ieID);
                    tvMessage.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //transactions
    public void editTransaction(Transactions transactions, final Context context, final Dialog dialog) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("transactions").child(transactions.getTransID()).setValue(transactions).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public void deleteTransaction(final TextView tvMessage, final ProgressBar progressBar, final Dialog dialog, final Context context, Transactions transaction) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("transactions").child(transaction.getTransID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    tvMessage.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addTransaction(final Activity activity, final Dialog dialog, Transactions transaction) {
        String transID = database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).push().getKey();
        transaction.setTransID(transID);
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("transactions").child(transID).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(Constant.TAG, "addtrans - " + IEList);
                    Toast.makeText(activity, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
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

    private void notifyItemInserted(int pos, final String adapterType) {
        switch (adapterType) {
            case Constant.INCOMES_ADAPTER:
                incomesAdapter.notifyItemInserted(pos);
                break;
            case Constant.INCOMES_TYPE_ADAPTER:
                incomesTypeAdapter.notifyItemInserted(pos);
                break;
            case Constant.EXPENSES_ADAPTER:
                expensesAdapter.notifyItemInserted(pos);
                break;
            case Constant.EXPENSES_TYPE_ADAPTER:
                expensesTypeAdapter.notifyItemInserted(pos);
                break;
        }
    }

    private void notifyItemChanged(int pos, final String adapterType) {
        switch (adapterType) {
            case Constant.INCOMES_ADAPTER:
                incomesAdapter.notifyItemChanged(pos);
                break;
            case Constant.INCOMES_TYPE_ADAPTER:
                incomesTypeAdapter.notifyItemChanged(pos);
                break;
            case Constant.EXPENSES_ADAPTER:
                expensesAdapter.notifyItemChanged(pos);
                break;
            case Constant.EXPENSES_TYPE_ADAPTER:
                expensesTypeAdapter.notifyItemChanged(pos);
                break;
        }
    }

    private void setIETypeListener(final int IEType, final String adapterType, final boolean isNotify) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("incomesExpensesTypes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                if (ie.getIeType() == IEType) {
                    IEList.add(ie);
                    if (isNotify)
                        notifyItemInserted(IEList.indexOf(ie), adapterType);
                    Log.d(Constant.TAG, "IE add - " + IEList + " - " + adapterType);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                for (IncomesExpenses x : IEList) {
                    if (x.getIeID().matches(ie.getIeID()) && x.getIeType() == IEType) {
                        IEList.set(IEList.indexOf(x), ie);
                        if (isNotify)
                            notifyItemChanged(IEList.indexOf(ie), adapterType);
                        Log.d(Constant.TAG, "IE change - " + IEList + " - " + adapterType);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                IncomesExpenses ie = snapshot.getValue(IncomesExpenses.class);
                for (IncomesExpenses x : IEList) {
                    if (x.getIeID().matches(ie.getIeID()) && x.getIeType() == IEType) {
                        int pos = IEList.indexOf(x);
                        IEList.remove(pos);
                        if (isNotify)
                            notifyDataChange(adapterType);
                        Log.d(Constant.TAG, "IE remove - " + IEList + " - " + adapterType);
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

    private void setTransactionListener(final String adapterType, final boolean isNotify) {
        database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("transactions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transactions transactions = snapshot.getValue(Transactions.class);
                if (isMatchIEType(transactions.getIeID()) == true) {
                    transactionsList.add(transactions);
                    if (isNotify)
                    notifyItemInserted(transactionsList.indexOf(transactions), adapterType);
                    Log.d(Constant.TAG, "trans add - " + transactionsList + " - " + adapterType);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Transactions trans = snapshot.getValue(Transactions.class);
                for (Transactions x : transactionsList) {
                    if (x.getTransID().matches(trans.getTransID())) {
                        transactionsList.set(transactionsList.indexOf(x), trans);
                        if (isNotify)
                        notifyItemChanged(transactionsList.indexOf(trans), adapterType);
                        Log.d(Constant.TAG, "trans change - " + transactionsList + " - " + adapterType);
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
                        if (isNotify)
                            notifyDataChange(adapterType);
                        Log.d(Constant.TAG, "trans remove - " + transactionsList + " - " + adapterType);
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

    private void notifyDataChange(String adapterType) {
        switch (adapterType) {
            case Constant.INCOMES_ADAPTER:
                incomesAdapter.notifyDataSetChanged();
                break;
            case Constant.INCOMES_TYPE_ADAPTER:
                incomesTypeAdapter.notifyDataSetChanged();
                break;
            case Constant.EXPENSES_ADAPTER:
                expensesAdapter.notifyDataSetChanged();
                break;
            case Constant.EXPENSES_TYPE_ADAPTER:
                expensesTypeAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void deleteChildWithIEIDEquals(String ieID) {
        for (Transactions trans : transactionsList) {
            if (trans.getIeID().matches(ieID)) {
                database.getDatabase().child("Users").child(database.getAuthentication().getCurrentUser().getUid().toString()).child("transactions").child(trans.getTransID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constant.TAG, "delete trans by deleting IE- " + transactionsList);
                        } else {
                            Log.d(Constant.TAG, "delete trans failed");
                        }
                    }
                });
            }
        }
        for (int i = 0; i < transactionsList.size(); i++) {
            if (transactionsList.get(i).getIeID().matches(ieID)) {
                transactionsList.remove(i);
            }
        }
    }
}
