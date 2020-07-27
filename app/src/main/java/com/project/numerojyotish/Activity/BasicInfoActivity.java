package com.project.numerojyotish.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.project.numerojyotish.Api.ApiClass;
import com.project.numerojyotish.R;
import com.project.numerojyotish.Utils.ConnectivityReceiver;
import com.project.numerojyotish.Utils.HideKeyboard;
import com.project.numerojyotish.Utils.MyApplication;
import com.project.numerojyotish.databinding.ActivityBasicInfoBinding;
import com.project.numerojyotish.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BasicInfoActivity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {
ActivityBasicInfoBinding binding;
    private int mYear, mMonth, mDay;
    String name,dob,gender="Male";
    int yy,mm,dd;
    boolean isConnected;
    private SessionManager session;
    static BasicInfoActivity basicInfoActivity;
    ApiClass apiClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_info);
        initialize();

        binding.submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=binding.nameET.getText().toString();
                dob=binding.dobTV.getText().toString();
                HideKeyboard.hideKeyboard(BasicInfoActivity.this);


        //      apiClass.getHomeData(BasicInfoActivity.this,"");

                if (binding.maleRB.isChecked()) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }
                if (name.isEmpty())
                {
                    openDialog("Enter valid Name.","warning");
                }
                else if (dob.isEmpty())
                {
                    openDialog("Enter valid date of birth.","warning");
                }
                else
                {
                    session.setBasicDetails(dob,name,gender);

                    checkConnection();
                    if (isConnected)
                    {
                        apiClass.getHomeData(BasicInfoActivity.this,"");

                    }
                    else
                    {
                        showSnack(isConnected);
                    }
                }


            }
        });

        binding.dobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HideKeyboard.hideKeyboard(BasicInfoActivity.this);

                    showDateTimePicker("dob");

                }
                catch (Exception e)
                {}
            }
        });




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

                }


            }
        });
    }
    private void initialize() {
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Laila-Regular.ttf");
        binding.maleRB.setTypeface(customFont);
        binding.femaleRB.setTypeface(customFont);
        session = new SessionManager(getApplicationContext());
        apiClass = new ApiClass();
        basicInfoActivity = this;

    }
    public static BasicInfoActivity getInstance() {
        return basicInfoActivity;
    }


    private void showDateTimePicker(final String from)
    {
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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


                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);



            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        mDatePicker.show();
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
    public void runThread(final String response) {

        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            session.setResponse(response);
                            Intent in7 = new Intent(BasicInfoActivity.this, HomePageActivity.class);
                            in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(in7);
                            overridePendingTransition(R.anim.trans_left_in,
                                    R.anim.trans_left_out);

                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

}
