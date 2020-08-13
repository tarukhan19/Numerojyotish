package com.project.numerojyotish.Model;

import java.util.ArrayList;

public class PratyantadashaChartModelsDTO {
    private String fromDate,toDate,dashaValue,anterDashaValue,pratyanterDashaValue;
    private ArrayList<PratyantarDashaChartValuesDTO> pratyantadashaChartValuesDTOArrayList;

    public String getDashaValue() {
        return dashaValue;
    }

    public void setDashaValue(String dashaValue) {
        this.dashaValue = dashaValue;
    }

    public String getAnterDashaValue() {
        return anterDashaValue;
    }

    public void setAnterDashaValue(String anterDashaValue) {
        this.anterDashaValue = anterDashaValue;
    }

    public String getPratyanterDashaValue() {
        return pratyanterDashaValue;
    }

    public void setPratyanterDashaValue(String pratyanterDashaValue) {
        this.pratyanterDashaValue = pratyanterDashaValue;
    }


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
