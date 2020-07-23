package com.project.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.ItemChartvaluesBinding;
import com.project.numerojyotish.session.SessionManager;

import java.util.List;

public class ChartValuesAdapter extends RecyclerView.Adapter<ChartValuesAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<ChartValuesDTO> chartValuesDTOList;
    Activity activity;
    ItemChartvaluesBinding binding;
    SessionManager sessionManager;
    AntardashaChartValuesAdapter antardashaChartValuesAdapter;
    public ChartValuesAdapter(Context mcontex, List<ChartValuesDTO> chartValuesDTOList)
    {
        this.mcontex = mcontex;
        this.chartValuesDTOList = chartValuesDTOList;
        activity= (Activity) mcontex;
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