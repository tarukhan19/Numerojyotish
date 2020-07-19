package com.project.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.numerojyotish.Model.DashaCalendarDto;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.ItemDashacalendarBinding;
import com.project.numerojyotish.session.SessionManager;

import java.util.List;

public class DashaCalendarAdapter extends RecyclerView.Adapter<DashaCalendarAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<DashaCalendarDto> dashaCalendarDtoList;
    Activity activity;
    ItemDashacalendarBinding binding;
    SessionManager sessionManager;
    public DashaCalendarAdapter(Context mcontex, List<DashaCalendarDto> dashaCalendarDtoList)
    {
        this.mcontex = mcontex;
        this.dashaCalendarDtoList = dashaCalendarDtoList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }
    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_dashacalendar, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.fromdateTV.setText(dashaCalendarDtoList.get(position).getYearFrom());
        holder.itemRowBinding.todateTV.setText(dashaCalendarDtoList.get(position).getYearTo());
        holder.itemRowBinding.dashaTV.setText(dashaCalendarDtoList.get(position).getDasha());


    }

    @Override
    public int getItemCount() {
        return dashaCalendarDtoList != null ? dashaCalendarDtoList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemDashacalendarBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemDashacalendarBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}