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
import com.project.numerojyotish.Adapter.AntarDashaCalendarAdapter;
import com.project.numerojyotish.Api.ApiClass;
import com.project.numerojyotish.Interface.ApiInterface;
import com.project.numerojyotish.Model.AntarDashaCalendarDTO;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.FragmentAntardashaBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AntardashaFragment extends Fragment {
    static AntardashaFragment antardashaFragment;
    ApiClass apiClass;
    FragmentAntardashaBinding binding;
    SessionManager sessionManager;
    AntarDashaCalendarAdapter adapter;
    private List<AntarDashaCalendarDTO> antarDashaCalendarDTOList;

    public AntardashaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_antardasha, container, false);
        View view = binding.getRoot();
        initialize();
        return  view;
    }

    private void initialize()
    {

        sessionManager=  new SessionManager(getActivity().getApplicationContext());
        antardashaFragment = this;
        apiClass = new ApiClass();
        antarDashaCalendarDTOList=new ArrayList<>();
        adapter = new AntarDashaCalendarAdapter(getActivity(), antarDashaCalendarDTOList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        loaddata();

    }

    private void loaddata() {
        String resounse=sessionManager.getResponse().get(SessionManager.KEY_RESPONSE);
        try {
            JSONObject jsonObject=new JSONObject(resounse);
            JSONArray antardashaArray=jsonObject.getJSONArray("AnterDashaCalander");
            for (int i=0;i<antardashaArray.length();i++)
            {
                JSONObject dashaObj=antardashaArray.getJSONObject(i);
                AntarDashaCalendarDTO antarDashaCalendarDTO=new AntarDashaCalendarDTO();
                antarDashaCalendarDTO.setDasha(dashaObj.getString("dasha"));
                antarDashaCalendarDTO.setYearFrom(dashaObj.getString("yearFrom"));
                antarDashaCalendarDTO.setYearTo(dashaObj.getString("yearTo"));
                antarDashaCalendarDTO.setAntardasha(dashaObj.getString("anterDasha"));

                antarDashaCalendarDTOList.add(antarDashaCalendarDTO);

            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
