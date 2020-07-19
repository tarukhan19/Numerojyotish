package com.project.numerojyotish.Fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.project.numerojyotish.Adapter.DashaCalendarAdapter;
import com.project.numerojyotish.Api.ApiClass;
import com.project.numerojyotish.Model.DashaCalendarDto;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.FragmentDashaBinding;
import com.project.numerojyotish.session.SessionManager;

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
        apiClass.getHomeData(getActivity());
        dashaCalendarDtoList=new ArrayList<>();
        adapter = new DashaCalendarAdapter(getActivity(), dashaCalendarDtoList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);

    }

    public static DashaFragment getInstance() {
        return dashaFragment;
    }
    public void runThread(final String response) {

        new Thread() {
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run()
                        {
                        Log.e("DashaFragmentresponse",response);
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

}
