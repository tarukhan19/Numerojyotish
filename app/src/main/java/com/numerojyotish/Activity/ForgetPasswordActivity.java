package com.numerojyotish.Activity;

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
import com.numerojyotish.R;
import com.numerojyotish.Utils.ConnectivityReceiver;
import com.numerojyotish.Utils.EndPoints;
import com.numerojyotish.Utils.HideKeyboard;
import com.numerojyotish.Utils.MyApplication;
import com.numerojyotish.databinding.ActivityForgetPasswordBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class ForgetPasswordActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
ActivityForgetPasswordBinding binding;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    String mobileNo;
    boolean isConnected;
    String imeino;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        initialize();

        binding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard.hideKeyboard(ForgetPasswordActivity.this);

                submit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in7 = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in7);
        overridePendingTransition(R.anim.trans_left_in,
                R.anim.trans_left_out);
    }

    private void submit() {
        mobileNo = binding.mobilenoET.getText().toString();
        if (mobileNo.isEmpty()) {
            openDialog("Enter valid Mobile No", "warning");
        }  else {
            checkConnection();
            if (isConnected) {
                imeino=getDeviceID();
                login(imeino);

            } else {
                showSnack(isConnected);
            }
        }

    }

    private void initialize() {

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
                .make(findViewById(R.id.linearlayout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
        showSnack(isConnected);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void login(final String imeino)
    {
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String url = EndPoints.CHANGE_PASSWORD + "?userId=" + mobileNo +
               "&imeiNo=" + imeino;
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
                            binding.mobilenoET.setText("");

                            if (id == 200 ) {

                                String alertmsg = obj.getString("Message");
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

    private String getDeviceID()
    {
        String _deviceId = "000000000000000";
        try
        {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                _deviceId = res1.toString();
            }
        }
        catch (Exception e)
        {
            Log.d("ERROR", e.toString());
            //Logger.getLogger().WriteLog(e.getMessage());
            _deviceId = "000000000000000";

        }
        return _deviceId;
    }

    private void openDialog(String message, final String imagetype) {
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
        if (imagetype.equalsIgnoreCase("warning")) {
            imageView.setImageResource(R.drawable.warning);
            titleTV.setText("Warning!");
        } else if (imagetype.equalsIgnoreCase("success")) {
            imageView.setImageResource(R.drawable.success);
            titleTV.setText("Success!");
        } else if (imagetype.equalsIgnoreCase("Fail")) {
            imageView.setImageResource(R.drawable.sorry);
            titleTV.setText("Failure!");
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.dismiss();


            }
        });
    }

}
