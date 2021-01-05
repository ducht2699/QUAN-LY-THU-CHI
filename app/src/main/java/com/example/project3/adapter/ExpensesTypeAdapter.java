package com.example.project3.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.IncomesExpenses;
import com.example.project3.model.Transactions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class ExpensesTypeAdapter extends RecyclerView.Adapter<ExpensesTypeAdapter.ViewHolder> {
    private Context context;
    private List<IncomesExpenses> IEList;
    private List<Transactions> transactionList;
    private int layout;
    private DAOIncomesExpenses daoIncomesExpenses;

    public ExpensesTypeAdapter() {
    }

    public ExpensesTypeAdapter(Context context, int layout, List<IncomesExpenses> IEList, List<Transactions> transactionList, DAOIncomesExpenses daoIncomesExpenses) {
        this.context = context;
        this.transactionList = transactionList;
        this.IEList = IEList;
        this.daoIncomesExpenses = daoIncomesExpenses;
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
                                daoIncomesExpenses.editIEType(ie.getIeID(), expenseTypeNewName, context, dialog);
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
                        final TextView tvConfirmMessage = dialog.findViewById(R.id.tvConfirmTitle);
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
                                daoIncomesExpenses.deleteIEType(incomesExpenses.getIeID(), tvConfirmMessage, progressBar, dialog, context);
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
                bottomSheetView.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
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