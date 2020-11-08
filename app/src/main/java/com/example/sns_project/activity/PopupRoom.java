package com.example.sns_project.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sns_project.R;

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

import static com.example.sns_project.activity.SearchActivity.searchActivity;

public class PopupRoom extends AppCompatActivity
{
    private static final String TAG_JSON="webnautes";
    private static final String TAG_RoomKinds = "RoomKinds";
    private static final String TAG_Price = "Price";
    private static String TAG = "PopupRoom";
    public static String OKB = "";
    String mJsonString;
    ArrayList<HashMap<String, String>> mArrayList;
    private static int count = 0;
    ListView mlistView;

    Button okBtn, cancleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 상태바 제거 ( 전체화면 모드 )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.popup_activity);

        okBtn = (Button) findViewById(R.id.okBtn);
        cancleBtn = (Button) findViewById(R.id.cancleBtn);
        mArrayList = new ArrayList<>();
        GetData task = new GetData();
        task.execute();


    }

    //동작 버튼 클릭
    public void mOk(View v)
    {
        searchActivity.finish();
        finish();
    }


    //취소 버튼 클릭
    public void mCancle(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(PopupRoom.this,
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

            String serverURL = "http://nsh.iptime.org:8012/get_room.php";


            try {

                URL url = new URL(serverURL);
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
            //mArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String RoomKinds = item.getString(TAG_RoomKinds);
                String Price = item.getString(TAG_Price);
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_RoomKinds, RoomKinds);
                hashMap.put(TAG_Price, Price);

                count = i;

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    PopupRoom.this, mArrayList, R.layout.item_place_list,
                    new String[]{TAG_RoomKinds, TAG_Price},
                    new int[]{R.id.placeNameText, R.id.addressTextView}
            );
            mlistView = findViewById(R.id.listView_main_list);
            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }
}
