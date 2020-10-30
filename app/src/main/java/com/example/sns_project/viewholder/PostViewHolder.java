package com.example.sns_project.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sns_project.R;
import com.example.sns_project.models.Post;


public class PostViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "Post";

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView timeView;
    public ImageView mauthorView;


    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.postNumStars);
        bodyView = itemView.findViewById(R.id.postBody);
        timeView = itemView.findViewById(R.id.posttime1);
        mauthorView = itemView.findViewById(R.id.postAuthorPhoto);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);
        timeView.setText(post.time1);

        starView.setOnClickListener(starClickListener);
    }

}