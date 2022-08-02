package com.example.bot.retrofit;

import com.example.bot.model.ResList;
import com.example.bot.model.ResDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Retrofit GET Method를 정의하는 interface
public interface TourApi {

    String SERVICE_KEY = "8P%2Bw9z7sqRrPQzYNBaJksrOM5UoosZ95Rw2nf770fDXTKNM7fINur9o6mgmJHfUXAw9Loa9xod2peneG51esFA%3D%3D";
    String OPTIONS_AREA = "&listYN=Y&MobileOS=AND&MobileApp=BOT&arrange=A&pageNo=1";
    String OPTIONS_COURSE = "&contentTypeId=25&areaCode=&sigunguCode=&cat1=C01&cat3=&listYN=Y&MobileOS=AND&MobileApp=BOT&arrange=A&pageNo=1";
    String OPTIONS_LOCATION = "&radius=2000&listYN=Y&MobileOS=AND&MobileApp=BOT&arrange=A&pageNo=1";
    String OPTIONS_DETAIL = "&MobileOS=AND&MobileApp=BOT&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y";

    //지역명으로 검색
    @GET("areaBasedList?ServiceKey=" + SERVICE_KEY + OPTIONS_AREA)
    Call<ResList> getAreaBasedList(
            @Query("contentTypeId") String contentTypeId,
            @Query("areaCode") String areaCode,
            @Query("numOfRows") String numOfRows,
            @Query("_type") String type
    );

    //http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=인증키
    //&contentTypeId=25&areaCode=&sigunguCode=&cat1=C01&cat2=C0117&cat3=&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=12&pageNo=1
    //여행 코스 검색
    @GET("areaBasedList?ServiceKey=" + SERVICE_KEY + OPTIONS_COURSE)
    Call<ResList> getCourseList(
            @Query("cat2") String category,
            @Query("numOfRows") String numOfRows,
            @Query("_type") String type
    );

    //http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=인증키&contentTypeId=12&mapX=126.981106&mapY=37.568477
    // &radius=2000&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=12&pageNo=1
    //가까운 지역 검색
    @GET("locationBasedList?ServiceKey=" + SERVICE_KEY + OPTIONS_LOCATION)
    Call<ResList> getLocationBasedList(
            @Query("contentTypeId") String contentTypeId,
            @Query("mapX") String longitude,
            @Query("mapY") String latitude,
            @Query("numOfRows") String numOfRows,
            @Query("_type") String type
    );

    //아이템 상세 검색
    @GET("detailCommon?ServiceKey=" + SERVICE_KEY + OPTIONS_DETAIL)
    Call<ResDetail> getDetail(
            @Query("contentTypeId") String contentTypeId,
            @Query("contentId") String contentId,
            @Query("_type") String type
    );

}
