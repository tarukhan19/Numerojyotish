package com.project.numerojyotish.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.numerojyotish.Adapter.AntardashaChartValuesAdapter;
import com.project.numerojyotish.Adapter.BasicChartAdapter;
import com.project.numerojyotish.Adapter.ChartValuesAdapter;
import com.project.numerojyotish.Api.ApiClass;
import com.project.numerojyotish.Model.AntardashaChartValuesDTO;
import com.project.numerojyotish.Model.BasicChartDTO;
import com.project.numerojyotish.Model.ChartValuesDTO;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.FragmentBasicInfoBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicInfoFragment extends Fragment {
    private BasicChartAdapter adapter;
    private ArrayList<BasicChartDTO> basicChartDTOArrayList;
    private ChartValuesAdapter chartValuesAdapter;
    private ArrayList<ChartValuesDTO> chartValuesDTOArrayList;

    FragmentBasicInfoBinding binding;
    SessionManager sessionManager;

    public BasicInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic_info, container, false);
        View view = binding.getRoot();

        initialize();
        return  view;
    }

    private void initialize() {

        sessionManager=  new SessionManager(getActivity().getApplicationContext());
        basicChartDTOArrayList = new ArrayList<>();
        adapter = new BasicChartAdapter(getActivity(), basicChartDTOArrayList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.scheduleLayoutAnimation();
        binding.recyclerView.setNestedScrollingEnabled(false);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.vertical_divider);
        verticalDecoration.setDrawable(verticalDivider);
        binding.recyclerView.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        binding.recyclerView.addItemDecoration(horizontalDecoration);
        binding.recyclerView.setAdapter(adapter);


        chartValuesDTOArrayList = new ArrayList<>();
        chartValuesAdapter = new ChartValuesAdapter(getActivity(), chartValuesDTOArrayList);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        binding.chartValuesrecyclerview.setLayoutManager(mLayoutManager1);
        binding.chartValuesrecyclerview.setAdapter(chartValuesAdapter);

        loaddata();

    }


    private void loaddata() {
        String resounse=sessionManager.getResponse().get(SessionManager.KEY_RESPONSE);
        try {
            JSONObject jsonObject=new JSONObject(resounse);
            JSONObject basicInfoObj=jsonObject.getJSONObject("BasicChart");

            binding.nameTV.setText(jsonObject.getString("name"));
            binding.dobTV.setText(jsonObject.getString("DOB"));

            binding.basicNoTV.setText(basicInfoObj.getString("basicNo"));
            binding.destinyTV.setText(basicInfoObj.getString("dastinyNo"));
            binding.supportiveNotv.setText(basicInfoObj.getString("supportiveNo"));
            binding.luckynobynameTV.setText(jsonObject.getString("LuckyColDirNobyDestiny"));
            binding.luckynobydestinyTV.setText(jsonObject.getString("LuckyNo"));

            JSONArray basicChartArray=basicInfoObj.getJSONArray("basicChart");
            JSONArray chartValuesArray=jsonObject.getJSONArray("ChartValues");

            for (int i=0;i<basicChartArray.length();i++)
            {
                String basicchartString=basicChartArray.getString(i);
                BasicChartDTO basicChartDTO=new BasicChartDTO();
                basicChartDTO.setBasicchartvalue(basicchartString);
                basicChartDTOArrayList.add(basicChartDTO);

            }
            adapter.notifyDataSetChanged();


            for (int j=0;j<chartValuesArray.length();j++)
            {
                JSONObject chartValuesjsonobj=chartValuesArray.getJSONObject(j);
                String fromDate=chartValuesjsonobj.getString("fromDate");
                String toDate=chartValuesjsonobj.getString("toDate");

                Log.e("fromdate",fromDate);
                Log.e("toDate",toDate);

                JSONArray anterDashaChartValues=chartValuesjsonobj.getJSONArray("anterDashaChartValues");

                ChartValuesDTO chartValuesDTO=new ChartValuesDTO();
                chartValuesDTO.setFromdate(fromDate);
                chartValuesDTO.setTodate(toDate);
                ArrayList<AntardashaChartValuesDTO> antardashaChartValuesDTOArrayList=new ArrayList<>();
                for (int k=0;k<anterDashaChartValues.length();k++)
                {
                    String values=anterDashaChartValues.getString(k);
                    Log.e("values",values);

                    AntardashaChartValuesDTO antardashaChartValuesDTO=new AntardashaChartValuesDTO();
                    antardashaChartValuesDTO.setAntardashaValues(values);
                    antardashaChartValuesDTOArrayList.add(antardashaChartValuesDTO);
                }
                chartValuesDTO.setAntardashaChartValuesArrayList(antardashaChartValuesDTOArrayList);
                chartValuesDTOArrayList.add(chartValuesDTO);

            }
            chartValuesAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
