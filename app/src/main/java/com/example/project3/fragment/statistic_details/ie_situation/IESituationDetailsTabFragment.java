package com.example.project3.fragment.statistic_details.ie_situation;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constants;
import com.example.project3.R;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.model.IECalculation;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class IESituationDetailsTabFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tvFromTime, tvToTime;
    private RelativeLayout rlFrom, rlTo;
    private BarChart barChart;
    private RecyclerView rcvStatisticList;
    private DAOIncomesExpenses daoIncomesExpenses;
    private DatePickerDialog datePickerDialog;
    private int timeType;
    private boolean fromSet = false, toSet = false;

    public IESituationDetailsTabFragment(int timeType) {
        this.timeType = timeType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_i_e_situation_details_tab, container, false);
        init();

        rlFrom.setOnClickListener(this);
        rlTo.setOnClickListener(this);
        return view;
    }

    private void init() {
        daoIncomesExpenses = new DAOIncomesExpenses();
        daoIncomesExpenses.createIECalculateAdapter(getActivity());
        tvFromTime = view.findViewById(R.id.tvFromTime);
        tvToTime = view.findViewById(R.id.tvToTime);
        rlFrom = view.findViewById(R.id.rlFromYearChooser);
        rlTo = view.findViewById(R.id.rlToYearChooser);
        barChart = view.findViewById(R.id.barChart);
        rcvStatisticList = view.findViewById(R.id.rcvStatisticList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvStatisticList.setLayoutManager(layoutManager);
        rcvStatisticList.setAdapter(daoIncomesExpenses.getIeCalculateAdapter());

        barChart.setVisibility(View.GONE);
        rcvStatisticList.setVisibility(View.GONE);
    }

    private String[] getXAxisLabel() {
        String[] list = new String[daoIncomesExpenses.getIeCalculationList().size()];
        for (int i = 0; i < daoIncomesExpenses.getIeCalculationList().size(); i++) {
            list[i] = String.valueOf(daoIncomesExpenses.getIeCalculationList().get(i).getTime());
        }
        return list;
    }

    private ArrayList<BarEntry> getChartData(int IEType) {
        ArrayList<BarEntry> list = new ArrayList<>();
        for (IECalculation x : daoIncomesExpenses.getIeCalculationList()) {
            if (IEType == Constants.INCOME)
                list.add(new BarEntry(x.getTime(), x.getIncomes()));
            if (IEType == Constants.EXPENSES)
                list.add(new BarEntry(x.getTime(), x.getExpense()));
        }
        return list;
    }

    @Override
    public void onClick(final View v) {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        final int y = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                switch (v.getId()) {
                    case R.id.rlFromYearChooser:
                        tvFromTime.setText(String.valueOf(year));
                        fromSet = true;
                        break;
                    case R.id.rlToYearChooser:
                        tvToTime.setText(String.valueOf(year));
                        toSet = true;
                        break;
                }
                if (fromSet && toSet && Integer.valueOf(tvFromTime.getText().toString()) <= Integer.valueOf(tvToTime.getText().toString())) {
                    barChart.setVisibility(View.VISIBLE);
                    rcvStatisticList.setVisibility(View.VISIBLE);
                    daoIncomesExpenses.addIECalculateListByTimeType(timeType, Integer.valueOf(tvFromTime.getText().toString()), Integer.valueOf(tvToTime.getText().toString()));
                    //get income data set
                    BarDataSet barDataSet = new BarDataSet(getChartData(Constants.INCOME), "Tiền thu");
                    barDataSet.setColors(Color.GREEN);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(10f);

                    //get expense data set
                    BarDataSet barDataSet1 = new BarDataSet(getChartData(Constants.EXPENSES), "Tiền chi");
                    barDataSet1.setColors(Color.RED);
                    barDataSet1.setValueTextColor(Color.BLACK);
                    barDataSet1.setValueTextSize(10f);
                    BarData barData = new BarData(barDataSet, barDataSet1);

                    float barSpace = 0.02f;
                    float groupSpace = 0.3f;
                    int groupCount = daoIncomesExpenses.getIeCalculationList().size();

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabel()));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setGranularity(1);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setGranularityEnabled(true);
                    barChart.getAxisLeft().setAxisMinimum(0);
                    barChart.getAxisRight().setAxisMinimum(0);
                    Description des = new Description();
                    des.setText("");
                    barChart.setDescription(des);
                    barChart.animateY(1000);
                    barChart.setData(barData);
                    barData.setBarWidth(0.33f);
                    xAxis.setAxisMinimum(0);
                    xAxis.setAxisMaximum(barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                    barChart.groupBars(0, groupSpace, barSpace);
                    barChart.invalidate();

                } else {
                    if (fromSet && toSet && Integer.valueOf(tvFromTime.getText().toString()) > Integer.valueOf(tvToTime.getText().toString()))
                        Toast.makeText(getActivity(), "Chọn sai, Mời nhập lại thời gian!", Toast.LENGTH_SHORT).show();
                    barChart.setVisibility(View.GONE);
                    rcvStatisticList.setVisibility(View.GONE);
                }
            }
        }, y, m, d);
        datePickerDialog.show();
    }
}