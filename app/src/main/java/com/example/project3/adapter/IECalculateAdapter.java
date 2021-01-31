package com.example.project3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constants;
import com.example.project3.R;
import com.example.project3.model.IECalculation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class IECalculateAdapter extends RecyclerView.Adapter<IECalculateAdapter.ViewHolder> {
    private Context context;
    private int layout;
    private List<IECalculation> ieCalculationsList;

    public IECalculateAdapter(Context context, int layout, List<IECalculation> ieCalculationsList) {
        this.context = context;
        this.layout = layout;
        this.ieCalculationsList = ieCalculationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(layout, parent, false);
        return new IECalculateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IECalculation ieCalculation = ieCalculationsList.get(position);
        String title = "";
        switch (ieCalculation.getTimeType()) {
            case Constants.MONTH:
                title = "Tháng ";
                break;
            case Constants.QUARTER:
                title = "Quý ";
                break;
            case Constants.YEAR:
                title = "Năm ";
                break;
        }
        title += ieCalculation.getTime();
        holder.tvTitleTime.setText(title);
        holder.tvIncome.setText(moneyStandardize(String.valueOf(ieCalculation.getIncomes())));
        holder.tvExpense.setText(moneyStandardize(String.valueOf(ieCalculation.getExpense())));
        holder.tvMoneyLeft.setText(moneyStandardize(String.valueOf(ieCalculation.getIncomes() - ieCalculation.getExpense())));
    }

    private String moneyStandardize(String money) {
        int mn = Integer.valueOf(money);
        NumberFormat fm = new DecimalFormat("#,###");
        return fm.format(mn).toString() + " VND";
    }

    @Override
    public int getItemCount() {
        return ieCalculationsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitleTime, tvIncome, tvExpense, tvMoneyLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleTime = itemView.findViewById(R.id.tvTitleTime);
            tvIncome = itemView.findViewById(R.id.tvIncome);
            tvExpense = itemView.findViewById(R.id.tvExpense);
            tvMoneyLeft = itemView.findViewById(R.id.tvMoneyLeft);
        }
    }

}
