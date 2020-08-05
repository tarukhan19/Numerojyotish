package com.project.numerojyotish.Model;

import org.json.JSONArray;

import java.util.ArrayList;

public class AntardashaValuesDTO {
    String fromdate,todate;
    ArrayList<AntardashaChartValuesDTO> antardashaChartValuesArrayList;
    JSONArray pratyanterDashChartModels;

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
