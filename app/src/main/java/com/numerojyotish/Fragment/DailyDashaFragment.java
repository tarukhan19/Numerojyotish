package com.numerojyotish.Fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.numerojyotish.Adapter.DailyDashaAdpater;
import com.numerojyotish.Model.DailyDashaDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.FragmentDailyDashaBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DailyDashaFragment extends Fragment {
    FragmentDailyDashaBinding binding;
    private List<DailyDashaDTO> dailyDashaDTOList;
    DailyDashaAdpater adapter;
    SessionManager sessionManager;

    public DailyDashaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily_dasha, container, false);
        View view = binding.getRoot();
        initialize();
        return  view;
    }

    private void initialize() {

        sessionManager=  new SessionManager(getActivity().getApplicationContext());

        dailyDashaDTOList=new ArrayList<>();
        adapter = new DailyDashaAdpater(getActivity(), dailyDashaDTOList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        loaddata();

    }

    private void loaddata() {
        String resounse=sessionManager.getResponse().get(SessionManager.KEY_RESPONSE);
        Log.e("resounse",resounse);
        try {
            JSONObject jsonObject=new JSONObject(resounse);
            JSONArray dashaArray=jsonObject.getJSONArray("DailyDasha");
            for (int i=0;i<dashaArray.length();i++)
            {
                JSONObject dashaObj=dashaArray.getJSONObject(i);
                DailyDashaDTO dailyDashaDTO=new DailyDashaDTO();
                dailyDashaDTO.setDayName(dashaObj.getString("DayName"));
                dailyDashaDTO.setPartyantarDashaValue(dashaObj.getString("partyantarDashaValue"));
                dailyDashaDTO.setDayDate(dashaObj.getString("DayDate"));
                dailyDashaDTO.setDayValue(dashaObj.getString("DayValue"));
                dailyDashaDTOList.add(dailyDashaDTO);

            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}