package com.isoftinc.film.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.isoftinc.film.R
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.activity.MainActivity
import com.isoftinc.film.adapter.MoviesAdapter
import com.isoftinc.film.adapter.MoviesVerticalAdapter
import com.isoftinc.film.adapter.SeriesAdapter
import com.isoftinc.film.databinding.FragmentHomeBinding
import com.isoftinc.film.databinding.FragmentViewAllBinding
import com.isoftinc.film.dialog.SubscriptionBottomSheetDialog
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.PreferenceStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeriesFragment : BaseFragment() {
    var binding: FragmentViewAllBinding? = null
    val service: ApiClient? = ApiClient()
    var subCategoryId = ""
    var seriesAdapter: SeriesAdapter? = null
    var preferenceStore: PreferenceStore? = null
    var mobileVerificationResult: MobileVerificationResult? = null
    var userIdd = ""
    var isForShows = false
    var subId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewAllBinding.bind(
            inflater.inflate(
                R.layout.fragment_view_all,
                container,
                false
            )
        )

        if (bottomNavigationView != null) {
            bottomNavigationView!!.visibility = View.VISIBLE
            bottomNavigationView!!.selectedItemId = R.id.home
        }
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.GONE
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        binding!!.toolbar.tvsubscribe.text = getText(R.string.subscribe)
        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }

        preferenceStore = PreferenceStore(activity.applicationContext)
        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
        if (mobileVerificationResult != null && mobileVerificationResult!!.subscription != null) {
            binding!!.toolbar.tvsubscribe.visibility = View.GONE
        } else {
            binding!!.toolbar.tvsubscribe.visibility = View.VISIBLE
        }
        if (mobileVerificationResult != null) {
            userIdd = mobileVerificationResult!!._id
        }

        binding!!.toolbar.tvsubscribe.setOnClickListener {
            openSubscriptionDialog()
        }

        getSeriesList()
        val gridLayoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding!!.rvViewAll.layoutManager = gridLayoutManager
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 2 column size for first row
                return 1
            }
        }
        seriesAdapter = SeriesAdapter()
        binding!!.rvViewAll.adapter = seriesAdapter

    }


    private fun getSeriesList() {
        try {

            val rootObject = SeriesRequestModel(userIdd, if (isForShows) "SH" else "SE")

            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getSeriesList(rootObject)
            call.enqueue(object : Callback<SeriesModelListResponse> {
                override fun onResponse(
                    call: Call<SeriesModelListResponse>,
                    response: Response<SeriesModelListResponse>
                ) {
                    val seriesResponse = response.body()!!
                    if (seriesResponse.code == 200) {
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            seriesAdapter!!.setSeriesAdapter(
                                activity,
                                seriesResponse.result.series_list
                            )

                        } catch (ex: Exception) {

                        }
                    }


                }


                override fun onFailure(call: Call<SeriesModelListResponse>, t: Throwable) {
                    Log.e("responseError", t.message)
                    binding!!.progressCircular.visibility = View.GONE
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    companion object {

        fun newInstance(isForShows: Boolean?) = SeriesFragment().apply {
            this.isForShows = isForShows!!


        }
    }
    fun openSubscriptionDialog(){
        val subscriptionBottomSheetDialog = SubscriptionBottomSheetDialog.newInstance(activity,"")
        subscriptionBottomSheetDialog.setListner(object  : SubscriptionBottomSheetDialog.OnListener{
            override fun accept(price: String, planId : String) {
                subscriptionBottomSheetDialog.dismiss()
                binding!!.toolbar.tvsubscribe.visibility = View.GONE
                subId = planId
                MainActivity().startPayment(price,activity,subId,false)
            }

            override fun navigate() {
                activity.replaceFragment(RegisterFragment.newInstance(true))
            }

        })
        subscriptionBottomSheetDialog.show(activity.supportFragmentManager, SubscriptionBottomSheetDialog().TAG)
    }
}