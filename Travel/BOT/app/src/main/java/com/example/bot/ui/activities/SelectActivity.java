package com.example.bot.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bot.R;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_screen);
        CardView purpose = (CardView) findViewById(R.id.purpose);
        purpose.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                Intent a= new Intent(SelectActivity.this,
                        MainActivity.class);
                startActivity(a);
            }
        });
        CardView theme = (CardView) findViewById(R.id.theme);
        theme.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                Intent b=new Intent(SelectActivity.this,
                        ThemeActivity.class);
                startActivity(b);
            }
        });
    }
}