package com.example.dspagingrecyclerviewexample.apicall.reactivex;

/**
 * Created by Pranay.
 * <p>
 * Common Callback to call API request response.
 *
 * @param <P> : response Type
 */
public interface RxAPICallback<P> {
    void onSuccess(P t);

    void onFailed(Throwable throwable);
}