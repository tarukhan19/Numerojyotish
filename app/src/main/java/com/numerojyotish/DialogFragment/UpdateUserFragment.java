package com.numerojyotish.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
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
import com.numerojyotish.Adapter.UserListAdapter;
import com.numerojyotish.Model.UserListDTO;
import com.numerojyotish.R;
import com.numerojyotish.Utils.ConnectivityReceiver;
import com.numerojyotish.Utils.EndPoints;
import com.numerojyotish.Utils.HideKeyboard;
import com.numerojyotish.Utils.MyApplication;
import com.numerojyotish.databinding.FragmentUpdateUserBinding;
import com.numerojyotish.databinding.FragmentUserListBinding;
import com.numerojyotish.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class UpdateUserFragment extends DialogFragment implements ConnectivityReceiver.ConnectivityReceiverListener{

    FragmentUpdateUserBinding binding;
    Dialog dialogUL;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    ImageView backIV;
    String firstName,lastName,gender,dateOfBirth,dateOfMembExpDate,mobileNo,emailId;
    boolean isConnected;
    private int mYear, mMonth, mDay;
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        dialogUL = super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(dialogUL.getContext()), R.layout.fragment_update_user, null, false);
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

        return dialogUL;
    }

    private void initialize() {

        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sessionManager = new SessionManager(getActivity().getApplicationContext());

        Toolbar toolbar = (Toolbar) dialogUL.findViewById(R.id.toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        backIV = toolbar.findViewById(R.id.plusimage);
        toolbar_title.setText("User Update");

        firstName = sessionManager.getCustomerDetails().get(SessionManager.KEY_CUSTOMER_FNAME);
        lastName = sessionManager.getCustomerDetails().get(SessionManager.KEY_CUSTOMER_LNAME);
        gender = sessionManager.getCustomerDetails().get(SessionManager.KEY_CUSTOMER_GENDER);
        dateOfBirth = sessionManager.getCustomerDetails().get(SessionManager.KEY_CUSTOMER_DOB);
        dateOfMembExpDate = sessionManager.getCustomerDetails().get(SessionManager.KEY_CUSTOMER_EXPIRATIONDATE);
        mobileNo = sessionManager.getCustomerDetails().get(SessionManager.KEY_CUSTOMER_MOBILENO);
        emailId = sessionManager.getCustomerDetails().get(SessionManager.KEY_CUSTOMER_EMAILID);

        if (gender.equalsIgnoreCase("Male"))
        {
            binding.maleRB.setChecked(true);
        }
        else
        {
            binding.femaleRB.setChecked(true);

        }

        binding.firstNameET.setText(firstName);
        binding.lastNameET.setText(lastName);
        binding.dobTV.setText(dateOfBirth);
        binding.membExpDateTV.setText(dateOfMembExpDate);
        binding.mobileNoET.setText(mobileNo);
        binding.emailET.setText(emailId);



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


        mDatePicker.show();
    }

    private void submit() {
        firstName = binding.firstNameET.getText().toString();
        lastName = binding.lastNameET.getText().toString();
        mobileNo = binding.mobileNoET.getText().toString();
        emailId = binding.emailET.getText().toString();
        dateOfBirth = binding.dobTV.getText().toString();
        dateOfMembExpDate = binding.membExpDateTV.getText().toString();


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
        }  else {
            checkConnection();
            if (isConnected) {
                login();

            } else {
                showSnack(isConnected);
            }
        }

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
            object.put("password", "");
            object.put("isUpdate", "true");



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
                            if (!status.equalsIgnoreCase("FAIL"))
                            {
                                UserListAdapter.getInstance().runThread(firstName,lastName,gender,dateOfBirth,dateOfMembExpDate,mobileNo,emailId);

                            }
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
                    dialogUL.dismiss();
                } else {
                    dialog.dismiss();



                }


            }
        });
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

}