package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sns_project.R;
import com.example.sns_project.activity.MainActivity;
import com.example.sns_project.databinding.Cul2Binding;

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
            Cul2Binding binding = Cul2Binding.inflate(getLayoutInflater());
            binding.fabNewPost.setVisibility(View.VISIBLE);
        }

        return inflater.inflate(R.layout.fragment_map, container, false);

    }
}