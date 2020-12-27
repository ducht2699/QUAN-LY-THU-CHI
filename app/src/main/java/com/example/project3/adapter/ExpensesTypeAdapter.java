package com.example.project3.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import com.example.project3.dao.DAOTransactions;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.IncomesExpenses;
import com.example.project3.model.Transactions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ExpensesTypeAdapter extends RecyclerView.Adapter<ExpensesTypeAdapter.ViewHolder> {
    private Context context;
    private List<IncomesExpenses> IEList;
    private DAOIncomesExpenses daoIncomesExpenses;
    private DAOTransactions daoTransactions;
    private int layout;

    public ExpensesTypeAdapter() {
    }

    public ExpensesTypeAdapter(Context context, ArrayList<IncomesExpenses> IEList) {
        this.context = context;
        this.IEList = IEList;
    }

    public ExpensesTypeAdapter(Context context, int layout, List<IncomesExpenses> IEList) {
        this.context = context;
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
        daoIncomesExpenses = new DAOIncomesExpenses();
        daoTransactions = new DAOTransactions();
        final IncomesExpenses tc = IEList.get(position);

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
                TextView txtXemchiTiet = bottomSheetView.findViewById(R.id.tvSeeDetail);
                txtXemchiTiet.setVisibility(View.GONE);
                TextView txtSuaKhoanChi = bottomSheetView.findViewById(R.id.tvEditIncomes);
                TextView txtXoa = bottomSheetView.findViewById(R.id.tvDeleteIE);
                txtSuaKhoanChi.setText("Sửa loại chi");

                txtXemchiTiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();

                    }
                });
                txtSuaKhoanChi.setOnClickListener(new View.OnClickListener() {
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
                        final TextView text = dialog.findViewById(R.id.add_incomes_type);
                        Button xoa = dialog.findViewById(R.id.btnCancel);
                        final Button them = dialog.findViewById(R.id.btnAdd);
                        final TextView title = dialog.findViewById(R.id.tvAddType);
                        title.setText("SỬA LOẠI CHI");
                        text.setText(tc.getIeName());
                        them.setText("SỬA");

                        them.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String themText = text.getText().toString();

                                IncomesExpenses incomesExpenses = new IncomesExpenses(tc.getIeID(), themText, Constant.EXPENSES);
                                if (daoIncomesExpenses.editIE(incomesExpenses) == true) {
                                    IEList.clear();
                                    IEList.addAll(daoIncomesExpenses.getIE(1));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        xoa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });

                txtXoa.setOnClickListener(new View.OnClickListener() {
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
                        final TextView txt_Massage = dialog.findViewById(R.id.txt_Titleconfirm);
                        Button xoa = dialog.findViewById(R.id.btnCancel);
                        final Button them = dialog.findViewById(R.id.btnAdd);
                        final Button btn_Yes = dialog.findViewById(R.id.btnYes);
                        final Button btn_No = dialog.findViewById(R.id.btnNo);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        txt_Massage.setText("Bạn có muốn xóa " + IEList.get(position).getIeName() + " hay không ? ");
                        btn_Yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (daoIncomesExpenses.deleteIE(tc)) {
                                    txt_Massage.setText("");
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            IEList.clear();
                                            IEList.addAll(daoIncomesExpenses.getIE(1));
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                        }
                                    }, 2000);

                                }

                            }
                        });
                        btn_No.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return IEList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView img_avataitem;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textList);
            img_avataitem = itemView.findViewById(R.id.img_avatarItem);
            relativeLayout = itemView.findViewById(R.id.relative_item);

        }
    }
}