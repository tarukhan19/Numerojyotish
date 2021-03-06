package com.numerojyotish.DialogFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.android.material.snackbar.Snackbar;
import com.numerojyotish.Activity.LoginActivity;
import com.numerojyotish.R;
import com.numerojyotish.Utils.ConnectivityReceiver;
import com.numerojyotish.Utils.EndPoints;
import com.numerojyotish.Utils.HideKeyboard;
import com.numerojyotish.Utils.MyApplication;
import com.numerojyotish.databinding.FragmentChangePasswordBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends DialogFragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    FragmentChangePasswordBinding binding;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    boolean isConnected;
    private int mYear, mMonth, mDay;
    String oldpassword, password, confpassword;
    Dialog cpdialog;

    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        cpdialog = super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(cpdialog.getContext()), R.layout.fragment_change_password, null, false);
        cpdialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogFragmentAnimation;
        cpdialog.setContentView(binding.getRoot());
        cpdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cpdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cpdialog.show();
        Toolbar toolbar = (Toolbar) cpdialog.findViewById(R.id.toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        ImageView backIV = toolbar.findViewById(R.id.plusimage);
        toolbar_title.setText("Change Password");

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpdialog.dismiss();
            }
        });

        initialize();
        binding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard.hideKeyboard(getActivity());

                submit();
            }
        });
    return cpdialog;
    }

    private void submit() {
        oldpassword = binding.oldpasswordET.getText().toString();

        password = binding.newpasswordET.getText().toString();
        confpassword = binding.confNewpasswordET.getText().toString();
        if (oldpassword.isEmpty()) {
            openDialog("Enter valid old Password.", "warning");
        }
        else if (password.isEmpty()) {
            openDialog("Enter valid new Password.", "warning");
        } else if (!confpassword.equals(password)) {
            openDialog("Password doesn't match", "warning");
        } else {
            checkConnection();
            if (isConnected) {
                changePassword();

            } else {
                showSnack(isConnected);
            }
        }

    }

    private void initialize() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        }
        session = new SessionManager(getActivity().getApplicationContext());
        progressDialog = new ProgressDialog(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);



    }
    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();

    }


    @Override
    public void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.linearlayout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
        showSnack(isConnected);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void changePassword()
    {
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String url = EndPoints.CHANGE_PASSWORD + "?userId=" + session.getLoginDetail().get(SessionManager.KEY_MOBILE_NO) +
                "&oldPassword=" + oldpassword+ "&newPassword=" + password+ "&imeiNo=" + session.getLoginDetail().get(SessionManager.KEY_IMEI_NO);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("response",response);
                            int id = obj.getInt("Code");
                            String msg = obj.getString("Status");

                            if (id == 200 ) {

                                String alertmsg = obj.getString("Message");
                                session.logoutUser();
                                openDialog(alertmsg, "SUCCESS");

                            } else {
                                String alertmsg = obj.getString("Message");
                                openDialog(alertmsg, "FAIL");
                            }


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

    private void openDialog(String message, final String imagetype) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialogbox);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        TextView descriptionTV = dialog.findViewById(R.id.descriptionTV);
        TextView titleTV = dialog.findViewById(R.id.titleTV);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        Button okBtn = dialog.findViewById(R.id.okBtn);

        descriptionTV.setText(message);
        if (imagetype.equalsIgnoreCase("warning")) {
            imageView.setImageResource(R.drawable.warning);
            titleTV.setText("Warning!");
        } else if (imagetype.equalsIgnoreCase("SUCCESS")) {
            imageView.setImageResource(R.drawable.success);
            titleTV.setText("Success!");
        } else if (imagetype.equalsIgnoreCase("FAIL")) {
            imageView.setImageResource(R.drawable.sorry);
            titleTV.setText("Failure!");
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagetype.equalsIgnoreCase("warning") || imagetype.equalsIgnoreCase("FAIL")) {
                    dialog.dismiss();
                    cpdialog.dismiss();
                } else {
                    dialog.dismiss();

                    binding.oldpasswordET.setText("");
                    binding.newpasswordET.setText("");
                    binding.confNewpasswordET.setText("");

                    Intent in7 = new Intent(getActivity(), LoginActivity.class);
                    in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in7);
                    cpdialog.dismiss();
                    getActivity().overridePendingTransition(R.anim.trans_left_in,
                            R.anim.trans_left_out);

                }


            }
        });
    }

}
