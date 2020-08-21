package com.numerojyotish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.numerojyotish.Model.BasicChartDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.ItemChartBinding;
import com.numerojyotish.session.SessionManager;

import java.util.List;

public class BasicChartAdapter extends RecyclerView.Adapter<BasicChartAdapter.ViewHolderPollAdapter> {
    private Context mcontex;
    private List<BasicChartDTO> basicChartDTOList;
    Activity activity;
    ItemChartBinding binding;
    SessionManager sessionManager;
    public BasicChartAdapter(Context mcontex, List<BasicChartDTO> basicChartDTOList)
    {
        this.mcontex = mcontex;
        this.basicChartDTOList = basicChartDTOList;
        activity= (Activity) mcontex;
        sessionManager = new SessionManager(mcontex.getApplicationContext());
    }
    @NonNull
    @Override
    public ViewHolderPollAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_chart, parent, false);
        return new ViewHolderPollAdapter(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPollAdapter holder, final int position) {
        holder.itemRowBinding.textview.setText(basicChartDTOList.get(position).getBasicchartvalue());

    }

    @Override
    public int getItemCount() {
        return basicChartDTOList != null ? basicChartDTOList.size() : 0;
    }

    public class ViewHolderPollAdapter extends RecyclerView.ViewHolder {

        ItemChartBinding itemRowBinding;

        public ViewHolderPollAdapter(ItemChartBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }
}