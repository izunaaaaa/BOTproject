package com.example.bot.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bot.R;
import com.example.bot.model.Item;
import com.example.bot.retrofit.ResultListener;
import com.example.bot.retrofit.RetrofitManager;
import java.util.List;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
    }

}