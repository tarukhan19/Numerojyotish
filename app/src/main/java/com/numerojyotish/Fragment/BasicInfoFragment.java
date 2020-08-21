package com.numerojyotish.Fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.numerojyotish.R;
import com.numerojyotish.databinding.FragmentBasicInfoBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

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
            binding.birthDateTV.setText(jsonObject.getString("DOB"));
            binding.genderTV.setText(jsonObject.getString("Gender"));

            binding.basicNoTv.setText(jsonObject.getString("BasicNo"));
            binding.destinyNoTV.setText(jsonObject.getString("DestinyNo"));
            binding.supportivenoTV.setText(jsonObject.getString("SupportiveNo"));
            binding.luckynoTV.setText(jsonObject.getString("LuckyNo"));
            binding.luckyColorTV.setText(jsonObject.getString("LuckyColor"));
            binding.luckyDirecTV.setText(jsonObject.getString("LuckyDirection"));
            binding.zodiacSignTV.setText(jsonObject.getString("ZodianSign"));

            String remark=jsonObject.getString("Remark");
            remark = remark.replace("\\n", "");
            remark = remark.replace("\\t", "").trim();

            binding.remarkTV.setText(remark);




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
