package com.example.project3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Constants;
import com.example.project3.R;
import com.example.project3.adapter.StatisticAdapter;
import com.example.project3.dao.DAOIncomesExpenses;


public class Statistic_Fragment extends Fragment {
    private View view;
    private RecyclerView rcvStatistic;
    private DAOIncomesExpenses daoIncomesExpenses;

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), Constants.GRID_COLUMN);
        rcvStatistic.setLayoutManager(gridLayoutManager);
        rcvStatistic.setAdapter(daoIncomesExpenses.getStatisticAdapter());
        return view;
    }

    private void init() {
        rcvStatistic = view.findViewById(R.id.rcvStatisticType);
        daoIncomesExpenses = new DAOIncomesExpenses();
        daoIncomesExpenses.createStatisticAdapter(getActivity());
    }
}
