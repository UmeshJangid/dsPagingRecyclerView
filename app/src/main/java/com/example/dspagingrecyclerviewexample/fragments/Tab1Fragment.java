package com.example.dspagingrecyclerviewexample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dspagingrecyclerviewexample.R;
import com.example.dspagingrecyclerviewexample.apicall.ApiInterface;
import com.example.dspagingrecyclerviewexample.apicall.ApiProduction;
import com.example.dspagingrecyclerviewexample.apicall.reactivex.RxAPICallHelper;
import com.example.dspagingrecyclerviewexample.apicall.reactivex.RxAPICallback;
import com.example.dspagingrecyclerviewexample.model.PopularMovieResponse;
import com.example.dspagingrecyclerviewexample.utility.Utils;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class Tab1Fragment extends Fragment {
    private Disposable disposable = null;
    private static final String TAG = "Tab1Fragment";
    private int PAGECOUNT = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Call API to get data from Server and Then set to Recyclerview.
        if (Utils.isNetworkConnectedAvailable(getContext())) {
            getUserBadgeDetails();
        } else {
            Utils.showToast(getContext(), "No Internet");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one, container, false);
    }


    /**
     * Call API to get stackoverflow User badge details
     */
    private void getUserBadgeDetails() {
        Utils.showDialog(getContext());
        ApiInterface badgesService = ApiProduction.getInstance(getContext()).provideService(ApiInterface.class);
        Observable<PopularMovieResponse> responseObservable = badgesService.getPopularMovies(ApiProduction.APIKEY, ApiProduction.language, PAGECOUNT + "");
        disposable = RxAPICallHelper.call(responseObservable, new RxAPICallback<PopularMovieResponse>() {
            @Override
            public void onSuccess(PopularMovieResponse badgesResponse) {
                Utils.hideDialog();
               /* Utils.showToast(badgesResponse.getItems().size() > 0 ? "Success" : "Failed");
                if (badgesResponse.getItems().size() > 0) {
                    calculateBadges(badgesResponse);
                }*/
                disposeCall();

            }

            @Override
            public void onFailed(Throwable throwable) {
                disposeCall();
                Utils.hideDialog();
                Log.d(TAG, "onFailed: " + throwable.getLocalizedMessage());
            }
        });
    }

    /**
     * After Called API, dispose call after success or failure.
     */
    private void disposeCall() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}