package com.example.project3.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constant;
import com.example.project3.R;
import com.example.project3.adapter.StatisticAdapter;
import com.example.project3.dao.DAOIncomesExpenses;
import com.example.project3.dao.DAOStatisticType;
import com.example.project3.model.StatisticType;

import java.text.SimpleDateFormat;


public class Statistic_Fragment extends Fragment {
    private View view;
    private RecyclerView rcvStatistic;

    public Statistic_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statistic, container, false);
        init();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Constant.GRID_COLUMN);
        rcvStatistic.setLayoutManager(gridLayoutManager);
        StatisticAdapter adapter = new StatisticAdapter(getContext(), R.layout.item_grid);
        rcvStatistic.setAdapter(adapter);
        return view;
    }

    private void init() {
        rcvStatistic = view.findViewById(R.id.rcvStatisticType);
    }
}
