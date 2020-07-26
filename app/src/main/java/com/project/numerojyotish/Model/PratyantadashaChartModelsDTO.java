package com.project.numerojyotish.Model;

import java.util.ArrayList;

public class PratyantadashaChartModelsDTO {
    private String fromDate,toDate;
    private ArrayList<PratyantarDashaChartValuesDTO> pratyantadashaChartValuesDTOArrayList;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public ArrayList<PratyantarDashaChartValuesDTO> getPratyantadashaChartValuesDTOArrayList() {
        return pratyantadashaChartValuesDTOArrayList;
    }

    public void setPratyantadashaChartValuesDTOArrayList(ArrayList<PratyantarDashaChartValuesDTO> pratyantadashaChartValuesDTOArrayList) {
        this.pratyantadashaChartValuesDTOArrayList = pratyantadashaChartValuesDTOArrayList;
    }
}
