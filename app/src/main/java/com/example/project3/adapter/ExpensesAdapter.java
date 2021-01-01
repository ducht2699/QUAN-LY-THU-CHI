package com.example.project3.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.Transactions;
import com.example.project3.model.IncomesExpenses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {
    private Context context;
    private List<Transactions> transactionsList;
    private List<IncomesExpenses> IEList;
    private DatePickerDialog datePickerDialog;
    private int layout;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private DAOIncomesExpenses daoIncomesExpenses;

    public ExpensesAdapter() {
    }

    public ExpensesAdapter(Context context, int layout, List<Transactions> transactionsList, List<IncomesExpenses> IEList, DAOIncomesExpenses daoIncomesExpenses) {
        this.context = context;
        this.transactionsList = transactionsList;
        this.IEList = IEList;
        this.layout = layout;
        this.daoIncomesExpenses = daoIncomesExpenses;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView text;
        private ImageView img_avataitem;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textList);
            img_avataitem = itemView.findViewById(R.id.img_avatarItem);
            relativeLayout = itemView.findViewById(R.id.relative_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
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
        holder.text.setText(transactionsList.get(position).getTransDescription());
        final Transactions transactions = transactionsList.get(position);
        //click on item
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
                TextView tvSeeDetails = bottomSheetView.findViewById(R.id.tvSeeDetail);
                TextView tvEditExpense = bottomSheetView.findViewById(R.id.tvEditIncomes);
                TextView tvDelete = bottomSheetView.findViewById(R.id.tvDeleteIE);
                tvEditExpense.setText("Sửa khoản chi");
                tvSeeDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Transactions trans = transactionsList.get(position);
                        //Format money type
                        NumberFormat fm = new DecimalFormat("#,###");
                        //show transaction's info when click on item
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.transaction_info);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        TextView tvTransDes, tvTransDate, tMoney, tvIEType, tvTitle;
                        tvTitle = dialog.findViewById(R.id.tvTransTitle);
                        tvTransDes = dialog.findViewById(R.id.tvTransDes);
                        tvTransDate = dialog.findViewById(R.id.tvTransDate);
                        tMoney = dialog.findViewById(R.id.tvMoney);
                        tvIEType = dialog.findViewById(R.id.tvIEType);
                        tvTitle.setText("THÔNG TIN CHI");
                        tvTransDes.setText(trans.getTransDescription());
                        tvTransDate.setText(dfm.format(trans.getTransDate()));
                        tMoney.setText(fm.format(Math.abs(trans.getAmountMoney())) + " VND");
                        String ieID = getNameIE(trans.getIeID(), Constant.EXPENSES);
                        tvIEType.setText(ieID);
                        dialog.show();
                    }
                });
                tvEditExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.add_incomes_expenses);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final TextView edtTransDes = dialog.findViewById(R.id.add_trans_description);
                        final TextView etvTransDate = dialog.findViewById(R.id.add_trans_date);
                        final TextView edtTransMoney = dialog.findViewById(R.id.add_trans_money);
                        final Spinner spnTransType = dialog.findViewById(R.id.spnTransType);
                        final TextView tvTitle = dialog.findViewById(R.id.titleAddTrans);
                        final Button btnCancel = dialog.findViewById(R.id.btnCancelTrans);
                        final Button btnEdit = dialog.findViewById(R.id.btnAddTrans);
                        //Set title, text
                        tvTitle.setText("SỬA KHOẢN CHI");
                        btnEdit.setText("SỬA");
                        edtTransDes.setText(transactions.getTransDescription());
                        etvTransDate.setText(dfm.format(transactions.getTransDate()));
                        edtTransMoney.setText(String.valueOf(transactions.getAmountMoney()));
                        final ArrayAdapter spnAdapter = new ArrayAdapter(context, R.layout.spiner, IEList);
                        spnTransType.setAdapter(spnAdapter);
                        int pos = -1;
                        for (int i = 0; i < IEList.size(); i++) {
                            if (IEList.get(i).getIeName().equalsIgnoreCase(getNameIE(transactions.getIeID(), Constant.INCOME))) {
                                pos = i;
                                break;
                            }
                        }
                        spnTransType.setSelection(pos);
                        //click on date show date chooser
                        etvTransDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Calendar calendar = Calendar.getInstance();
                                int d = calendar.get(Calendar.DAY_OF_MONTH);
                                int m = calendar.get(Calendar.MONTH);
                                int y = calendar.get(Calendar.YEAR);
                                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        final String tempTransDate = dayOfMonth + "/" + month + "/" + year;
                                        etvTransDate.setText(tempTransDate);
                                    }
                                }, y, m, d);
                                datePickerDialog.show();
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                spnTransType.setAdapter(spnAdapter);
                            }
                        });
                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String transDes = edtTransDes.getText().toString();
                                String transDate = etvTransDate.getText().toString();
                                String transMoney = edtTransMoney.getText().toString();
                                IncomesExpenses tc = (IncomesExpenses) spnTransType.getSelectedItem();
                                String ieID = tc.getIeID();
                                if (transDes.isEmpty() && transDate.isEmpty() && transMoney.isEmpty()) {
                                    Toast.makeText(context, "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        Transactions trans = new Transactions(transactions.getTransID(), transDes, dfm.parse(transDate), Integer.parseInt(transMoney), ieID);
                                        daoIncomesExpenses.editTransaction(trans, context, dialog);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        });
                        dialog.show();
                    }
                });
                tvDelete.setOnClickListener(new View.OnClickListener() {
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
                        final Button btnYes = dialog.findViewById(R.id.btnYes);
                        final Button btnNo = dialog.findViewById(R.id.btnNo);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        tvConfirmMessage.setText("Bạn có muốn xóa " + transactionsList.get(position).getTransDescription() + " hay không ? ");
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                daoIncomesExpenses.deleteTransaction(tvConfirmMessage, progressBar, dialog, context, transactions);
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
        holder.img_avataitem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
    }

    private String getNameIE(String ieID, int ieType) {
        String ieName = "";
        for (IncomesExpenses ie : IEList) {
            if (ie.getIeID().matches(ieID) && ie.getIeType() == ieType) {
                ieName = ie.getIeName();
                break;
            }
        }
        return ieName;
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }
}