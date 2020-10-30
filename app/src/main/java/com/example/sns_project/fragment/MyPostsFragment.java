
package com.example.sns_project.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.sns_project.activity.MainActivity;
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
        return databaseReference.collection("posts").whereEqualTo("uid", getUid()).orderBy("time1", Query.Direction.DESCENDING);
    }

}