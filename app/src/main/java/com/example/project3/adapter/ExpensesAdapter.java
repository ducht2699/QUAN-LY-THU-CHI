package com.example.project3.adapter;

import android.app.DatePickerDialog;
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

import com.example.project3.R;
import com.example.project3.dao.DAOTransactions;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.Transactions;
import com.example.project3.model.IncomesExpenses;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {
    private Context context;
    public static ArrayList<Transactions> list;
    private DAOTransactions daoTransactions;
    private ArrayList<IncomesExpenses> listTC = new ArrayList<>();
    private DAOIncomesExpenses daoIncomesExpenses;
    private DatePickerDialog datePickerDialog;
    int layout;
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");

    public ExpensesAdapter() {
    }

    public ExpensesAdapter(Context context, ArrayList<Transactions> list) {
        this.context = context;
        this.list = list;
    }

    public ExpensesAdapter(Context context, int layout, ArrayList<Transactions> list) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView text;
        private ImageView img_avataitem;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textList);
            img_avataitem = itemView.findViewById(R.id.img_avataitem);
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
        holder.text.setText(list.get(position).getTransDescription());
        daoTransactions = new DAOTransactions(context);
        final Transactions gd = list.get(position);
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
                TextView txtXemchiTiet = bottomSheetView.findViewById(R.id.txt_XemChiTiet);
                TextView txtSuaKhoanChi = bottomSheetView.findViewById(R.id.txt_SuaThuChi);
                TextView txtXoa = bottomSheetView.findViewById(R.id.txt_XoaThuChi);
                txtSuaKhoanChi.setText("Sửa khoản chi");
                txtXemchiTiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        Transactions gd = list.get(position);
                        //Format money type
                        NumberFormat fm = new DecimalFormat("#,###");
                        //show transaction's info when click on item
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.transaction_info);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        TextView mota, ngay, tien, loai, title;
                        title = dialog.findViewById(R.id.thongtinGD);
                        mota = dialog.findViewById(R.id.mota_gd);
                        ngay = dialog.findViewById(R.id.ngay_gd);
                        tien = dialog.findViewById(R.id.tien_gd);
                        loai = dialog.findViewById(R.id.loai_gd);
                        title.setText("THÔNG TIN CHI");
                        mota.setText(gd.getTransDescription());
                        ngay.setText(String.valueOf(gd.getTransDate()));
                        tien.setText(fm.format(Math.abs(gd.getAmountMoney())) + " VND");
                        daoIncomesExpenses = new DAOIncomesExpenses(context);
                        loai.setText(daoIncomesExpenses.getNameIE(gd.getIeID()));
                        dialog.show();
                    }
                });
                txtSuaKhoanChi.setOnClickListener(new View.OnClickListener() {
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
                        final TextView moTaGd = dialog.findViewById(R.id.them_mota_gd);
                        final TextView ngayGd = dialog.findViewById(R.id.them_ngay_gd);
                        final TextView tienGd = dialog.findViewById(R.id.them_tien_gd);
                        final Spinner spLoaiGd = dialog.findViewById(R.id.spLoaiGd);
                        final TextView title = dialog.findViewById(R.id.titleThemKhoan);
                        final Button huy = dialog.findViewById(R.id.huyThemGD);
                        final Button them = dialog.findViewById(R.id.btnThemGD);
                        daoIncomesExpenses = new DAOIncomesExpenses(context);
                        listTC = daoIncomesExpenses.getIE(1);
                        //Set title, text
                        title.setText("SỬA KHOẢN CHI");
                        them.setText("SỬA");
                        moTaGd.setText(gd.getTransDescription());
                        ngayGd.setText(dfm.format(gd.getTransDate()));
                        tienGd.setText(String.valueOf(gd.getAmountMoney()));
                        final ArrayAdapter sp = new ArrayAdapter(context, R.layout.spiner, listTC);
                        spLoaiGd.setAdapter(sp);
                        int vitri = -1;
                        for (int i = 0; i < listTC.size(); i++) {
                            if (listTC.get(i).getIeName().equalsIgnoreCase(daoIncomesExpenses.getNameIE(gd.getIeID()))) {
                                vitri = i;
                                break;
                            }
                        }
                        spLoaiGd.setSelection(vitri);


                        //click on date show date chooser
                        ngayGd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Calendar calendar = Calendar.getInstance();
                                int d = calendar.get(Calendar.DAY_OF_MONTH);
                                int m = calendar.get(Calendar.MONTH);
                                int y = calendar.get(Calendar.YEAR);
                                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        final String NgayGD = dayOfMonth + "/" + month + "/" + year;
                                        ngayGd.setText(NgayGD);
                                    }
                                }, y, m, d);
                                datePickerDialog.show();
                            }
                        });

                        //click on delete button
                        huy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                spLoaiGd.setAdapter(sp);
                            }
                        });

                        //click on edit button
                        them.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mota = moTaGd.getText().toString();
                                String ngay = ngayGd.getText().toString();
                                String tien = tienGd.getText().toString();
                                IncomesExpenses tc = (IncomesExpenses) spLoaiGd.getSelectedItem();
                                int ma = tc.getIeID();
                                if (mota.isEmpty() && ngay.isEmpty() && tien.isEmpty()) {
                                    Toast.makeText(context, "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        Transactions transactions = new Transactions(gd.getTransID(), mota, dfm.parse(ngay), Integer.parseInt(tien), ma);
                                        if (daoTransactions.editTrans(transactions) == true) {
                                            list.clear();
                                            list.addAll(daoTransactions.getTransByIE(1));
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

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
                        final Button btn_Yes = dialog.findViewById(R.id.btn_yes);
                        final Button btn_No = dialog.findViewById(R.id.btn_no);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        txt_Massage.setText("Bạn có muốn xóa " + list.get(position).getTransDescription() + " hay không ? ");
                        btn_Yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (daoTransactions.deleteTrans(gd) == true) {
                                    txt_Massage.setText("");
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            list.clear();
                                            list.addAll(daoTransactions.getTransByIE(1));
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
        return list.size();
    }
}