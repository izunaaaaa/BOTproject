package com.example.bot.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bot.R;
import com.example.bot.abstracts.GPSActivity;
import com.example.bot.model.Item;
import com.example.bot.utils.Utils;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

public class MapActivity extends GPSActivity {

    private static final String TMAP_KEY = "l7xx00f745db3f144f3a8f3ea2a6f8ba94ad";
    public static final String ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTitle = findViewById(R.id.tv_title);

        Intent intent = getIntent();
        if (intent != null) {
            Item item = (Item) intent.getSerializableExtra(ITEM);
            tvTitle.setText(item.getTitle());

            double latitude = item.getMapy();
            double longitude = item.getMapx();

            //https://tmapapi.sktelecom.com/main.html#android/sample/androidSample.sample1 가이드 참조.
            LinearLayout linearLayoutTmap = findViewById(R.id.linearLayoutTmap);
            TMapView tMapView = new TMapView(this);

            tMapView.setSKTMapApiKey( TMAP_KEY );
            linearLayoutTmap.addView( tMapView );

            TMapMarkerItem markerItem1 = new TMapMarkerItem();

            TMapPoint tMapPoint1 = new TMapPoint(latitude, longitude);

            Bitmap bitmap = Utils.getBitmapFromVector(this, R.drawable.ic_marker, 30);

            markerItem1.setIcon(bitmap); // 마커 아이콘 지정
            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
            markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
            markerItem1.setName(item.getTitle()); // 마커의 타이틀 지정
            tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가

            tMapView.setCenterPoint( longitude, latitude );

        }
    }

}