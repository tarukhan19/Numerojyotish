package com.project.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.numerojyotish.Model.ChartValuesDTO;
import com.project.numerojyotish.Model.PratyantadashaChartModelsDTO;
import com.project.numerojyotish.Model.PratyantarDashaChartValuesDTO;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.ItemChartvaluesBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChartValuesAdapter extends RecyclerView.Adapter<ChartValuesAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<ChartValuesDTO> chartValuesDTOList;
    Activity activity;
    ItemChartvaluesBinding binding;
    SessionManager sessionManager;
    AntardashaChartValuesAdapter antardashaChartValuesAdapter;
    PratyantadashaChartModelsAdapter pratyantadashaChartModelsAdapter;

    public ChartValuesAdapter(Context mcontex, List<ChartValuesDTO> chartValuesDTOList) {
        this.mcontex = mcontex;
        this.chartValuesDTOList = chartValuesDTOList;
        activity = (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }


    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_chartvalues, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.fromDateTV.setText(chartValuesDTOList.get(position).getFromdate());
        holder.itemRowBinding.todateTV.setText(chartValuesDTOList.get(position).getTodate());
        antardashaChartValuesAdapter = new AntardashaChartValuesAdapter(mcontex, chartValuesDTOList.get(position).getAntardashaChartValuesArrayList());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mcontex, 3);
        holder.itemRowBinding.chartValuesrecyclerview.setLayoutManager(mLayoutManager);
        holder.itemRowBinding.chartValuesrecyclerview.scheduleLayoutAnimation();
        holder.itemRowBinding.chartValuesrecyclerview.setNestedScrollingEnabled(false);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(holder.itemRowBinding.chartValuesrecyclerview.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(mcontex, R.drawable.vertical_divider);
        verticalDecoration.setDrawable(verticalDivider);
        holder.itemRowBinding.chartValuesrecyclerview.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(holder.itemRowBinding.chartValuesrecyclerview.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(mcontex, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        holder.itemRowBinding.chartValuesrecyclerview.addItemDecoration(horizontalDecoration);
        holder.itemRowBinding.chartValuesrecyclerview.setAdapter(antardashaChartValuesAdapter);



        JSONArray pratyanterDashChartModels = chartValuesDTOList.get(position).getPratyanterDashChartModels();
        ArrayList<PratyantadashaChartModelsDTO> pratyantadashaChartModelsDTOArrayList = new ArrayList<>();
        pratyantadashaChartModelsAdapter= new PratyantadashaChartModelsAdapter(mcontex, pratyantadashaChartModelsDTOArrayList);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mcontex);
        binding.pratyantardashachartvalueRV.setLayoutManager(mLayoutManager1);
        binding.pratyantardashachartvalueRV.setAdapter(pratyantadashaChartModelsAdapter);

        for (int i = 0; i < pratyanterDashChartModels.length(); i++) {

            try {
                JSONObject chartValuesjsonobj = pratyanterDashChartModels.getJSONObject(i);
                String fromDate = chartValuesjsonobj.getString("fromDate");
                String toDate = chartValuesjsonobj.getString("toDate");

                JSONArray pratyanterDashChartValues = chartValuesjsonobj.getJSONArray("pratyanterDashaChartValues");
                PratyantadashaChartModelsDTO pratyantadashaChartModelsDTO = new PratyantadashaChartModelsDTO();
                pratyantadashaChartModelsDTO.setFromDate(fromDate);
                pratyantadashaChartModelsDTO.setToDate(toDate);
                ArrayList<PratyantarDashaChartValuesDTO> pratyantarDashaChartValuesDTOArrayList = new ArrayList<>();

                for (int k = 0; k < pratyanterDashChartValues.length(); k++) {
                    String values = pratyanterDashChartValues.getString(k);

                    PratyantarDashaChartValuesDTO pratyantarDashaChartValuesDTO = new PratyantarDashaChartValuesDTO();
                    pratyantarDashaChartValuesDTO.setPratyantardashaValues(values);
                    pratyantarDashaChartValuesDTOArrayList.add(pratyantarDashaChartValuesDTO);
                }
                pratyantadashaChartModelsDTO.setPratyantadashaChartValuesDTOArrayList(pratyantarDashaChartValuesDTOArrayList);
                pratyantadashaChartModelsDTOArrayList.add(pratyantadashaChartModelsDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        pratyantadashaChartModelsAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return chartValuesDTOList != null ? chartValuesDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemChartvaluesBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemChartvaluesBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}