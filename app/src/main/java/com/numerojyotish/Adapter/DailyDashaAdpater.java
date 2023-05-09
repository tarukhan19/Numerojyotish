package com.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.numerojyotish.Model.DailyDashaDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.ItemDailydashacalendarBinding;
import com.numerojyotish.session.SessionManager;

import java.util.List;

public class DailyDashaAdpater extends RecyclerView.Adapter<DailyDashaAdpater.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<DailyDashaDTO> dailyDashaDTOList;
    Activity activity;
    ItemDailydashacalendarBinding binding;
    SessionManager sessionManager;
    public DailyDashaAdpater(Context mcontex, List<DailyDashaDTO> dailyDashaDTOList)
    {
        this.mcontex = mcontex;
        this.dailyDashaDTOList = dailyDashaDTOList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }



    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_dailydashacalendar, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.dateTV.setText(dailyDashaDTOList.get(position).getDayDate());
        holder.itemRowBinding.dayTV.setText(dailyDashaDTOList.get(position).getDayName());
        holder.itemRowBinding.pratayantardashaTV.setText(dailyDashaDTOList.get(position).getPartyantarDashaValue());
        holder.itemRowBinding.dailydashaTV.setText(dailyDashaDTOList.get(position).getDayValue());
    }

    @Override
    public int getItemCount() {
        return dailyDashaDTOList != null ? dailyDashaDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemDailydashacalendarBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemDailydashacalendarBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }
    }
}