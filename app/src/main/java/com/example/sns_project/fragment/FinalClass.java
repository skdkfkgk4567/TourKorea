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

public class FinalClass extends Fragment
{
    DatePicker comcheckin, comcheckout;
    TextView date_final, adult_final;
    Button Reservation, CheckBtn;
    PersonFragment personFragment = new PersonFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
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
                        date_final.setText("체크인 날짜 : "+date_final.getText() + " " + comcheckin.getYear() + "/" + comcheckin.getMonth()+1 + "/" + comcheckin.getDayOfMonth()
                        + "\n체크아웃 날짜 : "+date_final.getText() + " " + comcheckout.getYear() + "/" + comcheckout.getMonth()+1 + "/" + comcheckout.getDayOfMonth());
                        break;
                }
            }
        });

        date_final = view.findViewById(R.id.date_final);
        adult_final = view.findViewById(R.id.adult_final);
        Reservation = view.findViewById(R.id.Reservation);


        adult_final.setText("성인 : " + personFragment.adult_count.getText() + "명     " + "어린이 : " + personFragment.child_count.getText() + "명     " + "유아 : " + personFragment.baby_count.getText() + "명");

        personFragment.complete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adult_final.setText("성인 : " + personFragment.adult_count.getText() + "명     " + "어린이 : " + personFragment.child_count.getText() + "명     " + "유아 : " + personFragment.baby_count.getText() + "명");
                /*
                adult_final.setText("성인 : " + personFragment.adult_count.getText() + "명");
                child_final.setText("어린이 : " + personFragment.child_count.getText() + "명");
                baby_final.setText("유아 : " + personFragment.baby_count.getText() + "명");
                */


            }
        });

        return view;
    }
}
