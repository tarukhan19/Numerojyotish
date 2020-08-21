package com.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.numerojyotish.Model.PratyantarDashaChartValuesDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.ItemAntardashachartvaluesBinding;
import com.numerojyotish.session.SessionManager;

import java.util.List;

public class PratyantarDashaChartValuesAdapter extends RecyclerView.Adapter<PratyantarDashaChartValuesAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<PratyantarDashaChartValuesDTO> pratyantarDashaChartValuesDTOList;
    Activity activity;
    ItemAntardashachartvaluesBinding binding;
    SessionManager sessionManager;
    public PratyantarDashaChartValuesAdapter(Context mcontex, List<PratyantarDashaChartValuesDTO> pratyantarDashaChartValuesDTOList)
    {
        this.mcontex = mcontex;
        this.pratyantarDashaChartValuesDTOList = pratyantarDashaChartValuesDTOList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }



    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_antardashachartvalues, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.textview.setText(pratyantarDashaChartValuesDTOList.get(position).getPratyantardashaValues());

    }

    @Override
    public int getItemCount() {
        return pratyantarDashaChartValuesDTOList != null ? pratyantarDashaChartValuesDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemAntardashachartvaluesBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemAntardashachartvaluesBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}