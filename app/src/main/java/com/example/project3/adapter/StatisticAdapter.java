package com.example.project3.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.StatisticDetails;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.dao.DAOStatisticType;
import com.example.project3.dao.DAOUsers;
import com.example.project3.fragment.statistic_details.CurrentStatisticFragment;
import com.example.project3.fragment.statistic_details.DeptLoanFragment;
import com.example.project3.fragment.statistic_details.ExpenseAnalysisFragment;
import com.example.project3.fragment.statistic_details.FinancialAnalyticFragment;
import com.example.project3.fragment.statistic_details.IEEventFragment;
import com.example.project3.fragment.statistic_details.IEObjectFragment;
import com.example.project3.fragment.statistic_details.IESituationFragment;
import com.example.project3.fragment.statistic_details.IncomeAnalysisFragment;
import com.example.project3.model.StatisticType;

import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {
    private Context context;
    private int layout;
    private List<StatisticType> statisticTypeList;
    private DAOStatisticType daoStatisticType;
    private DAOIncomesExpenses daoIncomesExpenses;
    private DAOUsers daoUsers;

    public StatisticAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        daoStatisticType = new DAOStatisticType();
        statisticTypeList = daoStatisticType.getStatisticTypesList();
        daoIncomesExpenses = new DAOIncomesExpenses();
        daoUsers = new DAOUsers();
        daoUsers.addAccountTypeListener();
        daoUsers.createAccountTypeAdapter(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new StatisticAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.imgAvatarItem.setImageResource(statisticTypeList.get(position).getTypeImage());
        holder.tvName.setText(statisticTypeList.get(position).getTypeName());
        holder.relativeItem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.imgAvatarItem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        final StatisticType statisticType = statisticTypeList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity appCompatActivity = (Activity) v.getContext();
                Intent i = new Intent(context, StatisticDetails.class);
                setDefaultExtraData(i, statisticType);
                appCompatActivity.startActivity(i);
            }
        });
    }

    private void setDefaultExtraData(Intent intent, StatisticType statisticType) {
        intent.putExtra("statisticID", statisticType.getStatisticID());
        switch (statisticType.getTypeImage()) {
            case R.drawable.ic_current_finance:
                intent.putExtra("currentIncomes", daoIncomesExpenses.calculateIE(Constant.INCOME));
                intent.putExtra("currentMoney", daoUsers.getTotalMoney());
                intent.putExtra("currentExpense", daoIncomesExpenses.calculateIE(Constant.EXPENSES));
                break;
            case R.drawable.ic_ie_situation:

                break;
            case R.drawable.ic_expense_analysis:

                break;
            case R.drawable.ic_income_analysis:

                break;
            case R.drawable.ic_dept_loan:

                break;
            case R.drawable.ic_ie_object:

                break;
            case R.drawable.ic_event:

                break;
            case R.drawable.ic_financial_analytics:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return statisticTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView imgAvatarItem;
        private RelativeLayout relativeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textList);
            imgAvatarItem = itemView.findViewById(R.id.img_avatarItem);
            relativeItem = itemView.findViewById(R.id.relative_item);
        }
    }
}
