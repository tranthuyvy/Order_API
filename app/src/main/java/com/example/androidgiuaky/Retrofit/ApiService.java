package com.example.androidgiuaky.Retrofit;

import com.example.androidgiuaky.Model.Order;
import com.example.androidgiuaky.Model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface ApiService {

    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxdWFuZ2h1bmciLCJpYXQiOjE2ODQ1NjM0NjMsImV4cCI6MTY4NDY0OTg2M30.hQpHiH_SpFs7eSXCKyjBrvEf4dtFl5AZTFpyI-_MAGCxVXhCTJdqCHun1muAp6IzsL2ySCbgN0rMAa7IBNs9jw";
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS)
            .addInterceptor(new TokenInterceptor(token))
            .build();
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    // client -> setTime gọi API,
//https://d19cqcnpm01-api.azurewebsites.net/
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://d19cqcnpm01-api.azurewebsites.net/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("api/orders")
    Call<List<Order>> getOrdersfromUserId(@Query("userId") int userId );
    @PUT("api/orders/{orderId}")
    Call<Order> PutOrderByOrderID(@Path("orderId") int orderId, @Body Order order );

    @GET("api/products")
    Call<List<Product>> productListData();

}
