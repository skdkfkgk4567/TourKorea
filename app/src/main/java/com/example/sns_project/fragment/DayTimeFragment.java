package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sns_project.R;

public class DayTimeFragment extends Fragment
{
    DatePicker comcheckin, comcheckout;
    TextView date_final, adult_final;
    Button CheckBtn;
    public static int checkinMonth;
    public static int checkoutMonth;
    public static String checkinDate;
    public static  String checkoutDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        comcheckin = view.findViewById(R.id.comcheckin);
        comcheckout = view.findViewById(R.id.comcheckout);

        CheckBtn = view.findViewById(R.id.CheckBtn);

        CheckBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.CheckBtn:
                        date_final.setText("");
                        checkinMonth = comcheckin.getMonth()+1;
                        checkoutMonth = comcheckout.getMonth()+1;
                        String cingetYear = String.valueOf(comcheckin.getYear());
                        String cingetMonth = String.valueOf(checkinMonth);
                        String cingetDay = String.valueOf(comcheckin.getDayOfMonth());
                        checkinDate = cingetYear+cingetMonth+cingetDay;
                        String coutgetYear = String.valueOf(comcheckout.getYear());
                        String coutgetMonth = String.valueOf(checkoutMonth);
                        String coutgetDay = String.valueOf(comcheckout.getDayOfMonth());
                        checkoutDate = coutgetYear+coutgetMonth+coutgetDay;
                        FinalClass.date_final.setText("체크인 날짜 : "+date_final.getText() + " " + comcheckin.getYear() + "/" + checkinMonth + "/" + comcheckin.getDayOfMonth()
                                + "\n체크아웃 날짜 : "+date_final.getText() + " " + comcheckout.getYear() + "/" + checkoutMonth + "/" + comcheckout.getDayOfMonth());
                        break;
                }
            }
        });

        date_final = view.findViewById(R.id.date_final);
        adult_final = view.findViewById(R.id.adult_final);

        return view;
    }
}
