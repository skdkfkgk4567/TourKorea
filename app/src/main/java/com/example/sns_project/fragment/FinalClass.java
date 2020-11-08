package com.example.sns_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sns_project.R;
import com.example.sns_project.activity.MainActivity;

public class FinalClass extends Fragment
{
    DatePicker comcheckin, comcheckout;
    public static TextView date_final, adult_final, hotel_name, hotel_address;
    Button Reservation, CheckBtn;
    PersonFragment personFragment = new PersonFragment();
    public static int person;
    public static int adult;
    public static int child;
    public static int baby;
    public static String areacode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        comcheckin = view.findViewById(R.id.comcheckin);
        comcheckout = view.findViewById(R.id.comcheckout);
        hotel_name = view.findViewById(R.id.hotel_name);
        hotel_address = view.findViewById(R.id.hotel_address);

        CheckBtn = view.findViewById(R.id.CheckBtn);

        CheckBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.CheckBtn:
                        String test = hotel_address.getText().toString();
                        String split_test[] = test.split(" ");
                        System.out.println(split_test[0]);
                        switch (split_test[0])
                        {
                            case "서울":
                                areacode = "1";
                                break;
                            case "인천":
                                areacode = "2";
                                break;
                            case "대전":
                                areacode = "3";
                                break;
                            case "대구":
                                areacode = "4";
                                break;
                            case "광주":
                                areacode = "5";
                                break;
                            case "부산":
                                areacode = "6";
                                break;
                            case "울산":
                                areacode = "7";
                                break;
                            case "세종특별자치시":
                                areacode = "8";
                                break;
                            case "경기도":
                                areacode = "31";
                                break;
                            case "강원도":
                                areacode = "32";
                                break;
                            case "충북":
                                areacode = "33";
                                break;
                            case "충남":
                                areacode = "34";
                                break;
                            case "경북":
                                areacode = "35";
                                break;
                            case "경남":
                                areacode = "36";
                                break;
                            case "전북":
                                areacode = "37";
                                break;
                            case "전남":
                                areacode = "38";
                                break;
                            case "제주":
                                areacode = "39";
                                break;
                        }

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("areacode",areacode);
                        startActivity(intent);

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
                adult = Integer.valueOf((String) personFragment.adult_count.getText());
                child = Integer.valueOf((String) personFragment.child_count.getText());
                baby = Integer.valueOf((String) personFragment.baby_count.getText());
                person = adult+child+baby;
            }
        });

        return view;
    }
}
