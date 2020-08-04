package com.project.numerojyotish.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.project.numerojyotish.Api.ApiClass;
import com.project.numerojyotish.R;
import com.project.numerojyotish.Utils.ConnectivityReceiver;
import com.project.numerojyotish.Utils.EndPoints;
import com.project.numerojyotish.Utils.HideKeyboard;
import com.project.numerojyotish.Utils.MyApplication;
import com.project.numerojyotish.databinding.FragmentRegistrationBinding;
import com.project.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    FragmentRegistrationBinding binding;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private SessionManager session;
    boolean isConnected;
    private int mYear, mMonth, mDay;
    String firstName, lastName, mobileNo, emailId, dateOfBirth = "", dateOfMembExpDate = "", gender = "Male", password, confpassword;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        View view = binding.getRoot();
        initialize();
        binding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideKeyboard.hideKeyboard(getActivity());

                submit();
            }
        });

        binding.dobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HideKeyboard.hideKeyboard(getActivity());

                    showDateTimePicker("dob");

                } catch (Exception e) {
                }
            }
        });

        binding.membExpDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HideKeyboard.hideKeyboard(getActivity());

                    showDateTimePicker("exp");

                } catch (Exception e) {
                }
            }
        });

        return view;
    }

    private void showDateTimePicker(final String from) {
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "MM/dd/yyyy"; //Change as you need

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                if (from.equalsIgnoreCase("dob")) {
                    binding.dobTV.setText(sdf.format(myCalendar.getTime()));

                } else {
                    binding.membExpDateTV.setText(sdf.format(myCalendar.getTime()));
                }


                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);


        //   mDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
        if (from.equalsIgnoreCase("dob"))
        {
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        else
        {
            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        mDatePicker.show();
    }

    private void submit() {
        firstName = binding.firstNameET.getText().toString();
        lastName = binding.lastNameET.getText().toString();
        mobileNo = binding.mobileNoET.getText().toString();
        emailId = binding.emailET.getText().toString();
        dateOfBirth = binding.dobTV.getText().toString();
        dateOfMembExpDate = binding.membExpDateTV.getText().toString();
        password = binding.passwordET.getText().toString();
        confpassword = binding.confpasswordET.getText().toString();

        if (binding.maleRB.isChecked()) {
            gender = "Male";
        } else {
            gender = "Female";
        }
        if (firstName.isEmpty()) {
            openDialog("Enter valid First Name", "warning");
        } else if (lastName.isEmpty()) {
            openDialog("Enter valid Last Name", "warning");
        } else if (mobileNo.isEmpty()) {
            openDialog("Enter valid Mobile No", "warning");
        } else if (emailId.isEmpty()) {
            openDialog("Enter valid Email ID", "warning");
        } else if (dateOfBirth.isEmpty()) {
            openDialog("Enter valid Date of birth", "warning");
        } else if (dateOfMembExpDate.isEmpty()) {
            openDialog("Enter valid Member Expiration Date", "warning");
        } else if (password.isEmpty()) {
            openDialog("Enter valid Password.", "warning");
        } else if (!confpassword.equals(password)) {
            openDialog("Password doesn't match", "warning");
        } else {
            checkConnection();
            if (isConnected) {
                login();

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

    private void login() {
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JSONObject object = new JSONObject();

        try {

            object.put("firstName", firstName);
            object.put("lastName", lastName);
            object.put("gender", gender);
            object.put("dob", dateOfBirth);
            object.put("memberExpirationDate", dateOfMembExpDate);
            object.put("mobileNo", mobileNo);
            object.put("email", emailId);
            object.put("password", password);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("jsonObject", ">>>>>" + object.toString());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, EndPoints.REGISTRATION, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("BILL_FETCHresponse", ">>>>>" + response);
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");
                            openDialog(message, status);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            Log.e("errorApplication", ">>>>>" + error);
                        }
                    }
                }) {


        };


        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

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
                } else {
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
