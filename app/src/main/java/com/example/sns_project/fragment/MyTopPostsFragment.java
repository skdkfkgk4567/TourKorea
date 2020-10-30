package com.example.sns_project.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.sns_project.activity.MainActivity;
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
        // [START my_top_posts_query]
        // My top posts by number of stars
        String myUserId = getUid();
        // [END my_top_posts_query]

        return databaseReference.collection("posts").whereEqualTo("uid", getUid()).orderBy("starCount", Query.Direction.DESCENDING).orderBy("time1", Query.Direction.DESCENDING);
    }
}
