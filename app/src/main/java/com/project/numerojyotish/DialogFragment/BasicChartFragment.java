package com.project.numerojyotish.DialogFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.numerojyotish.Adapter.BasicChartAdapter;
import com.project.numerojyotish.Adapter.ChartValuesAdapter;
import com.project.numerojyotish.Model.AntardashaChartValuesDTO;
import com.project.numerojyotish.Model.BasicChartDTO;
import com.project.numerojyotish.Model.ChartValuesDTO;
import com.project.numerojyotish.R;
import com.project.numerojyotish.Utils.EndPoints;
import com.project.numerojyotish.databinding.FragmentBasicChartBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicChartFragment extends DialogFragment {
    Dialog dialog;
    FragmentBasicChartBinding binding;
    ImageView backIV;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    private BasicChartAdapter adapter;
    private ArrayList<BasicChartDTO> basicChartDTOArrayList;
    private ChartValuesAdapter chartValuesAdapter;
    private ArrayList<ChartValuesDTO> chartValuesDTOArrayList;

    public BasicChartFragment() {
        // Required empty public constructor
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.fragment_basic_chart, null, false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogFragmentAnimation;
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        Toolbar toolbar = (Toolbar) dialog.findViewById(R.id.toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        backIV = toolbar.findViewById(R.id.plusimage);
        toolbar_title.setText("Chart");

        session = new SessionManager(getActivity().getApplicationContext());
        progressDialog = new ProgressDialog(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());



        if (dialog.isShowing()) {


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
            backIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
            submitData();


        }
        return dialog;
    }



    private void submitData() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
       String url= EndPoints.LOAD_CHART+"?dob="+session.getBasicDetails().get(SessionManager.KEY_DOB)+
                "&fromDate="+session.getFromToDate().get(SessionManager.KEY_FROMDATE)+
                "&toDate="+session.getFromToDate().get(SessionManager.KEY_TODATE);

       Log.e("url",url);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        //{"Id":1,"Status":"Success","Msg":"Order Placed Successfully.","OrderId":"94"}
                        try {
                            binding.scrollview.setVisibility(View.VISIBLE);
                            JSONObject obj = new JSONObject(response);

                            JSONObject basicChartObj=obj.getJSONObject("BasicChart");


                                JSONArray basicChartArray=basicChartObj.getJSONArray("basicChart");
                                JSONArray chartValuesArray=obj.getJSONArray("ChartValues");

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


                                    JSONArray anterDashaChartValues=chartValuesjsonobj.getJSONArray("anterDashaChartValues");
                                    JSONArray pratyanterDashChartModels=chartValuesjsonobj.getJSONArray("pratyanterDashChart");
                                    ChartValuesDTO chartValuesDTO=new ChartValuesDTO();
                                    chartValuesDTO.setFromdate(fromDate);
                                    chartValuesDTO.setTodate(toDate);
                                    chartValuesDTO.setPratyanterDashChartModels(pratyanterDashChartModels);
                                    ArrayList<AntardashaChartValuesDTO> antardashaChartValuesDTOArrayList=new ArrayList<>();

                                    for (int k=0;k<anterDashaChartValues.length();k++)
                                    {
                                        String values=anterDashaChartValues.getString(k);

                                        AntardashaChartValuesDTO antardashaChartValuesDTO=new AntardashaChartValuesDTO();
                                        antardashaChartValuesDTO.setAntardashaValues(values);
                                        antardashaChartValuesDTOArrayList.add(antardashaChartValuesDTO);
                                    }
                                    chartValuesDTO.setAntardashaChartValuesArrayList(antardashaChartValuesDTOArrayList);
                                    chartValuesDTOArrayList.add(chartValuesDTO);

                                }
                                chartValuesAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
        ) ;
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }
}
