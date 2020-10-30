package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sns_project.R;

public class PlaceFragment extends Fragment
{
    public PlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("PlaceFragment 실행");
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        return view;
    }
}
