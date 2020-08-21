package com.numerojyotish.Fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.numerojyotish.Adapter.PartyantarDashaAdpater;
import com.numerojyotish.Model.PartyantarDashaDTO;
import com.numerojyotish.R;
import com.numerojyotish.databinding.FragmentPartyantarDashaBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartyantarDashaFragment extends Fragment {
FragmentPartyantarDashaBinding binding;
    private List<PartyantarDashaDTO> partyantarDashaDTOList;
    PartyantarDashaAdpater adapter;
    SessionManager sessionManager;

    public PartyantarDashaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_partyantar_dasha, container, false);
        View view = binding.getRoot();
        initialize();
        return  view;
    }

    private void initialize() {

        sessionManager=  new SessionManager(getActivity().getApplicationContext());

        partyantarDashaDTOList=new ArrayList<>();
        adapter = new PartyantarDashaAdpater(getActivity(), partyantarDashaDTOList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        loaddata();

    }

    private void loaddata() {
        String resounse=sessionManager.getResponse().get(SessionManager.KEY_RESPONSE);
        try {
            JSONObject jsonObject=new JSONObject(resounse);
            JSONArray dashaArray=jsonObject.getJSONArray("PartyantarDashaCalander");
            for (int i=0;i<dashaArray.length();i++)
            {
                JSONObject dashaObj=dashaArray.getJSONObject(i);
                PartyantarDashaDTO partyantarDashaDTO=new PartyantarDashaDTO();
                partyantarDashaDTO.setDasha(dashaObj.getString("dasha"));
                partyantarDashaDTO.setYearFrom(dashaObj.getString("yearFrom"));
                partyantarDashaDTO.setYearTo(dashaObj.getString("yearTo"));
                partyantarDashaDTO.setAntardasha(dashaObj.getString("anterDasha"));
                partyantarDashaDTO.setPartyantardasha(dashaObj.getString("partyantarDasha"));

                partyantarDashaDTOList.add(partyantarDashaDTO);

            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
