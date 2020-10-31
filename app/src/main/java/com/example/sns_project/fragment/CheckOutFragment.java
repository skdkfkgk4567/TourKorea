package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sns_project.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class CheckOutFragment extends Fragment implements View.OnClickListener
{
    TextView tv;
    DatePicker dp;
    Calendar c;
    Button btnGetDate;
    private TabLayout tabLayout;
    private CheckOutFragment checkOutFragment;

    public static String TAG = "CheckInFragment";

    public CheckOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        c = Calendar.getInstance();
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH); //1월은 '0'부터 시작
        int dayOfMonth = c.get(c.DAY_OF_MONTH);

        tv = view.findViewById(R.id.checkintv);
        btnGetDate = view.findViewById(R.id.btn_update_date);
        dp = view.findViewById(R.id.checkinDate);
        checkOutFragment = new CheckOutFragment();
        btnGetDate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_update_date:
                tv.setText("");
                tv.setText("체크인 날짜 \n"+tv.getText() + " " + dp.getYear() + "/"
                        + (dp.getMonth()+1) + "/" + dp.getDayOfMonth());
                break;
        }
    }
}