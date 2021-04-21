package com.example.dspagingrecyclerviewexample.apicall;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Project : DSPagingRecyclerViewExample
 * Created by Umesh Jangid on 20,April,2021
 * Dotsquares Limited,
 * Jaipur Rajasthan, India.
 */

public class ApiProduction {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String APIKEY = "3c6ce42f2bcf39ccf5b2cab79ab6279f";
    public static final String language = "en-US";
    private static final String page = "page";

    private Context context;

    private ApiProduction(Context context) {
        this.context = context;
    }

    public static ApiProduction getInstance(Context context) {
        return new ApiProduction(context);
    }

    private Retrofit provideRestAdapter() {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpProduction.getOkHttpClient(context, true))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <S> S provideService(Class<S> serviceClass) {
        return provideRestAdapter().create(serviceClass);
    }
}