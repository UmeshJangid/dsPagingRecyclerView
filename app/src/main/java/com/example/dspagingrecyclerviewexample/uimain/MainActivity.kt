package com.example.dspagingrecyclerviewexample.uimain

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.dspagingrecyclerviewexample.R
import com.example.dspagingrecyclerviewexample.adapter.TabAdapter
import com.example.dspagingrecyclerviewexample.databinding.ActivityMainBinding
import com.example.dspagingrecyclerviewexample.fragments.Tab1Fragment
import com.example.dspagingrecyclerviewexample.fragments.Tab2Fragment
import com.example.dspagingrecyclerviewexample.fragments.Tab3Fragment
import com.example.dspagingrecyclerviewexample.model.StackOverFlowUserBadgesResponse
import io.reactivex.disposables.Disposable


class MainActivity : AppCompatActivity() {
    private var bronze_count = 0
    private var silver_count = 0
    private var gold_count = 0
    private var mProgressDialog: ProgressDialog? = null
    private var disposable: Disposable? = null
    private var adapter: TabAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        //ButterKnife.bind(this)
       // setSupportActionBar(binding.toolbar)
        mProgressDialog = ProgressDialog(this)
        adapter = TabAdapter(supportFragmentManager)
        adapter!!.addFragment(Tab1Fragment(), "Latest")
        adapter!!.addFragment(Tab2Fragment(), "Popular")
        adapter!!.addFragment(Tab3Fragment(), "Upcoming")
        // adapter!!.addFragment(Tab2Fragment(), "Tab 2")
        // adapter!!.addFragment(Tab3Fragment(), "Tab 3")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        /* mProgressDialog!!.setMessage(getString(R.string.str_progress_message))
         if (isInternetAvailable) {
             userBadgeDetails
         } else {
             showToast(getString(R.string.no_internet_connection))
         }*/
    }

    /**
     * Call API to get stackoverflow User badge details
     */
    /*  private val userBadgeDetails: Unit
          private get() {
              mProgressDialog!!.show()
              val badgesService: StackOverFlowUserBadgesService =
                  ApiProduction.getInstance(this).provideService(
                      StackOverFlowUserBadgesService::class.java
                  )
              val responseObservable: Observable<StackOverFlowUserBadgesResponse> =
                  badgesService.getBadges("2949612")
              disposable = RxAPICallHelper.call(
                  responseObservable,
                  object : RxAPICallback<StackOverFlowUserBadgesResponse?>() {
                      fun onSuccess(badgesResponse: StackOverFlowUserBadgesResponse) {
                          mProgressDialog!!.dismiss()
                          showToast(if (badgesResponse.getItems().size() > 0) "Success" else "Failed")
                          if (badgesResponse.getItems().size() > 0) {
                              calculateBadges(badgesResponse)
                          }
                          disposeCall()
                      }

                      fun onFailed(throwable: Throwable) {
                          disposeCall()
                          mProgressDialog!!.dismiss()
                          showToast(throwable.localizedMessage)
                      }
                  })
          }
  */
    /**
     * After Called API, dispose call after success or failure.
     */
    private fun disposeCall() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    /**
     * Calculate user badges as per aPI response
     *
     * @param badgesResponse : use badge response
     */
    private fun calculateBadges(badgesResponse: StackOverFlowUserBadgesResponse) {
        for (badge in badgesResponse.getItems()) {
            when (badge.getRank()) {
                "bronze" -> bronze_count = bronze_count + badge.getAwardCount()
                "silver" -> silver_count = silver_count + badge.getAwardCount()
                "gold" -> gold_count = gold_count + badge.getAwardCount()
            }
        }


        /*   // Set Badge Counts in TextView
           tvBronzeBadge.setText(
               getStringFromHtmlText(
                   getString(
                       R.string.str_bronze_badge_text,
                       bronze_count.toString()
                   )
               )
           )
           tvSilverBadge.setText(
               getStringFromHtmlText(
                   getString(
                       R.string.str_silver_badge_text,
                       silver_count.toString()
                   )
               )
           )
           tvGoldBadge.setText(
               getStringFromHtmlText(
                   getString(
                       R.string.str_gold_badge_text,
                       gold_count.toString()
                   )
               )
           )*/
    }

    /**
     * Show toast message
     *
     * @param message : toast message string
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Check internet connection is available
     *
     * @return : true if internet connection is available else false
     */
    val isInternetAvailable: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return (netInfo != null && netInfo.isConnectedOrConnecting
                    && cm.activeNetworkInfo!!.isAvailable
                    && cm.activeNetworkInfo!!.isConnected)
        }

    fun getStringFromHtmlText(baseString: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(baseString, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(baseString)
        }
    }
}