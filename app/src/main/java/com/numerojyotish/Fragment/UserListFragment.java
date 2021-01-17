package com.numerojyotish.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.numerojyotish.Activity.BasicInfoActivity;
import com.numerojyotish.R;
import com.numerojyotish.Utils.ConnectivityReceiver;
import com.numerojyotish.Utils.EndPoints;
import com.numerojyotish.Utils.HideKeyboard;
import com.numerojyotish.databinding.FragmentUserListBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;


public class UserListFragment extends DialogFragment  {

FragmentUserListBinding binding;
Dialog dialogUL;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    ImageView backIV;
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {

        dialogUL = super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(dialogUL.getContext()), R.layout.fragment_user_list, null, false);
        dialogUL.getWindow().getAttributes().windowAnimations = R.style.CustomDialogFragmentAnimation;
        dialogUL.setContentView(binding.getRoot());
        dialogUL.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogUL.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogUL.show();

        initialize();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUL.dismiss();
            }
        });





        return dialogUL;
    }

    private void initialize() {

        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sessionManager=  new SessionManager(getActivity().getApplicationContext());

        Toolbar toolbar = (Toolbar) dialogUL.findViewById(R.id.toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        backIV = toolbar.findViewById(R.id.plusimage);
        toolbar_title.setText("User List");


        loadData();

    }

    private void loadData()
    {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url= EndPoints.LOAD_USER;

        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("LOAD_USERresponse", response + "");
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);


                            BasicInfoActivity.getInstance().runThread(response);



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
        );

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }
}