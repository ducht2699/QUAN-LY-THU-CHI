package com.example.project3.dao;

import com.example.project3.R;
import com.example.project3.database.Database;
import com.example.project3.model.StatisticType;

import java.util.ArrayList;
import java.util.List;

public class DAOStatisticType {
    private Database database;
    private List<StatisticType> statisticTypesList;

    public DAOStatisticType() {
        this.database = new Database();
        statisticTypesList = new ArrayList<>();
        statisticTypesList.add(new StatisticType("Tài chính hiện tại", R.drawable.ic_current_finance, "eiwuflwefq"));
        statisticTypesList.add(new StatisticType("Tình hình thu chi", R.drawable.ic_ie_situation, "tbq5ybyrthwvete"));
        statisticTypesList.add(new StatisticType("Phân tích chi tiêu", R.drawable.ic_expense_analysis, "wregervwevf"));
        statisticTypesList.add(new StatisticType("Phân tích thu", R.drawable.ic_income_analysis, "ehrthtyj"));
        statisticTypesList.add(new StatisticType("Gợi ý chi tiêu", R.drawable.ic_guide, "eoiwjepifj3"));
        statisticTypesList.add(new StatisticType("Phân tích tài chính", R.drawable.ic_financial_analytics, "egwegweuih83"));
    }

    public List<StatisticType> getStatisticTypesList() {
        return statisticTypesList;
    }
}
