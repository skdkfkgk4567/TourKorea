package com.example.sns_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sns_project.R;
import com.example.sns_project.activity.MainActivity;
import com.example.sns_project.activity.NowLocation;

public class NowLocationFrag extends Fragment {
    public NowLocationFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("MainActivity.PageName : "+MainActivity.PageName);
        if (MainActivity.PageName.equals("nowlocation"))
        {
            System.out.println("MainActivity.PageName : "+MainActivity.PageName);
            getActivity().startActivity(new Intent(getActivity(), NowLocation.class));
        }
        return inflater.inflate(R.layout.fragment_map, container, false);

    }
}