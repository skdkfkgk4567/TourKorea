package com.example.sns_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sns_project.R;
import com.example.sns_project.activity.BookingSystem;
import com.example.sns_project.activity.MainActivity;
import com.example.sns_project.databinding.Cul2Binding;

public class BookingFragment extends Fragment
{
    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println(MainActivity.PageName);
        if (MainActivity.PageName.equals("BookingFragment"))
        {
            getActivity().startActivity(new Intent(getActivity(), BookingSystem.class));
            Cul2Binding binding = Cul2Binding.inflate(getLayoutInflater());
            binding.fabNewPost.setVisibility(View.GONE);
        }
        return inflater.inflate(R.layout.activity_booking, container, false);

    }
}
