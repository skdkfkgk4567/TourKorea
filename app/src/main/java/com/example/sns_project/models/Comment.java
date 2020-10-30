package com.example.sns_project.models;


import com.google.firebase.firestore.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
    Date time = new Date();

    public String uid;
    public String author;
    public String text;
    public String commenttime = format1.format(time);

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text, String commenttime) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.commenttime = commenttime;
    }

}
