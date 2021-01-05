package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.dao.DAOStatisticType;
import com.example.project3.fragment.statistic_details.CurrentStatisticFragment;
import com.example.project3.fragment.statistic_details.DeptLoanFragment;
import com.example.project3.fragment.statistic_details.ExpenseAnalysisFragment;
import com.example.project3.fragment.statistic_details.FinancialAnalyticFragment;
import com.example.project3.fragment.statistic_details.IEEventFragment;
import com.example.project3.fragment.statistic_details.IEObjectFragment;
import com.example.project3.fragment.statistic_details.IESituationFragment;
import com.example.project3.fragment.statistic_details.IncomeAnalysisFragment;
import com.example.project3.model.IncomesExpenses;
import com.example.project3.model.StatisticType;

import java.util.ArrayList;
import java.util.List;

public class StatisticDetails extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner spinner;
    private List<StatisticType> statisticDetailsList;
    private DAOStatisticType daoStatisticType;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_details);
        init();
        ArrayAdapter<StatisticType> adapter = new ArrayAdapter<StatisticType>(this, R.layout.statistic_spinner, statisticDetailsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String id = getIntent().getExtras().getString("statisticID");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final StatisticType statisticType = statisticDetailsList.get(position);
                selectFragment(statisticType.getTypeImage());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setSpinnerSelected(id);
    }

    private void selectFragment(int typeImage) {
        switch (typeImage) {
            case R.drawable.ic_current_finance:
                CurrentStatisticFragment fragment = new CurrentStatisticFragment();
                Bundle bundle = new Bundle();
                bundle.putString("currentIncomes", String.valueOf(getIntent().getExtras().getInt("currentIncomes", 0)));
                bundle.putString("currentExpense", String.valueOf(getIntent().getExtras().getInt("currentExpense", 0)));
                bundle.putString("currentMoney", String.valueOf(getIntent().getExtras().getLong("currentMoney", 0)));
                fragment.setArguments(bundle);
                replaceFragment(fragment);
                break;
            case R.drawable.ic_ie_situation:
                IESituationFragment ieSituationFragment = new IESituationFragment();
                replaceFragment(ieSituationFragment);
                break;
            case R.drawable.ic_expense_analysis:
                ExpenseAnalysisFragment fragment1 = new ExpenseAnalysisFragment();
                replaceFragment(fragment1);
                break;
            case R.drawable.ic_income_analysis:
                IncomeAnalysisFragment fragment2 = new IncomeAnalysisFragment();
                replaceFragment(fragment2);
                break;
            case R.drawable.ic_dept_loan:
                DeptLoanFragment deptLoanFragment = new DeptLoanFragment();
                replaceFragment(deptLoanFragment);
                break;
            case R.drawable.ic_ie_object:
                IEObjectFragment ieObjectFragment = new IEObjectFragment();
                replaceFragment(ieObjectFragment);
                break;
            case R.drawable.ic_event:
                IEEventFragment ieEventFragment = new IEEventFragment();
                replaceFragment(ieEventFragment);
                break;
            case R.drawable.ic_financial_analytics:
                FinancialAnalyticFragment financialAnalyticFragment = new FinancialAnalyticFragment();
                replaceFragment(financialAnalyticFragment);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.statisticFrame, fragment).commit();
    }

    private void setSpinnerSelected(String id) {
        for (StatisticType x: statisticDetailsList) {
            if  (x.getStatisticID().matches(id)) {
                spinner.setSelection(statisticDetailsList.indexOf(x));
                selectFragment(x.getTypeImage());
            }
        }
    }

    private void init() {
        frameLayout = findViewById(R.id.statisticFrame);
        toolbar = findViewById(R.id.statisticToolbar);
        spinner = findViewById(R.id.statisticSpinner);
        daoStatisticType = new DAOStatisticType();
        statisticDetailsList = daoStatisticType.getStatisticTypesList();
    }
}