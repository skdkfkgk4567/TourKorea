
package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sns_project.databinding.Cul2Binding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPostsFragment extends PostListFragment
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MyPostsFragment() {}

    @Override
    public Query getQuery(FirebaseFirestore databaseReference)
    {
        // All my posts
        Cul2Binding binding = Cul2Binding.inflate(getLayoutInflater());
        binding.fabNewPost.setVisibility(View.VISIBLE);
        return databaseReference.collection("posts").whereEqualTo("uid", getUid()).orderBy("time1", Query.Direction.DESCENDING);

    }

}