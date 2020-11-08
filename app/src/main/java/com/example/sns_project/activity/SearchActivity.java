package com.example.sns_project.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sns_project.R;
import com.example.sns_project.adapter.CustomAdapter;
import com.example.sns_project.fragment.FinalClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static String TAG = "phptest_MainActivity";
    private View header;
    private ImageView photoimageView;
    private LayoutInflater inflater;

    private static final String TAG_JSON="webnautes";
    private static final String TAG_HotelName = "HotelName";
    private static final String TAG_SiGunGu = "SiGunGu";
    private static final String TAG_ImageLink ="ImageLink";
    public static ImageView imageview;
    public static TextView placeNameText;
    public static TextView addressTextView;
    private static int count = 0;
    private int person;
    private String Clcode;
    private String Clcode2;
    private int adult;
    private int child;
    private int baby;
    public static String OKB;
    public static Activity searchActivity;
    public static String HotelName;
    public static String HotelAddress;

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;
    Button button;
    TextView textView, hotel_name, hotel_address;
    Bitmap bitmap;
    TextView txtResult;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchaddress);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        person = FinalClass.person;
        adult = FinalClass.adult;
        child = FinalClass.child;
        baby = FinalClass.baby;
        OKB = PopupRoom.OKB;
        hotel_name = FinalClass.hotel_name;
        hotel_address = FinalClass.hotel_address;
        searchActivity = SearchActivity.this;


        System.out.println("SearchActivity person : "+person);
        switch (person)
        {

            case 0:
                Toast.makeText(getApplicationContext(), "인원을 선택해주세요!!!!!!",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 1:
                if(adult==0)
                {
                    Toast.makeText(getApplicationContext(), "성인없이 숙박업소 예약은 불가능합니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Clcode="1";
                    Clcode2="1";
                    break;
                }
            case 2:
            case 3:
            case 4:
                if(adult==0)
                {
                    Toast.makeText(getApplicationContext(), "성인없이 숙박업소 예약은 불가능합니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Clcode="2";
                    Clcode2="3";
                    break;
                }

            case 5:
            default:
                if(adult==0)
                {
                    Toast.makeText(getApplicationContext(), "성인없이 숙박업소 예약은 불가능합니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Clcode="4";
                    Clcode2="4";
                    break;
                }
        }

        placeNameText = findViewById(R.id.placeNameText);
        addressTextView = findViewById(R.id.addressTextView);
        textView = findViewById(R.id.search_place);
        button = findViewById(R.id.search);

        mlistView = findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();

        mlistView.setOnItemClickListener(this);
        button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SiGunGu = textView.getText().toString();
                System.out.println("SiGunGu : "+SiGunGu);
                GetData task = new GetData();
                task.execute(SiGunGu,Clcode,Clcode2);
            }
        });

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String data = parent.getAdapter().getItem(position).toString();
        System.out.println("data : "+data);
        HotelName = data.substring(data.indexOf(", HotelName=")+12,data.length()-1);
        HotelAddress = data.substring(data.indexOf("SiGunGu=")+8,data.indexOf(", HotelName="));
        hotel_name.setText(HotelName);
        hotel_address.setText(HotelAddress);
        Intent intent = new Intent(SearchActivity.this, PopupRoom.class);
        startActivityForResult(intent, 1);
    }

    public class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SearchActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null){

            }
            else {


                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = "http://nsh.iptime.org:8012/ssss.php?SiGunGu=";
            String SiGunGu = params[0];
            String Clcode = params[1];
            String Clcode2 = params[2];


            try {

                URL url = new URL(serverURL+SiGunGu+"&Clcode="+Clcode+"&Clcode2="+Clcode2);
                System.out.println("url : "+url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult(){

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            mArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String HotelName = item.getString(TAG_HotelName);
                String HotelAddress = item.getString(TAG_SiGunGu);
                String ImageLink = item.getString(TAG_ImageLink);
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_HotelName, HotelName);
                hashMap.put(TAG_SiGunGu, HotelAddress);
                hashMap.put(TAG_ImageLink, ImageLink);

                count = i;

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new CustomAdapter(
                    SearchActivity.this, mArrayList, R.layout.item_place_list,
                    new String[]{TAG_HotelName, TAG_SiGunGu, TAG_ImageLink},
                    new int[]{R.id.placeNameText, R.id.addressTextView, R.id.photoImage}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }
}