package com.project.numerojyotish.Model;

import java.util.ArrayList;

public class ChartValuesDTO {
    String fromdate,todate;
    ArrayList<AntardashaChartValuesDTO> antardashaChartValuesArrayList;

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
