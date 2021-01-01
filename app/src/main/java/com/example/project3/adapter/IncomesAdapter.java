package com.example.project3.adapter;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class IncomesAdapter extends RecyclerView.Adapter<IncomesAdapter.ViewHolder> {
    private Context context;
    private List<Transactions> transactionsList;
    private List<IncomesExpenses> IEList;
    private int layout;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private DatePickerDialog datePickerDialog;
    private boolean isDark = false;
    private DAOIncomesExpenses daoIncomesExpenses;

    public IncomesAdapter() {
    }

    public IncomesAdapter(Context context, int layout, List<Transactions>transactionsList, List<IncomesExpenses> IEList, DAOIncomesExpenses daoIncomesExpenses) {
        this.context = context;
        this.layout = layout;
        this.daoIncomesExpenses = daoIncomesExpenses;
        this.transactionsList = transactionsList;
        this.IEList = IEList;
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
        holder.tvName.setText(transactionsList.get(position).getTransDescription());
        final Transactions transaction = transactionsList.get(position);
        //click on item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(context).inflate(
                        R.layout.bottom_sheet_action,
                        (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
                TextView tvSeeDetails = bottomSheetView.findViewById(R.id.tvSeeDetail);
                TextView tvEditIncomes = bottomSheetView.findViewById(R.id.tvEditIncomes);
                TextView tvDeleteIncomes = bottomSheetView.findViewById(R.id.tvDeleteIE);
                tvEditIncomes.setText("Sửa khoản thu");
                tvSeeDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        if (position == RecyclerView.NO_POSITION) return;
                        Transactions transactionsTemp = transactionsList.get(position);
                        //Format money type
                        NumberFormat fm = new DecimalFormat("#,###");
                        //show transaction's info when click on item
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.transaction_info);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        TextView tvDescription, tvDate, tvMoney, tvIEType, tvTitle;
                        tvDescription = dialog.findViewById(R.id.tvTransDes);
                        tvDate = dialog.findViewById(R.id.tvTransDate);
                        tvMoney = dialog.findViewById(R.id.tvMoney);
                        tvIEType = dialog.findViewById(R.id.tvIEType);
                        tvTitle = dialog.findViewById(R.id.tvTransTitle);
                        tvTitle.setText("THÔNG TIN THU");
                        tvDescription.setText(transactionsTemp.getTransDescription());
                        tvDate.setText(dfm.format(transactionsTemp.getTransDate()));
                        tvMoney.setText(fm.format(transactionsTemp.getAmountMoney()) + " VND");
                        String ieID = getNameIE(transactionsTemp.getIeID(), Constant.INCOME);
                        tvIEType.setText(ieID);
                        dialog.show();
                    }
                });
                tvEditIncomes.setOnClickListener(new View.OnClickListener() {
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
                        final EditText edtTransDes = dialog.findViewById(R.id.add_trans_description);
                        final TextView tvTransDate = dialog.findViewById(R.id.add_trans_date);
                        final EditText edtTransMoney = dialog.findViewById(R.id.add_trans_money);
                        final Spinner spnIEType = dialog.findViewById(R.id.spnTransType);
                        final TextView tvTransTitle = dialog.findViewById(R.id.titleAddTrans);
                        final Button btnCancel = dialog.findViewById(R.id.btnCancelTrans);
                        final Button btnEdit = dialog.findViewById(R.id.btnAddTrans);
                        //Set title, text
                        tvTransTitle.setText("SỬA KHOẢN THU");
                        btnEdit.setText("SỬA");
                        edtTransDes.setText(transaction.getTransDescription());
                        tvTransDate.setText(dfm.format(transaction.getTransDate()));
                        edtTransMoney.setText(String.valueOf(transaction.getAmountMoney()));
                        final ArrayAdapter spnAdapter = new ArrayAdapter(context, R.layout.spiner, IEList);
                        spnIEType.setAdapter(spnAdapter);
                        int pos = -1;
                        for (int i = 0; i < IEList.size(); i++) {
                            if (IEList.get(i).getIeName().equalsIgnoreCase(getNameIE(transaction.getIeID(), Constant.INCOME))) {
                                pos = i;
                                break;
                            }
                        }
                        spnIEType.setSelection(pos);
                        //click on date show date chooser
                        tvTransDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Calendar calendar = Calendar.getInstance();
                                int d = calendar.get(Calendar.DAY_OF_MONTH);
                                int m = calendar.get(Calendar.MONTH);
                                int y = calendar.get(Calendar.YEAR);
                                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        final String transDate = dayOfMonth + "/" + month + "/" + year;
                                        tvTransDate.setText(transDate);
                                    }
                                }, y, m, d);
                                datePickerDialog.show();
                            }
                        });
                        //click on cancel button
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        //click on edit button
                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String transDes = edtTransDes.getText().toString();
                                String transDate = tvTransDate.getText().toString();
                                String transMoney = edtTransMoney.getText().toString();
                                IncomesExpenses incomesExpenses = (IncomesExpenses) spnIEType.getSelectedItem();
                                String ieID = incomesExpenses.getIeID();
                                if (transDes.isEmpty() && transDate.isEmpty() && transMoney.isEmpty()) {
                                    Toast.makeText(context, "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        Transactions transactions = new Transactions(transaction.getTransID(), transDes, dfm.parse(transDate), Integer.parseInt(transMoney), ieID);
                                        daoIncomesExpenses.editTransaction(transactions, context, dialog);
                                    } catch (Exception ex) {
                                        Log.d(Constant.TAG, "error edit trans - " + ex);
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        });
                        dialog.show();
                    }
                });
                tvDeleteIncomes.setOnClickListener(new View.OnClickListener() {
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
                        final TextView tvMessage = dialog.findViewById(R.id.txt_Titleconfirm);
                        Button btnCancel = dialog.findViewById(R.id.btnCancel);
                        final Button btnAdd = dialog.findViewById(R.id.btnAdd);
                        final Button btnYes = dialog.findViewById(R.id.btnYes);
                        final Button btnNo = dialog.findViewById(R.id.btnNo);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        tvMessage.setText("Bạn có muốn xóa " + transactionsList.get(position).getTransDescription() + " hay không ? ");
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                daoIncomesExpenses.deleteTransaction(tvMessage, progressBar, dialog, context, transaction);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView imvAvatarItem;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textList);
            imvAvatarItem = itemView.findViewById(R.id.img_avatarItem);
            relativeLayout = itemView.findViewById(R.id.relative_item);
        }
    }
}