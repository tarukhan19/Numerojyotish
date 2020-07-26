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


        loaddata();

    }


    private void loaddata() {
        String resounse=sessionManager.getResponse().get(SessionManager.KEY_RESPONSE);
        try {
            JSONObject jsonObject=new JSONObject(resounse);
        //    JSONObject basicInfoObj=jsonObject.getJSONObject("BasicChart");

            binding.nameTV.setText(jsonObject.getString("name"));
            binding.dobTV.setText(jsonObject.getString("DOB"));
            binding.genderTV.setText(jsonObject.getString("Gender"));

            binding.basicNoTV.setText(jsonObject.getString("BasicNo"));
            binding.destinyTV.setText(jsonObject.getString("DestinyNo"));
            binding.supportiveNotv.setText(jsonObject.getString("SupportiveNo"));
            binding.luckynoTV.setText(jsonObject.getString("LuckyNo"));
            binding.luckycolorTV.setText(jsonObject.getString("LuckyColor"));
            binding.luckydirectionTV.setText(jsonObject.getString("LuckyDirection"));
            binding.zodiacsignTV.setText(jsonObject.getString("ZodianSign"));




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
