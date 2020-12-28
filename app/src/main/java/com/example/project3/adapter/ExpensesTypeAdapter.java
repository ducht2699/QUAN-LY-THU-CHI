package com.example.project3.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.model.IncomesExpenses;
import com.example.project3.model.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ExpensesTypeAdapter extends RecyclerView.Adapter<ExpensesTypeAdapter.ViewHolder> {
    private Context context;
    private List<IncomesExpenses> IEList;
    private List<Transactions> transactionList;
    private int layout;
    private DatabaseReference mData;

    public ExpensesTypeAdapter() {
    }

    public ExpensesTypeAdapter(Context context, ArrayList<IncomesExpenses> IEList) {
        this.context = context;
        this.IEList = IEList;
    }

    public ExpensesTypeAdapter(Context context, int layout, List<IncomesExpenses> IEList, List<Transactions> transactionList, DatabaseReference mData) {
        this.context = context;
        this.mData = mData;
        this.transactionList = transactionList;
        this.IEList = IEList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.text.setText(IEList.get(position).getIeName());
        final IncomesExpenses incomesExpenses = IEList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        context, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(context).inflate(
                        R.layout.bottom_sheet_action,
                        (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
                TextView tvDetails = bottomSheetView.findViewById(R.id.tvSeeDetail);
                tvDetails.setVisibility(View.GONE);
                TextView tvEditExpense = bottomSheetView.findViewById(R.id.tvEditIncomes);
                TextView tvDeleteExpense = bottomSheetView.findViewById(R.id.tvDeleteIE);
                tvEditExpense.setText("Sửa loại chi");
                tvDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                tvEditExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.add_incomes_expenses_type);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final TextView tvExpenseType = dialog.findViewById(R.id.add_incomes_type);
                        Button btnCancel = dialog.findViewById(R.id.btnCancel);
                        final Button btEdit = dialog.findViewById(R.id.btnAdd);
                        final TextView tvTitle = dialog.findViewById(R.id.tvAddType);
                        tvTitle.setText("SỬA LOẠI CHI");
                        tvExpenseType.setText(incomesExpenses.getIeName());
                        btEdit.setText("SỬA");
                        btEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String expenseTypeNewName = tvExpenseType.getText().toString();
                                IncomesExpenses ie = new IncomesExpenses(incomesExpenses.getIeID(), expenseTypeNewName, Constant.EXPENSES);
                                mData.child("incomesExpensesTypes").child(incomesExpenses.getIeID()).child("ieName").setValue(expenseTypeNewName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
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
                tvDeleteExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_delete);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final TextView tvConfirmMessage = dialog.findViewById(R.id.txt_Titleconfirm);
                        Button btnCancel = dialog.findViewById(R.id.btnCancel);
                        final Button btnAdd = dialog.findViewById(R.id.btnAdd);
                        final Button btnYes = dialog.findViewById(R.id.btnYes);
                        final Button btnNo = dialog.findViewById(R.id.btnNo);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        tvConfirmMessage.setText("Bạn có muốn xóa " + IEList.get(position).getIeName() + " hay không ? ");
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mData.child("incomesExpensesTypes").child(incomesExpenses.getIeID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            deleteChildWithIEIDEquals(incomesExpenses.getIeID());
                                            tvConfirmMessage.setText("");
                                            progressBar.setVisibility(View.VISIBLE);
                                            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    notifyDataSetChanged();
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
                        });
                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                bottomSheetView.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
        holder.imvAvatarItem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
    }

    private void deleteChildWithIEIDEquals(String ieID) {
        for (Transactions trans : transactionList) {
            if (trans.getIeID().matches(ieID)) {
                mData.child("transactions").child(trans.getTransID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constant.TAG, "delete trans - " + transactionList);
                        } else {
                            Log.d(Constant.TAG, "delete trans failed");
                        }
                    }
                });
            }
        }
        for (int i = 0; i < transactionList.size();i ++) {
            if (transactionList.get(i).getIeID().matches(ieID)) {
                transactionList.remove(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return IEList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView imvAvatarItem;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textList);
            imvAvatarItem = itemView.findViewById(R.id.img_avatarItem);
            relativeLayout = itemView.findViewById(R.id.relative_item);
        }
    }
}