package com.example.bot.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bot.model.BodyList;
import com.example.bot.model.BodyDetail;
import com.example.bot.model.Item;
import com.example.bot.model.ResList;
import com.example.bot.model.ResDetail;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final String[] AREA_NAME = { "서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종",
            "경기", "강원", "충북", "충남", "경북" , "경남", "전북", "전남", "제주" };
    private static final String[] AREA_CODE = { "1", "2", "3", "4", "5", "6", "7", "8",
            "31", "32", "33", "34", "35", "36", "37", "38", "39" };

    private static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    private static TourApi mApi;

    public static TourApi getApi() {
        if (mApi == null) {

            //Log Message 추가 (로그화면에서 "D/OkHttp"로 필터링)
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            //Log Message 추가

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .client(client)//Log Message 추가
                    .addConverterFactory(GsonConverterFactory.create()) //JSON -> POJO 변환
                    .build();
            mApi = retrofit.create(TourApi.class);
        }
        return mApi;
    }

    public static void requestAreaBasedList(String contentTypeId, String city, ResultListener listener) {
        Call<ResList> call = getApi().getAreaBasedList(contentTypeId, getAreaCode(city), "100", "json");
        call.enqueue(new Callback<ResList>() {
            @Override
            public void onResponse(@NonNull Call<ResList> call, @NonNull retrofit2.Response<ResList> response) {
                if (response.code() == 200 && response.body() != null) {
                    BodyList body = response.body().getResponse().getBody();
                    if (body != null) {
                        listener.onSuccess(body.getItems().getItem());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResList> call, Throwable t) {
                listener.onFail();
            }
        });
    }

    public static void requestLocationBasedList(String contentTypeId, String latitude, String longitude, ResultListener listener) {
        Call<ResList> call = getApi().getLocationBasedList(contentTypeId, longitude, latitude, "100", "json");
        call.enqueue(new Callback<ResList>() {
            @Override
            public void onResponse(@NonNull Call<ResList> call, @NonNull retrofit2.Response<ResList> response) {
                if (response.code() == 200 && response.body() != null) {
                    BodyList body = response.body().getResponse().getBody();
                    if (body != null) {
                        listener.onSuccess(body.getItems().getItem());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResList> call, Throwable t) {
                listener.onFail();
            }
        });
    }

    public static void requestDetail(String contentTypeId, String contentId, ResultListener listener) {
        Call<ResDetail> call = getApi().getDetail(contentTypeId, contentId, "json");
        call.enqueue(new Callback<ResDetail>() {
            @Override
            public void onResponse(@NonNull Call<ResDetail> call, @NonNull retrofit2.Response<ResDetail> response) {
                if (response.code() == 200 && response.body() != null) {
                    BodyDetail body = response.body().getResponse().getBody();
                    if (body != null) {
                        ArrayList<Item> items = new ArrayList<>();
                        items.add(body.getItems().getItem());
                        listener.onSuccess(items);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResDetail> call, Throwable t) {
                listener.onFail();
            }
        });
    }

    public static void requestCourseList(String category, ResultListener listener) {
        Call<ResList> call = getApi().getCourseList(category,"100", "json");
        call.enqueue(new Callback<ResList>() {
            @Override
            public void onResponse(@NonNull Call<ResList> call, @NonNull retrofit2.Response<ResList> response) {
                if (response.code() == 200 && response.body() != null) {
                    BodyList body = response.body().getResponse().getBody();
                    if (body != null) {
                        listener.onSuccess(body.getItems().getItem());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResList> call, Throwable t) {
                listener.onFail();
            }
        });
    }

    private static String getAreaCode(String city) {
        int index = 0;
        for (int i = 0; i < AREA_NAME.length; i++) {
            if (AREA_NAME[i].equals(city)) index = i;
        }
        return AREA_CODE[index];
    }

}
