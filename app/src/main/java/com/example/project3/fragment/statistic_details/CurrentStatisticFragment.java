package com.example.project3.fragment.statistic_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.project3.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CurrentStatisticFragment extends Fragment {
    private View view;
    private TextView tvCurrentMoney, tvCurrentIncome, tvCurrentExpense;

    public CurrentStatisticFragment() {
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
        view = inflater.inflate(R.layout.fragment_current_statistic, container, false);
        init();
        final Bundle bundle = getArguments();
        if (bundle != null) {
            tvCurrentIncome.setText(moneyStandardize(bundle.getString("currentIncomes", "0")));
            tvCurrentExpense.setText(moneyStandardize(bundle.getString("currentExpense", "0")));
            tvCurrentMoney.setText(moneyStandardize(bundle.getString("currentMoney", "0")));
        }
        tvCurrentMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), bundle.getString("currentMoney", "0"), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private String moneyStandardize(String money) {
        int mn = Integer.valueOf(money);
        NumberFormat fm = new DecimalFormat("#,###");
        return fm.format(mn).toString() +" VND";
    }

    private void init() {
        tvCurrentMoney = view.findViewById(R.id.tvCurrentMoney);
        tvCurrentIncome = view.findViewById(R.id.tvCurrentIncome);
        tvCurrentExpense = view.findViewById(R.id.tvCurrentExpense);
    }
}