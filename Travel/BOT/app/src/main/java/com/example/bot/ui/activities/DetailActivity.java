package com.example.bot.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.bot.R;
import com.example.bot.model.Item;
import com.example.bot.retrofit.ResultListener;
import com.example.bot.retrofit.RetrofitManager;
import com.example.bot.utils.Utils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String ITEM = "item";
    private TextView mTvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageButton buttonBack = findViewById(R.id.button_back);
        buttonBack.setBackground(Utils.getRoundRectangle(0x10000000, 30, 7, Color.TRANSPARENT));
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        ImageView imageView = findViewById(R.id.image_view);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvLocation = findViewById(R.id.tv_location);
        mTvOverview = findViewById(R.id.tv_overview);
        mTvOverview.setMovementMethod(LinkMovementMethod.getInstance()); //텍스트에 hyperlink가 포함 될 경우 클릭 가능하도록 설정

        Intent intent = getIntent();
        if (intent != null) {
            Item item = (Item) intent.getSerializableExtra(ITEM);

            String location = (item.getTel() == null) ? item.getAddr1() : item.getAddr1() + "\n\n" + item.getTel();
            if (location == null) {
                tvLocation.setVisibility(View.GONE);
            } else {
                tvLocation.setText(location);
            }
            tvTitle.setText(item.getTitle());

            requestData(item.getContenttypeid() + "",item.getContentid() + "", false);
            Glide.with(DetailActivity.this).load(item.getFirstimage())
                    .placeholder(R.drawable.no_image_large)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imageView);

            findViewById(R.id.linear_naver).setOnClickListener(v -> {
                try {
                    String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + item.getTitle();
                    if (item.getContenttypeid() == 39) url += " " + item.getAddr1();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            findViewById(R.id.linear_tmap).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailActivity.this, MapActivity.class);
                    intent.putExtra(MapActivity.ITEM, item);
                    startActivity(intent);
                }
            });
        }
    }

    private void requestData(String contentTypeId, String contentId, boolean retry) {
        RetrofitManager.requestDetail(contentTypeId, contentId, new ResultListener() {
            @Override
            public void onSuccess(List<Item> items) {
                setData(items);
            }

            @Override
            public void onFail() {
                if (!retry) requestData(contentTypeId, contentId, true);
            }
        });
    }

    private void setData(List<Item> items) {
        if (items.size() > 0) {
            Item item = items.get(0);
            if (mTvOverview != null) mTvOverview.setText(Html.fromHtml(item.getOverview()));
        }
    }

}