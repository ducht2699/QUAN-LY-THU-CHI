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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.R;
import com.example.project3.dao.DAOUsers;
import com.example.project3.model.AccountType;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AccountTypeAdapter extends RecyclerView.Adapter<AccountTypeAdapter.ViewHolder> {
    private Context context;
    private List<AccountType> accountTypeList;
    private int layout;
    private DAOUsers daoUsers;

    public AccountTypeAdapter(Context context, List<AccountType> accountTypeList, int layout, DAOUsers daoUsers) {
        this.context = context;
        this.accountTypeList = accountTypeList;
        this.layout = layout;
        this.daoUsers = daoUsers;
    }

    @NonNull
    @Override
    public AccountTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(layout, parent, false);
        return new AccountTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountTypeAdapter.ViewHolder holder, final int position) {
        final AccountType accountType = accountTypeList.get(position);
        holder.tvName.setText(accountType.getAccountName());
        holder.imvAvatarItem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_action, (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer));
                TextView tvSeeDetails = bottomSheetView.findViewById(R.id.tvSeeDetail);
                TextView tvEditIncomes = bottomSheetView.findViewById(R.id.tvEditIncomes);
                TextView tvDeleteIncomes = bottomSheetView.findViewById(R.id.tvDeleteIE);
                TextView tvCancel = bottomSheetView.findViewById(R.id.tvCancel);
                TextView tvEdit = bottomSheetView.findViewById(R.id.tvEditIncomes);
                TextView tvDelete = bottomSheetView.findViewById(R.id.tvDeleteIE);
                tvEditIncomes.setText("Sửa tài khoản");
                tvSeeDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        if (position == RecyclerView.NO_POSITION) return;
                        AccountType accountTypeTemp = accountTypeList.get(position);
                        //Format money type
                        NumberFormat fm = new DecimalFormat("#,###");
                        //show transaction's info when click on item
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.account_type_info);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        TextView tvMoney, tvName;
                        tvMoney = dialog.findViewById(R.id.tvMoney);
                        tvName = dialog.findViewById(R.id.tvAccountTypeName);
                        tvName.setText(accountType.getAccountName());
                        tvMoney.setText(fm.format(accountTypeTemp.getAmountMoney()) + " VND");
                        dialog.show();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
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
                        final TextView tvMessage = dialog.findViewById(R.id.tvConfirmTitle);
                        final Button btnYes = dialog.findViewById(R.id.btnYes);
                        final Button btnNo = dialog.findViewById(R.id.btnNo);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        tvMessage.setText("Bạn có muốn xóa " + accountType.getAccountName() + " hay không ? ");
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                daoUsers.deleteAccountType(context, accountType, tvMessage, progressBar, dialog);
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
                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.add_account_type);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final EditText edtEditAccountName = dialog.findViewById(R.id.edtAccountType);
                        final Button btnCancel = dialog.findViewById(R.id.btnCancel);
                        final Button btnEdit = dialog.findViewById(R.id.btnAdd);
                        final TextView tvTitle = dialog.findViewById(R.id.titleAddAccount);
                        final EditText edtMoney = dialog.findViewById(R.id.edtAmountMoney);
                        tvTitle.setText("SỬA TÀI KHOẢN");
                        edtEditAccountName.setHint("Nhập tên tài khoản");
                        edtEditAccountName.setText(accountType.getAccountName());
                        edtMoney.setText(String.valueOf(accountType.getAmountMoney()));
                        btnEdit.setText("SỬA");
                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AccountType accountTypeTemp = new AccountType(accountType.getAccountTypeID(), edtEditAccountName.getText().toString(), Long.valueOf(edtMoney.getText().toString()));
                                daoUsers.editAccountType(accountTypeTemp, context, dialog);
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
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountTypeList.size();
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
