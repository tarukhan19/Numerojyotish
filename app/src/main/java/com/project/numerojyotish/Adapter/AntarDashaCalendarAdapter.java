package com.project.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.numerojyotish.Fragment.AntardashaFragment;
import com.project.numerojyotish.Model.AntarDashaCalendarDTO;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.ItemAntardashacalendarBinding;
import com.project.numerojyotish.session.SessionManager;

import java.util.List;

public class AntarDashaCalendarAdapter extends RecyclerView.Adapter<AntarDashaCalendarAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<AntarDashaCalendarDTO> antarDashaCalendarDTOList;
    Activity activity;
    ItemAntardashacalendarBinding binding;
    SessionManager sessionManager;
    public AntarDashaCalendarAdapter(Context mcontex, List<AntarDashaCalendarDTO> antarDashaCalendarDTOList)
    {
        this.mcontex = mcontex;
        this.antarDashaCalendarDTOList = antarDashaCalendarDTOList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }



    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_antardashacalendar, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.fromdateTV.setText(antarDashaCalendarDTOList.get(position).getYearFrom());
        holder.itemRowBinding.todateTV.setText(antarDashaCalendarDTOList.get(position).getYearTo());
        holder.itemRowBinding.dashaTV.setText(antarDashaCalendarDTOList.get(position).getDasha());
        holder.itemRowBinding.antardashaTV.setText(antarDashaCalendarDTOList.get(position).getAntardasha());


    }

    @Override
    public int getItemCount() {
        return antarDashaCalendarDTOList != null ? antarDashaCalendarDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemAntardashacalendarBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemAntardashacalendarBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}