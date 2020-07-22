package com.project.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.numerojyotish.Model.PartyantarDashaDTO;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.ItemPatyantarcalendarBinding;
import com.project.numerojyotish.session.SessionManager;

import java.util.List;

public class PartyantarDashaAdpater extends RecyclerView.Adapter<PartyantarDashaAdpater.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<PartyantarDashaDTO> partyantarDashaDTOList;
    Activity activity;
    ItemPatyantarcalendarBinding binding;
    SessionManager sessionManager;
    public PartyantarDashaAdpater(Context mcontex, List<PartyantarDashaDTO> partyantarDashaDTOList)
    {
        this.mcontex = mcontex;
        this.partyantarDashaDTOList = partyantarDashaDTOList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }



    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_patyantarcalendar, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.fromdateTV.setText(partyantarDashaDTOList.get(position).getYearFrom());
        holder.itemRowBinding.todateTV.setText(partyantarDashaDTOList.get(position).getYearTo());
        holder.itemRowBinding.dashaTV.setText(partyantarDashaDTOList.get(position).getDasha());
        holder.itemRowBinding.antardashaTV.setText(partyantarDashaDTOList.get(position).getAntardasha());
        holder.itemRowBinding.patyantardashaTV.setText(partyantarDashaDTOList.get(position).getPartyantardasha());


    }

    @Override
    public int getItemCount() {
        return partyantarDashaDTOList != null ? partyantarDashaDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemPatyantarcalendarBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemPatyantarcalendarBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}