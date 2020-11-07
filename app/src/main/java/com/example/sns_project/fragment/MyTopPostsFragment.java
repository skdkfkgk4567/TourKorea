package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sns_project.databinding.Cul2Binding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyTopPostsFragment extends PostListFragment
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MyTopPostsFragment() {}

    @Override
    public Query getQuery(FirebaseFirestore databaseReference)
    {
        String myUserId = getUid();
        Cul2Binding binding = Cul2Binding.inflate(getLayoutInflater());
        binding.fabNewPost.setVisibility(View.VISIBLE);
        return databaseReference.collection("posts").orderBy("starCount", Query.Direction.DESCENDING).orderBy("time1", Query.Direction.DESCENDING);
    }
}
