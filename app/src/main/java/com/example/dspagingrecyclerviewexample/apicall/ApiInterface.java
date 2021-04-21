package com.example.dspagingrecyclerviewexample.apicall;


import com.example.dspagingrecyclerviewexample.model.PopularMovieResponse;
import com.example.dspagingrecyclerviewexample.model.StackOverFlowUserBadgesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Project : DSPagingRecyclerViewExample
 * Created by Umesh Jangid on 21,April,2021
 * Dotsquares Limited,
 * Jaipur Rajasthan, India.
 */

public interface ApiInterface {
    //get User Badges
    @GET("users/{userId}/badges?page=1&pagesize=80&order=desc&sort=type&site=stackoverflow")
    Observable<StackOverFlowUserBadgesResponse> getBadges(@Path("userId") String userId);

    /*     @GET("Search") //i.e https://api.test.com/Search?
      Call<Products> getProducts(@Query("one") String one, @Query("two") String two,
                                @Query("key") String key)*/
    //?api_key={apiKey}&language=en-US&page={page}
    @GET("movie/popular")
    Observable<PopularMovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") String page);
}