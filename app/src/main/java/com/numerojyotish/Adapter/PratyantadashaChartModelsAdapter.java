package com.numerojyotish.Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.numerojyotish.Model.PratyantadashaChartModelsDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.ItemPratyantarchartmodelsBinding;
import com.numerojyotish.session.SessionManager;

import java.util.List;

public class PratyantadashaChartModelsAdapter extends RecyclerView.Adapter<PratyantadashaChartModelsAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<PratyantadashaChartModelsDTO> pratyantadashaChartModelsDTOList;
    Activity activity;
    ItemPratyantarchartmodelsBinding binding;
    SessionManager sessionManager;
    PratyantarDashaChartValuesAdapter pratyantarDashaChartValuesAdapter;
    public PratyantadashaChartModelsAdapter(Context mcontex, List<PratyantadashaChartModelsDTO> pratyantadashaChartModelsDTOList)
    {
        this.mcontex = mcontex;
        this.pratyantadashaChartModelsDTOList = pratyantadashaChartModelsDTOList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }


    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_pratyantarchartmodels, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.fromDateTV.setText("From Date: "+pratyantadashaChartModelsDTOList.get(position).getFromDate());
        holder.itemRowBinding.toDateTV.setText("To Date: "+pratyantadashaChartModelsDTOList.get(position).getToDate());
        holder.itemRowBinding.dashaTV.setText(pratyantadashaChartModelsDTOList.get(position).getDashaValue());
        holder.itemRowBinding.antardashaTV.setText(pratyantadashaChartModelsDTOList.get(position).getAnterDashaValue());
        holder.itemRowBinding.pratyantardashaTV.setText(pratyantadashaChartModelsDTOList.get(position).getPratyanterDashaValue());

        pratyantarDashaChartValuesAdapter = new PratyantarDashaChartValuesAdapter(mcontex, pratyantadashaChartModelsDTOList.get(position).getPratyantadashaChartValuesDTOArrayList());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mcontex, 3);
        holder.itemRowBinding.pratyantardashachartvalueRV.setLayoutManager(mLayoutManager);
        holder.itemRowBinding.pratyantardashachartvalueRV.scheduleLayoutAnimation();
        holder.itemRowBinding.pratyantardashachartvalueRV.setNestedScrollingEnabled(false);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(holder.itemRowBinding.pratyantardashachartvalueRV.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(mcontex, R.drawable.vertical_divider);
        verticalDecoration.setDrawable(verticalDivider);
        holder.itemRowBinding.pratyantardashachartvalueRV.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(holder.itemRowBinding.pratyantardashachartvalueRV.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(mcontex, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        holder.itemRowBinding.pratyantardashachartvalueRV.addItemDecoration(horizontalDecoration);
        holder.itemRowBinding.pratyantardashachartvalueRV.setAdapter(pratyantarDashaChartValuesAdapter);

    }

    @Override
    public int getItemCount() {
        return pratyantadashaChartModelsDTOList != null ? pratyantadashaChartModelsDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemPratyantarchartmodelsBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemPratyantarchartmodelsBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}