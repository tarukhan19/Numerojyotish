package com.project.numerojyotish.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.project.numerojyotish.DialogFragment.BasicChartFragment;
import com.project.numerojyotish.R;
import com.project.numerojyotish.databinding.FragmentChartBinding;
import com.project.numerojyotish.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {
FragmentChartBinding binding;
    int selectyear;
    int yy,mm,dd;
    private int mYear, mMonth, mDay;
    private SessionManager session;



    public ChartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart, container, false);
        View view = binding.getRoot();
        session = new SessionManager(getActivity().getApplicationContext());

        binding.submitBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (String.valueOf(selectyear).isEmpty())
                {
                    openDialog("Enter valid Date.","warning");
                }

                else
                {
                    session.setFromToDate(String.valueOf(selectyear));
                    BasicChartFragment dialogFragment = new BasicChartFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                    ft.addToBackStack(null);
                    dialogFragment.show(ft, "dialog");
               }


            }
        });



        binding.selectYearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showDateTimePicker();
                }
                catch (Exception e)
                {}
            }
        });


        return  view;
    }


    private void openDialog(String message, final String imagetype)
    {
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


    private void showDateTimePicker()
    {
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "MM/dd/yyyy"; //Change as you need

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
//
//                if(from.equalsIgnoreCase("todate"))
//                {
//                    binding.todateTV.setText(sdf.format(myCalendar.getTime()));
//
//                }
//
//                else if(from.equalsIgnoreCase("fromdate"))
//                {
                    binding.selectYearTV.setText(sdf.format(myCalendar.getTime()));
                    selectyear=myCalendar.get(Calendar.YEAR);
                    yy = myCalendar.get(Calendar.YEAR);
                    mm = myCalendar.get(Calendar.MONTH);
                    dd = myCalendar.get(Calendar.DAY_OF_MONTH);

    //            }


                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);


//
//        if (from.equalsIgnoreCase("fromdate"))
//        {
//            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//        }
//        else if (from.equalsIgnoreCase("todate"))
//        {
//            Log.e("mYear-----",Calendar.YEAR+"");
//
//            myCalendar.set(Calendar.MONTH, mm);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dd);
//            myCalendar.set(Calendar.YEAR, yy );
//            mDatePicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
//            myCalendar.set(Calendar.MONTH, mm);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dd);
//            myCalendar.set(Calendar.YEAR, yy + 5);
//            mDatePicker.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
//
//        }
        mDatePicker.show();
    }
}
