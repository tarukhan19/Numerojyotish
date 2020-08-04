package com.project.numerojyotish.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.project.numerojyotish.R;
import com.project.numerojyotish.Utils.ConnectivityReceiver;
import com.project.numerojyotish.Utils.EndPoints;
import com.project.numerojyotish.Utils.HideKeyboard;
import com.project.numerojyotish.Utils.MyApplication;
import com.project.numerojyotish.databinding.ActivityRegistrationBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {
ActivityRegistrationBinding binding;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    boolean isConnected;
    private int mYear, mMonth, mDay;

    String firstName,lastName,mobileNo,emailId,dateOfBirth="",dateOfMembExpDate="",gender="Male",password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        initialize();
        binding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard.hideKeyboard(RegistrationActivity.this);

                submit();
            }
        });

        binding.dobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HideKeyboard.hideKeyboard(RegistrationActivity.this);

                    showDateTimePicker("dob");

                }
                catch (Exception e)
                {}
            }
        });

        binding.membExpDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HideKeyboard.hideKeyboard(RegistrationActivity.this);

                    showDateTimePicker("exp");

                }
                catch (Exception e)
                {}
            }
        });

    }

    private void showDateTimePicker(final String from)
    {
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog mDatePicker = new DatePickerDialog(this,  AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "MM/dd/yyyy"; //Change as you need

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                if (from.equalsIgnoreCase("dob"))
                {
                    binding.dobTV.setText(sdf.format(myCalendar.getTime()));

                }
                else
                {
                    binding.membExpDateTV.setText(sdf.format(myCalendar.getTime()));
                }


                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);


        //   mDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//

        mDatePicker.show();
    }

    private void submit()
    {
        firstName=binding.firstNameET.getText().toString();
        lastName=binding.lastNameET.getText().toString();
        mobileNo = binding.mobileNoET.getText().toString();
        emailId=binding.emailET.getText().toString();
        dateOfBirth=binding.dobTV.getText().toString();
        dateOfMembExpDate=binding.membExpDateTV.getText().toString();
        password = binding.passwordET.getText().toString();
        if (binding.maleRB.isChecked()) {
            gender = "Male";
        } else {
            gender = "Female";
        }
        if (firstName.isEmpty())
        {
            openDialog("Enter valid First Name","warning");
        }
        else  if (lastName.isEmpty())
        {
            openDialog("Enter valid Last Name","warning");
        }
        else  if (mobileNo.isEmpty())
        {
            openDialog("Enter valid Mobile No","warning");
        }
        else  if (emailId.isEmpty())
        {
            openDialog("Enter valid Email ID","warning");
        }
        else  if (dateOfBirth.isEmpty())
        {
            openDialog("Enter valid Date of birth","warning");
        }
        else  if (dateOfMembExpDate.isEmpty())
        {
            openDialog("Enter valid Member Expiration Date","warning");
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
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JSONObject object=new JSONObject();

        try {

            object.put("firstName", firstName);
            object.put("lastName",lastName);
            object.put("gender",gender);
            object.put("dob",dateOfBirth);
            object.put("memberExpirationDate",dateOfMembExpDate);
            object.put("mobileNo",mobileNo);
            object.put("email",emailId);
            object.put("password",password);




        } catch (JSONException e) {
            e.printStackTrace ( );
        }
        Log.e("jsonObject", ">>>>>" + object.toString());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, EndPoints.REGISTRATION, object,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.e("BILL_FETCHresponse", ">>>>>" + response);
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            String message=jsonObject.getString("message");
                            String status=jsonObject.getString("status");
                            openDialog(message,status);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.e("errorApplication", ">>>>>" + error);
                        }
                    }
                }) {


        }
                ;


        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

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
        else if (imagetype.equalsIgnoreCase("SUCCESS"))
        {
            imageView.setImageResource(R.drawable.success);
            titleTV.setText("Success!");
        }
        else if (imagetype.equalsIgnoreCase("FAIL"))
        {
            imageView.setImageResource(R.drawable.sorry);
            titleTV.setText("Failure!");
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (imagetype.equalsIgnoreCase("warning")  || imagetype.equalsIgnoreCase("FAIL"))
                {
                    dialog.dismiss();
                }

                else
                {
                    dialog.dismiss();

                    binding.firstNameET.setText("");
                    binding.lastNameET.setText("");
                    binding.mobileNoET.setText("");
                    binding.emailET.setText("");
                    binding.dobTV.setText("");
                    binding.membExpDateTV.setText("");
                    binding.passwordET.setText("");
                    binding.maleRB.setChecked(true);
                    binding.femaleRB.setChecked(false);
                    binding.firstNameET.requestFocus();

                }


            }
        });
    }
}
