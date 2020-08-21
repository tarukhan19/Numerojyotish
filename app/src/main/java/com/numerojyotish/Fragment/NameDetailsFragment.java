package com.numerojyotish.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.numerojyotish.R;
import com.numerojyotish.Utils.EndPoints;
import com.numerojyotish.Utils.HideKeyboard;
import com.numerojyotish.databinding.FragmentNameDetailsBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class NameDetailsFragment extends Fragment {
FragmentNameDetailsBinding binding;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    public NameDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_name_details, container, false);
        View view = binding.getRoot();
        session = new SessionManager(getActivity().getApplicationContext());
        progressDialog = new ProgressDialog(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        ImageView backIV = toolbar.findViewById(R.id.plusimage);
        backIV.setVisibility(View.GONE);
        toolbar_title.setText("Name Details");
        binding.submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=binding.nameET.getText().toString();
                HideKeyboard.hideKeyboard(getActivity());

                if (name.isEmpty())
                {
                    Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    submitData(name);

                }
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        return view;
    }

    private void submitData(String name) {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url= EndPoints.LOAD_NAMEDATA+name;

        Log.e("url",url);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("response", response);
                        //{"Id":1,"Status":"Success","Msg":"Order Placed Successfully.","OrderId":"94"}
                        try {
                            JSONObject obj = new JSONObject(response);

                            binding.detailsLL.setVisibility(View.VISIBLE);
                            binding.nameTV.setText(obj.getString("Name"));
                            binding.luckynoTV.setText(obj.getString("LuckeyNumber"));
//                            binding.nameCharValueTV.setText(obj.getString("NameCharValue"));






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
