package com.example.sns_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.sns_project.R;
import com.example.sns_project.activity.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecentPostsFragment extends PostListFragment
{
    public RecentPostsFragment() {}


    @Override
    public Query getQuery(FirebaseFirestore databaseReference)
    {
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys

        return databaseReference.collection("posts").orderBy("time1", Query.Direction.DESCENDING);
    }
}