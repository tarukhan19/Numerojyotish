package com.numerojyotish.DialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.numerojyotish.Adapter.AntarDashaCalendarAdapter;
import com.numerojyotish.Adapter.UserListAdapter;
import com.numerojyotish.Interface.UserListInterface;
import com.numerojyotish.Model.UserListDTO;
import com.numerojyotish.R;
import com.numerojyotish.Utils.ConnectivityReceiver;
import com.numerojyotish.Utils.EndPoints;
import com.numerojyotish.Utils.HideKeyboard;
import com.numerojyotish.databinding.FragmentUserListBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserListFragment extends DialogFragment implements UserListInterface{

    FragmentUserListBinding binding;
    Dialog dialogUL;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    ImageView backIV;
    ArrayList<UserListDTO> userListDTOArrayList;
    UserListAdapter adapter;
    EditText searchET;
    UserListInterface listener;

    public Dialog onCreateDialog(final Bundle savedInstanceState) {

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


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    listener.filterProduct(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return dialogUL;
    }



    public void setListener(UserListInterface listener) {
        this.listener = listener;
    }


    private void initialize() {

        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sessionManager = new SessionManager(getActivity().getApplicationContext());

        Toolbar toolbar = (Toolbar) dialogUL.findViewById(R.id.toolbar);
        backIV = toolbar.findViewById(R.id.plusimage);
        searchET= toolbar.findViewById(R.id.searchET);

        userListDTOArrayList = new ArrayList<>();
        adapter = new UserListAdapter(getActivity(), userListDTOArrayList,listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);
        setListener(this);

        loadData();

    }


    @Override
    public void onUserSelected(UserListDTO detailDTO) {

    }

    @Override
    public void filterProduct(String query) {

        adapter.getFilter().filter(query);
    }
    private void loadData() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = EndPoints.LOAD_USER;
        Log.e("url", url);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("LOAD_USERresponse", response + "");
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray registeredUser = obj.getJSONArray("registerUsers");
                            for (int i = 0; i < registeredUser.length(); i++) {
                                JSONObject jsonObject = registeredUser.getJSONObject(i);
                                String firstName = jsonObject.getString("firstName");
                                String lastName = jsonObject.getString("lastName");
                                String gender = jsonObject.getString("gender");
                                String dob = jsonObject.getString("dob");
                                String memberExpirationDate = jsonObject.getString("memberExpirationDate");
                                String mobileNo = jsonObject.getString("mobileNo");
                                String email = jsonObject.getString("email");

                                UserListDTO userListDTO = new UserListDTO();
                                userListDTO.setFirstname(firstName);
                                userListDTO.setLastname(lastName);
                                userListDTO.setGender(gender);
                                userListDTO.setDateofbirth(dob);
                                userListDTO.setExpirydate(memberExpirationDate);
                                userListDTO.setMobileno(mobileNo);
                                userListDTO.setEmailid(email);
                                userListDTOArrayList.add(userListDTO);


                            }
                            adapter.notifyDataSetChanged();


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