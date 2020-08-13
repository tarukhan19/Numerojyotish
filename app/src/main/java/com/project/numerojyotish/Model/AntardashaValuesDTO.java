package com.project.numerojyotish.Model;

import org.json.JSONArray;

import java.util.ArrayList;

public class AntardashaValuesDTO {
    String fromdate,todate,dashaValue,anterDashaValue;
    ArrayList<AntardashaChartValuesDTO> antardashaChartValuesArrayList;
    JSONArray pratyanterDashChartModels;

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

    public JSONArray getPratyanterDashChartModels() {
        return pratyanterDashChartModels;
    }

    public void setPratyanterDashChartModels(JSONArray pratyanterDashChartModels) {
        this.pratyanterDashChartModels = pratyanterDashChartModels;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public ArrayList<AntardashaChartValuesDTO> getAntardashaChartValuesArrayList() {
        return antardashaChartValuesArrayList;
    }

    public void setAntardashaChartValuesArrayList(ArrayList<AntardashaChartValuesDTO> antardashaChartValuesArrayList) {
        this.antardashaChartValuesArrayList = antardashaChartValuesArrayList;
    }
}
