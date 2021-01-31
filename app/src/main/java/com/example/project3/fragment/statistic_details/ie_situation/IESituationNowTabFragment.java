package com.example.project3.fragment.statistic_details.ie_situation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.project3.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class IESituationNowTabFragment extends Fragment {
    private View view;
    private TextView tvTodayIncome, tvTodayExpense, tvTodayMoney, tvWeekIncome, tvWeekExpense, tvWeekMoney, tvMonthIncome, tvMonthExpense, tvMonthMoney, tvQuarterIncome, tvQuarterExpense, tvQuarterMoney, tvYearIncome, tvYearExpense, tvYearMoney;

    public IESituationNowTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_i_e_situation_now_tab, container, false);
        init();
        final Bundle bundle = getArguments();
        int inMoney = Integer.valueOf(bundle.getString("todayIncomes"));
        int outMoney = Integer.valueOf(bundle.getString("todayExpense"));
        tvTodayIncome.setText(moneyStandardize(String.valueOf(inMoney)));
        tvTodayExpense.setText(moneyStandardize(String.valueOf(outMoney)));
        tvTodayMoney.setText(moneyStandardize(String.valueOf(inMoney - outMoney)));
        tvWeekIncome.setText(moneyStandardize(bundle.getString("weekIncomes")));
        tvWeekExpense.setText(moneyStandardize(bundle.getString("weekExpense")));
        inMoney = Integer.valueOf(bundle.getString("weekIncomes"));
        outMoney = Integer.valueOf(bundle.getString("weekExpense"));
        tvWeekMoney.setText(moneyStandardize(String.valueOf(inMoney - outMoney)));
        tvMonthIncome.setText(moneyStandardize(bundle.getString("monthIncomes")));
        tvMonthExpense.setText(moneyStandardize(bundle.getString("monthExpense")));
        inMoney = Integer.valueOf(bundle.getString("monthIncomes"));
        outMoney = Integer.valueOf(bundle.getString("monthExpense"));
        tvMonthMoney.setText(moneyStandardize(String.valueOf(inMoney - outMoney)));
        tvQuarterIncome.setText(moneyStandardize(bundle.getString("quarterIncomes")));
        tvQuarterExpense.setText(moneyStandardize(bundle.getString("quarterExpense")));
        inMoney = Integer.valueOf(bundle.getString("quarterIncomes"));
        outMoney = Integer.valueOf(bundle.getString("quarterExpense"));
        tvQuarterMoney.setText(moneyStandardize(String.valueOf(inMoney - outMoney)));
        tvYearIncome.setText(moneyStandardize(bundle.getString("yearIncomes")));
        tvYearExpense.setText(moneyStandardize(bundle.getString("yearExpense")));
        inMoney = Integer.valueOf(bundle.getString("yearIncomes"));
        outMoney = Integer.valueOf(bundle.getString("yearExpense"));
        tvYearMoney.setText(moneyStandardize(String.valueOf(inMoney - outMoney)));
        return view;
    }

    private String moneyStandardize(String money) {
        int mn = Integer.valueOf(money);
        NumberFormat fm = new DecimalFormat("#,###");
        return fm.format(mn).toString() +" VND";
    }

    private void init() {
        tvTodayIncome = view.findViewById(R.id.tvTodayIncome);
        tvTodayExpense = view.findViewById(R.id.tvTodayExpense);
        tvTodayMoney = view.findViewById(R.id.tvTodayMoney);
        tvWeekIncome = view.findViewById(R.id.tvWeekIncome);
        tvWeekExpense = view.findViewById(R.id.tvWeekExpense);
        tvWeekMoney = view.findViewById(R.id.tvWeekMoney);
        tvMonthIncome = view.findViewById(R.id.tvMonthIncome);
        tvMonthExpense = view.findViewById(R.id.tvMonthExpense);
        tvMonthMoney = view.findViewById(R.id.tvMonthMoney);
        tvQuarterIncome = view.findViewById(R.id.tvQuarterIncome);
        tvQuarterExpense = view.findViewById(R.id.tvQuarterExpense);
        tvQuarterMoney = view.findViewById(R.id.tvQuarterMoney);
        tvYearIncome = view.findViewById(R.id.tvYearIncome);
        tvYearExpense = view.findViewById(R.id.tvYearExpense);
        tvYearMoney = view.findViewById(R.id.tvYearMoney);
    }
}