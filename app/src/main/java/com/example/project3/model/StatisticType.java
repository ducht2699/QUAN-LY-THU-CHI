package com.example.project3.model;

public class StatisticType {
    private String typeName;
    private int typeImage;
    private String statisticID;

    public StatisticType() {

    }

    public StatisticType(String typeName, int typeImage, String statisticID) {
        this.typeName = typeName;
        this.typeImage = typeImage;
        this.statisticID = statisticID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(int typeImage) {
        this.typeImage = typeImage;
    }

    public String getStatisticID() {
        return statisticID;
    }

    public void setStatisticID(String statisticID) {
        this.statisticID = statisticID;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
