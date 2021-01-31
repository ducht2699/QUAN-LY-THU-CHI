package com.example.project3.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constants;
import com.example.project3.R;
import com.example.project3.StatisticDetails;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.dao.DAOStatisticType;
import com.example.project3.dao.DAOUsers;
import com.example.project3.model.StatisticType;

import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {
    private Context context;
    private int layout;
    private List<StatisticType> statisticTypeList;
    private DAOStatisticType daoStatisticType;
    private DAOIncomesExpenses daoIncomesExpenses;
    private DAOUsers daoUsers;

    public StatisticAdapter(Context context, int layout, DAOIncomesExpenses daoIncomesExpenses) {
        this.context = context;
        this.layout = layout;
        daoStatisticType = new DAOStatisticType();
        statisticTypeList = daoStatisticType.getStatisticTypesList();
        this.daoIncomesExpenses = daoIncomesExpenses;
        daoUsers = new DAOUsers();
        daoUsers.addAccountTypeListener(true);
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
        //current statistic data
        intent.putExtra("currentIncomes", daoIncomesExpenses.calculateIE(Constants.INCOME));
        intent.putExtra("currentMoney", daoUsers.getTotalMoney());
        intent.putExtra("currentExpense", daoIncomesExpenses.calculateIE(Constants.EXPENSES));
        //IE situation data
        intent.putExtra("todayIncomes", daoIncomesExpenses.calculateIE(Constants.INCOME, Constants.NOW));
        intent.putExtra("weekIncomes", daoIncomesExpenses.calculateIE(Constants.INCOME, Constants.WEEK));
        intent.putExtra("monthIncomes", daoIncomesExpenses.calculateIE(Constants.INCOME, Constants.MONTH));
        intent.putExtra("quarterIncomes", daoIncomesExpenses.calculateIE(Constants.INCOME, Constants.QUARTER));
        intent.putExtra("yearIncomes", daoIncomesExpenses.calculateIE(Constants.INCOME, Constants.YEAR));
        intent.putExtra("todayExpense", daoIncomesExpenses.calculateIE(Constants.EXPENSES, Constants.NOW));
        intent.putExtra("weekExpense", daoIncomesExpenses.calculateIE(Constants.EXPENSES, Constants.WEEK));
        intent.putExtra("monthExpense", daoIncomesExpenses.calculateIE(Constants.EXPENSES, Constants.MONTH));
        intent.putExtra("quarterExpense", daoIncomesExpenses.calculateIE(Constants.EXPENSES, Constants.QUARTER));
        intent.putExtra("yearExpense", daoIncomesExpenses.calculateIE(Constants.EXPENSES, Constants.YEAR));
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
