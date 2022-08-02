package com.example.bot.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.bot.R;
import com.example.bot.model.Item;
import com.example.bot.retrofit.ResultListener;
import com.example.bot.retrofit.RetrofitManager;
import com.example.bot.ui.activities.DetailActivity;
import com.example.bot.ui.activities.MainActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hugeterry(http://hugeterry.cn)
 */
public class MainFragment extends Fragment {
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private static final String CONTENT_TYPE_ID = "content_type_id";
    private String mContentTypeId;

    public static MainFragment getInstance(String contentTypeId) {
        MainFragment fra = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_TYPE_ID, contentTypeId);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_fragment, container, false);

        if (getArguments() != null) mContentTypeId = getArguments().getString(CONTENT_TYPE_ID);

        mProgressBar = v.findViewById(R.id.progress_bar);
        mRecyclerView = v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter(this, mContentTypeId);
        mRecyclerView.setAdapter(mAdapter);

        String[] cities = getResources().getStringArray(R.array.city_item);
        Spinner spinner = v.findViewById(R.id.spinner_city);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = cities[position];
                if (mAdapter != null) mAdapter.setList(city, false);
                if (mRecyclerView != null) mRecyclerView.scrollToPosition(0);
                setProgress(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        v.findViewById(R.id.text_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null); // 메모리 누수 방지를 위해 어댑터를 제거한다.
            mRecyclerView = null;
        }
    }

    private void setProgress(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        if (mProgressBar != null) mProgressBar.setVisibility(visibility);
    }

    private void getLocation() {
        if (getActivity() instanceof MainActivity) {
            double[] location = ((MainActivity) getActivity()).getLocation();
            if (location != null) {
                if (location[0] == 0 || location[1] == 0) { //GPS를 방금 켠 경우 위치를 가져오는데, 딜레이가 발생함.
                    Toast.makeText(getContext(), "위치를 가져오는데, 실패하였습니다. 잠시 후 다시 시도하세요.", Toast.LENGTH_LONG).show();
                } else {
                    if (mAdapter != null) mAdapter.setListNearby(location, false);
                    setProgress(true);
                }
            }
        }
    }

    void onItemClicked(Item item) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.ITEM, item);
        startActivity(intent);
    }

    static class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        private final WeakReference<MainFragment> mFragment;
        private final String mContentTypeId;
        private final ArrayList<Item> mItems = new ArrayList<>();

        public Adapter(MainFragment fragment, String contentTypeId) {
            mFragment = new WeakReference<>(fragment);
            mContentTypeId = contentTypeId;
        }

        private void removeProgress() {
            if (mFragment.get() != null) mFragment.get().setProgress(false);
        }

        //주변 검색
        void setListNearby(double[] location, boolean retry) {
            RetrofitManager.requestLocationBasedList(mContentTypeId, location[0] + "", location[1] + "", new ResultListener() {
                @Override
                public void onSuccess(List<Item> items) {
                    if (items != null) {
                        mItems.clear();
                        mItems.addAll(items);
                        notifyDataSetChanged();
                    }
                    removeProgress();
                }

                @Override
                public void onFail() {
                    if (retry) { //두번 요청했으나 실패하였으므로 프로그래스 바만 제거한다.
                        removeProgress();
                    } else {
                        setListNearby(location, true); //Network Error 발생 시 한 번더 시도.
                    }
                }
            });
        }

        //지역 검색
        void setList(String city, boolean retry) {
            RetrofitManager.requestAreaBasedList(mContentTypeId, city, new ResultListener() {
                @Override
                public void onSuccess(List<Item> items) {
                    if (items != null) {
                        mItems.clear();
                        mItems.addAll(items);
                        notifyDataSetChanged();
                    }
                    removeProgress();
                }

                @Override
                public void onFail() {
                    if (retry) {
                        removeProgress();
                    } else {
                        setList(city, true);
                    }
                }
            });
        }

        @Override
        @NonNull
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Item item = mItems.get(position);
            String title = (position + 1) + ". " + item.getTitle();
            holder.title.setText(title);
            holder.location.setText(item.getAddr1());
            Glide.with(holder.itemView.getContext()).load(item.getFirstimage2())
                    .placeholder(R.drawable.no_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
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
                        if (mFragment.get() != null) mFragment.get().onItemClicked(item);
                    }
                });
            }
        }
    }

}

