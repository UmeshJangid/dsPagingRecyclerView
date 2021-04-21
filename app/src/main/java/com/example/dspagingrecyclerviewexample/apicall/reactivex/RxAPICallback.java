package com.example.dspagingrecyclerviewexample.apicall.reactivex;

/**
 * Project : DSPagingRecyclerViewExample
 * Created by Umesh Jangid on 21,April,2021
 * Dotsquares Limited,
 * Jaipur Rajasthan, India.
 */
public interface RxAPICallback<P> {
    void onSuccess(P t);
    void onFailed(Throwable throwable);
}