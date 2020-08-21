package com.numerojyotish.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.numerojyotish.Adapter.DashaCalendarAdapter;
import com.numerojyotish.Api.ApiClass;
import com.numerojyotish.Model.DashaCalendarDto;
import com.numerojyotish.R;
import com.numerojyotish.databinding.FragmentDashaBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DashaFragment extends Fragment {
    static DashaFragment dashaFragment;
    ApiClass apiClass;
    FragmentDashaBinding binding;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    DashaCalendarAdapter adapter;
    private List<DashaCalendarDto> dashaCalendarDtoList;

    public DashaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dasha, container, false);
        View view = binding.getRoot();
        initialize();
        return  view;
    }

    private void initialize() {
        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sessionManager=  new SessionManager(getActivity().getApplicationContext());
        dashaFragment = this;
        apiClass = new ApiClass();
        dashaCalendarDtoList=new ArrayList<>();
        adapter = new DashaCalendarAdapter(getActivity(), dashaCalendarDtoList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        loaddata();

    }

    private void loaddata() {
        String resounse=sessionManager.getResponse().get(SessionManager.KEY_RESPONSE);
        try {
            JSONObject jsonObject=new JSONObject(resounse);
            JSONArray dashaArray=jsonObject.getJSONArray("DashaCalander");
            for (int i=0;i<dashaArray.length();i++)
            {
                JSONObject dashaObj=dashaArray.getJSONObject(i);
                DashaCalendarDto dashaCalendarDto=new DashaCalendarDto();
                dashaCalendarDto.setDasha(dashaObj.getString("dasha"));
                dashaCalendarDto.setYearFrom(dashaObj.getString("yearFrom"));
                dashaCalendarDto.setYearTo(dashaObj.getString("yearTo"));

                dashaCalendarDtoList.add(dashaCalendarDto);

            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
