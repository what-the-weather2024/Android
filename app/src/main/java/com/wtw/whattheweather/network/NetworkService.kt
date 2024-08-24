package com.wtw.whattheweather.network

import com.wtw.whattheweather.model.FeedItemData
import com.wtw.whattheweather.model.UmbrellaItemData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface NetworkService {

    @GET("api/feed")
    suspend fun getFeedList(

    ) : Response<List<FeedItemData>>

    @GET("api/feed")
    suspend fun getSearchAddressFeedList(
        @Query ("city") city: String,
        @Query ("district") district: String
    ) : Response<List<FeedItemData>>

    @GET("/api/umbrella")
    suspend fun getUmbrellaAddressList(

    ) : Response<List<UmbrellaItemData>>

    @GET("")
    suspend fun getWeatherInfo(
        @Query ("city") city: String,
        @Query ("district") district: String
    ) : Response<FeedItemData>

    @Multipart
    @POST("api/feed")
    suspend fun imageUpload(
        @Part file: MultipartBody.Part,
        @Part ("city") city : RequestBody?,
        @Part ("district") district : RequestBody?,
        @Part ("memberId") memberId : RequestBody?
    ) : Response<FeedItemData>

}