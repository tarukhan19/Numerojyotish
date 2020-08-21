package com.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.numerojyotish.Model.AntardashaChartValuesDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.ItemAntardashachartvaluesBinding;
import com.numerojyotish.session.SessionManager;

import java.util.List;

public class AntardashaChartValuesAdapter extends RecyclerView.Adapter<AntardashaChartValuesAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<AntardashaChartValuesDTO> antardashaChartValuesDTOList;
    Activity activity;
    ItemAntardashachartvaluesBinding binding;
    SessionManager sessionManager;
    public AntardashaChartValuesAdapter(Context mcontex, List<AntardashaChartValuesDTO> antardashaChartValuesDTOList)
    {
        this.mcontex = mcontex;
        this.antardashaChartValuesDTOList = antardashaChartValuesDTOList;
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
        holder.itemRowBinding.textview.setText(antardashaChartValuesDTOList.get(position).getAntardashaValues());

    }

    @Override
    public int getItemCount() {
        return antardashaChartValuesDTOList != null ? antardashaChartValuesDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemAntardashachartvaluesBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemAntardashachartvaluesBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}