package com.project.numerojyotish.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.project.numerojyotish.R;
import com.project.numerojyotish.Utils.ConnectivityReceiver;
import com.project.numerojyotish.Utils.EndPoints;
import com.project.numerojyotish.Utils.HideKeyboard;
import com.project.numerojyotish.Utils.MyApplication;
import com.project.numerojyotish.databinding.ActivityLoginBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {
ActivityLoginBinding binding;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    String mobileNo,password;
    boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initialize();

        binding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard.hideKeyboard(LoginActivity.this);

                submit();
            }
        });

    }

    private void submit()
    {
        mobileNo = binding.mobileNoET.getText().toString();
        password = binding.passwordET.getText().toString();
        if (mobileNo.isEmpty())
        {
            openDialog("Enter valid Mobile No","warning");
        }
        else if (password.isEmpty())
        {
            openDialog("Enter valid Password.","warning");
        }
        else
        {
            checkConnection();
            if (isConnected)
            {
              login();

            }
            else
            {
                showSnack(isConnected);
            }
        }

    }
    private void initialize()
    {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
            session = new SessionManager(getApplicationContext());
            progressDialog = new ProgressDialog(this);
            requestQueue = Volley.newRequestQueue(this);
    }


    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();

    }

    private void showSnack(boolean isConnected)
    {
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
                .make(findViewById(R.id.linearlayout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        this.isConnected=isConnected;
        showSnack(isConnected);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void login()
    {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url= EndPoints.LOGIN+"?userId="+mobileNo+"&password="+password;
        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            int id = obj.getInt("Code");
                            String msg = obj.getString("Status");

                            if (id == 200 && msg.equalsIgnoreCase("Success"))
                            {

                                session.setUserId();
                                Intent in7 = new Intent(LoginActivity.this, BasicInfoActivity.class);
                                in7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                startActivity(in7);
                                overridePendingTransition(R.anim.trans_left_in,
                                        R.anim.trans_left_out);
                            }

                            else
                            {
                                String alertmsg = obj.getString("Message");
                                openDialog(alertmsg,"failure");
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
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("userId", mobileNo);
//                params.put("password", password);
//                /// params.put("DeviceId", "regId");
//
//
//                return params;
//            }
//        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    private void openDialog(String message, final String imagetype)
    {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
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
        if (imagetype.equalsIgnoreCase("warning"))
        {
            imageView.setImageResource(R.drawable.warning);
            titleTV.setText("Warning!");
        }
        else if (imagetype.equalsIgnoreCase("success"))
        {
            imageView.setImageResource(R.drawable.success);
            titleTV.setText("Success!");
        }
        else if (imagetype.equalsIgnoreCase("failure"))
        {
            imageView.setImageResource(R.drawable.sorry);
            titleTV.setText("Failure!");
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (imagetype.equalsIgnoreCase("warning")  || imagetype.equalsIgnoreCase("failure"))
                {
                    dialog.dismiss();
                }

                else
                {
                    dialog.dismiss();
                    Intent in7 = new Intent(LoginActivity.this, BasicInfoActivity.class);
                    in7.putExtra("from","login");

                    in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(in7);
                    overridePendingTransition(R.anim.trans_left_in,
                            R.anim.trans_left_out);
                }


            }
        });
    }

}
