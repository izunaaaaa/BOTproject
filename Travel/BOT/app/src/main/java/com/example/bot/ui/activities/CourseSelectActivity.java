package com.example.bot.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bot.R;
import com.example.bot.utils.Utils;

public class CourseSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_select);

        findViewById(R.id.button_back).setOnClickListener(v -> finish());

        int[] tvIds = { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6 };
        int[] colors = { 0xff4472c4, 0xff00b050, 0xff7030a0, 0xffed7d31, 0xffd038c9, 0xFFF44336};
        String[] categories = { "C0112", "C0113", "C0114", "C0115", "C0116", "C0117"};

        for (int i = 0; i < tvIds.length; i++) {
            TextView textView = findViewById(tvIds[i]);

            final int color = colors[i];
            final String category = categories[i];
            final String title = textView.getText().toString();

            textView.setBackground(Utils.getRoundRectangle(color, 10));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseSelectActivity.this, CourseActivity.class);
                    intent.putExtra(CourseActivity.TITLE, title);
                    intent.putExtra(CourseActivity.CATEGORY, category);
                    intent.putExtra(CourseActivity.COLOR, color);
                    startActivity(intent);
                }
            });
        }

    }

}