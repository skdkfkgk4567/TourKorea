package com.example.sns_project.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
    Date time = new Date();

    public String uid;
    public String author;
    public String title;
    public String body;
    public String time1 = format1.format(time);
    public String imgurl;

    public int starCount = 0;
    public List<String> stars = new ArrayList<String>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String body,String time1,String imgurl) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.time1 = time1;
        this.imgurl = imgurl;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("date", time1);
        result.put("imgurl",imgurl);
        //result.put("stars", stars);

        return result;
    }
    // [END post_to_map]
    public  Map<String,Object> mdMap(){
        HashMap<String, Object> mdresult = new HashMap<>();
        mdresult.put("title", title);
        mdresult.put("body", body);
        mdresult.put("date", time1);

        return mdresult;
    }

}
// [END post_class]
