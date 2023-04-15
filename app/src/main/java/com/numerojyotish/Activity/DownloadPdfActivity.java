package com.numerojyotish.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

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
import com.numerojyotish.databinding.ActivityDownloadPdfBinding;
import com.numerojyotish.session.SessionManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DownloadPdfActivity  extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
     ActivityDownloadPdfBinding binding;
    String name, dateOfBirth = "", noOfYear = "", gender = "Male";
    ImageView backIV;
    private int mYear, mMonth, mDay;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    boolean isConnected;
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download_pdf);
        initialize();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        backIV = toolbar.findViewById(R.id.plusimage);
        toolbar_title.setText("PDF View");

        initialize();


        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard.hideKeyboard(DownloadPdfActivity.this);
                submit();
            }
        });

        binding.dobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HideKeyboard.hideKeyboard(DownloadPdfActivity.this);

                    showDateTimePicker("dob");

                } catch (Exception e) {
                }
            }
        });

    }

    private void showDateTimePicker(final String from) {
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog mDatePicker = new DatePickerDialog(DownloadPdfActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "MM/dd/yyyy"; //Change as you need

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                if (from.equalsIgnoreCase("dob")) {
                    binding.dobTV.setText(sdf.format(myCalendar.getTime()));

                }


                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);


        mDatePicker.show();
    }



    private void submit() {
        name = binding.nameET.getText().toString();
        dateOfBirth = binding.dobTV.getText().toString();
        noOfYear = binding.yearET.getText().toString();

        if (binding.maleRB.isChecked()) {
            gender = "Male";
        } else {
            gender = "Female";
        }
        if (name.isEmpty()) {
            openDialog("Enter valid Name", "warning");
        } else if (noOfYear.isEmpty()) {
            openDialog("Enter valid no. of years", "warning");
        } else if (dateOfBirth.isEmpty()) {
            openDialog("Enter valid Date of birth", "warning");
        } else {
            checkConnection();
            if (isConnected) {
                login();

            } else {
                showSnack(isConnected);
            }
        }

    }

    private void initialize()
    {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Window window =getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(DownloadPdfActivity.this, R.color.colorPrimary));
        }

        intent=getIntent();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (intent.hasExtra("from"))
        {
            binding.toolbar.setVisibility(View.GONE);
        }
        else
        {
            binding.toolbar.setVisibility(View.VISIBLE);
        }
        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(DownloadPdfActivity.this);
        requestQueue = Volley.newRequestQueue(DownloadPdfActivity.this);
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
                .make(findViewById(R.id.linearlayout), message, Snackbar.LENGTH_LONG);

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
    private void login() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = EndPoints.DOWNLOAD_PDF + "?name=" + name + "&gender=" + gender+"&dob="+dateOfBirth+"&noOfyear="+noOfYear;


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("response",response);
                        String url=response.replace("\"","");
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "No app can handle this request", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } catch (Exception e) {
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
        final Dialog dialog = new Dialog(DownloadPdfActivity.this, R.style.CustomDialog);
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
                }
            }
        });
    }

}
