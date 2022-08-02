package com.example.bot.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.bot.R;
import com.example.bot.model.Item;
import com.example.bot.retrofit.ResultListener;
import com.example.bot.retrofit.RetrofitManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CourseActivity extends AppCompatActivity {

    public static final String CATEGORY = "category";
    public static final String TITLE = "title";
    public static final String COLOR = "color";

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        findViewById(R.id.button_back).setOnClickListener(v -> {
            Intent intent = new Intent(CourseActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //FLAG_ACTIVITY_CLEAR_TOP : Activity Stack에서 MainActivity 위에 있는 Stack을 모두 제거.
            startActivity(intent);
            finish();
        });

        TextView tvTitle = findViewById(R.id.tv_title);
        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null) {
            String category = intent.getStringExtra(CATEGORY);
            String title = intent.getStringExtra(TITLE);
            int color = intent.getIntExtra(COLOR, 0xFFFA8072);

            findViewById(R.id.root).setBackgroundColor(color);
            tvTitle.setText(title);

            mAdapter = new Adapter(this, category);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setList(false);
            setProgress(true);
        }

        mSwipeLayout = findViewById(R.id.swipe_layout);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter != null) mAdapter.setList(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }
    }

    private void setProgress(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        if (mProgressBar != null) mProgressBar.setVisibility(visibility);
        if (!visible && mSwipeLayout != null) mSwipeLayout.setRefreshing(false);
    }

    private void onItemClicked(Item item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.ITEM, item);
        startActivity(intent);
    }

    static class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private final WeakReference<CourseActivity> mActivity; //메모리 누수 방지를 위해 WeakReference로 Activity 참조를 저장한다.
        private final String mCategory;
        private final ArrayList<Item> mItems = new ArrayList<>();

        public Adapter(CourseActivity activity, String category) {
            mActivity = new WeakReference<>(activity);
            mCategory = category;
            //Default Dummy Data 입력
            for (int i=0; i<30; i++) {
                mItems.add(new Item());
            }
        }

        private void removeProgress() {
            if (mActivity.get() != null) mActivity.get().setProgress(false);
        }

        void setList(boolean retry) {
            RetrofitManager.requestCourseList(mCategory, new ResultListener() {
                @Override
                public void onSuccess(List<Item> items) {
                    if (items != null && items.size() > 0) {
                        Collections.shuffle(items, new Random());
                        mItems.clear();
                        int size = Math.min(items.size(), 30);
                        for (int i=0; i<size; i++) {
                            mItems.add(items.get(i));
                        }
                        notifyDataSetChanged();
                    }
                    removeProgress();
                }

                @Override
                public void onFail() {
                    if (retry) {
                        removeProgress();
                    } else {
                        setList(true);
                    }
                }
            });
        }

        @Override
        @NonNull
        public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Adapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
            Item item = mItems.get(position);

            String title = (item.getTitle() == null) ? "" : (position + 1) + ". " + item.getTitle();
            holder.title.setText(title);

            String address = (item.getAddr1() == null) ? "" : item.getAddr1();
            holder.location.setText(address);

            Glide.with(holder.itemView.getContext()).load(item.getFirstimage2())
                    .placeholder(R.drawable.no_image) //이미지가 없을 경우 Default Image
                    .transition(DrawableTransitionOptions.withCrossFade()) //이미지 로딩 시 fade in(alpha 0 -> 1) animation
                    .centerCrop() //Image를 화면 비율을 유지한 채 ImageView 사이즈에 맞게 꽉 채운다.
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView location;
            ImageView imageView;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.tv_title);
                location = view.findViewById(R.id.tv_location);
                imageView = view.findViewById(R.id.iv_img);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item item = mItems.get(getAdapterPosition());
                        if (mActivity.get() != null) mActivity.get().onItemClicked(item);
                    }
                });
            }
        }
    }

}