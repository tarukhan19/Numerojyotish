package com.numerojyotish.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.numerojyotish.DialogFragment.ChangePasswordFragment;
import com.numerojyotish.DialogFragment.RegistrationFragment;
import com.numerojyotish.R;
import com.numerojyotish.databinding.FragmentMoreBinding;
import com.numerojyotish.session.SessionManager;


public class MoreFragment extends Fragment {

FragmentMoreBinding binding;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        View view = binding.getRoot();
        initialize();

        binding.changepasswordCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ChangePasswordFragment dialogFragment = new ChangePasswordFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");
            }
        });

        binding.registrationCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationFragment dialogFragment = new RegistrationFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");
            }
        });

        binding.userlistCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListFragment dialogFragment = new UserListFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");
            }
        });
        return  view;
    }

    private void initialize()
    {
        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sessionManager=  new SessionManager(getActivity().getApplicationContext());

        hideItem();
    }

    public void hideItem()
    {
        if (!sessionManager.getLoginDetail().get(SessionManager.KEY_ROLE).equalsIgnoreCase("Admin"))
        {
            binding.registrationCV.setVisibility(View.GONE);
            binding.userlistCV.setVisibility(View.GONE);
        }
        else
        {
            binding.registrationCV.setVisibility(View.VISIBLE);
            binding.userlistCV.setVisibility(View.VISIBLE);
        }

    }
}