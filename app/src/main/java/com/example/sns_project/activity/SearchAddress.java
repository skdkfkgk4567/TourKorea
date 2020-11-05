package com.example.sns_project.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns_project.CallBack.RecyclerViewAdapterCallback;
import com.example.sns_project.R;
import com.example.sns_project.adapter.RecyclerViewAdapter;

public class SearchAddress extends AppCompatActivity implements RecyclerViewAdapterCallback {

    private EditText editText;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable workRunnable;
    private final long DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInit();
    }

    @Override
    public void showToast(int position) {
        Toast.makeText(this, position + " clicked.", Toast.LENGTH_SHORT).show();
    }

    private void layoutInit() {
        editText = (EditText)findViewById(R.id.edt_search);
        recyclerView = (RecyclerView)findViewById(R.id.rl_listview);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String keyword = s.toString();

                handler.removeCallbacks(workRunnable);
                workRunnable = new Runnable() {
                    @Override
                    public void run() {
                        adapter.filter(keyword);
                    }
                };
                handler.postDelayed(workRunnable, DELAY);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerViewAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setCallback(this);
    }
}
