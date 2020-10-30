package com.example.sns_project.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String phone;
    public String photoUrl;
    public String password;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String phone,String photoUrl,String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.photoUrl = "gs://sns-project-4a58d.appspot.com/tt.jpg";
        this.password = password;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("email",email);
        result.put("phone", phone);
        result.put("photoUrl", photoUrl);
        result.put("password", password);
        //result.put("stars", stars);

        return result;
    }

}
// [END blog_user_class]
