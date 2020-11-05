package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sns_project.R;

public class PersonFragment extends Fragment
{
    public static Button adult_min, adult_plus, child_min, child_plus, baby_min, baby_plus, complete;
    public static TextView adult_count, child_count, baby_count, adult_final, child_final, baby_final;
    public PersonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        View view2 = inflater.inflate(R.layout.fragment_complete, container, false);
        //TextView
        adult_count = view.findViewById(R.id.adult_count);
        child_count = view.findViewById(R.id.child_count);
        baby_count = view.findViewById(R.id.baby_count);
        adult_final  = view2.findViewById(R.id.adult_final);


        //Button
        adult_min = view.findViewById(R.id.adult_min);
        adult_plus = view.findViewById(R.id.adult_plus);
        child_min = view.findViewById(R.id.child_min);
        child_plus = view.findViewById(R.id.child_plus);
        baby_min = view.findViewById(R.id.baby_min);
        baby_plus = view.findViewById(R.id.baby_plus);
        complete = view.findViewById(R.id.complete);

        //ClickListener
        adult_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Integer.parseInt(adult_count.getText().toString()) > 0)
                {
                    int adult_c = Integer.parseInt(adult_count.getText().toString());
                    adult_c -= 1;
                    String Sadult_c = String.valueOf(adult_c);
                    adult_count.setText(Sadult_c);
                }
            }
        });
        adult_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int adult_c = Integer.parseInt(adult_count.getText().toString());
                adult_c += 1;
                String Sadult_c = String.valueOf(adult_c);
                adult_count.setText(Sadult_c);
            }
        });
        child_min.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Integer.parseInt(child_count.getText().toString()) > 0)
                {
                    int adult_c = Integer.parseInt(child_count.getText().toString());
                    adult_c -= 1;
                    String Schild_c = String.valueOf(adult_c);
                    child_count.setText(Schild_c);
                }
            }
        });
        child_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int child_c = Integer.parseInt(child_count.getText().toString());
                child_c += 1;
                String Schild_c = String.valueOf(child_c);
                child_count.setText(Schild_c);
            }
        });
        baby_min.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Integer.parseInt(baby_count.getText().toString()) > 0)
                {
                    int baby_c = Integer.parseInt(baby_count.getText().toString());
                    baby_c -= 1;
                    String Sbaby_c = String.valueOf(baby_c);
                    baby_count.setText(Sbaby_c);
                }
            }
        });
        baby_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int baby_c = Integer.parseInt(baby_count.getText().toString());
                baby_c += 1;
                String Sbaby_c = String.valueOf(baby_c);
                baby_count.setText(Sbaby_c);
            }
        });
        complete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adult_final.setText("성인 : " + adult_count.getText() + "명");
                child_final.setText("어린이 : " + child_count.getText() + "명");
                baby_final.setText("유아 : " + baby_count.getText() + "명");
            }
        });

        return view;
    }
}
