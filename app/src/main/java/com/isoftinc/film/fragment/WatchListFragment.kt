package com.isoftinc.film.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.isoftinc.film.R
import com.isoftinc.film.adapter.MoviesAdapter
import com.isoftinc.film.databinding.FragmentWatchlistBinding
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.PaginationScrollListener
import com.isoftinc.film.util.PreferenceStore
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WatchListFragment : BaseFragment() {
    var binding :FragmentWatchlistBinding? = null
    var moviesAdapter :MoviesAdapter? = null
    var preferenceStore : PreferenceStore? = null
    var mobileVerificationResult : MobileVerificationResult? = null
    var isLastPagecheck: Boolean = false
    var isLoadingcheck: Boolean = false
    var pageNo = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWatchlistBinding.bind(inflater.inflate(R.layout.fragment_watchlist, container, false))

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.setImageResource(R.drawable.ic_baseline_close_24)
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        binding!!.toolbar.tvsubscribe.text = getText(R.string.watch_List)

        binding!!.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        preferenceStore = PreferenceStore(activity.applicationContext)

        val gridLayoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding!!.rvwatchList.layoutManager = gridLayoutManager
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 2 column size for first row
                return 1
            }
        }
       /* val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding!!.rvwatchList.layoutManager = layoutManager*/
        moviesAdapter = MoviesAdapter()
        binding!!.rvwatchList.adapter = moviesAdapter
        /*binding!!.rvwatchList.addOnScrollListener(object  : PaginationScrollListener(gridLayoutManager){
            override fun isLastPage(): Boolean {
             return  isLastPagecheck
            }

            override fun isLoading(): Boolean {

                return  isLoadingcheck
            }

            override fun loadMoreItems() {
              isLoadingcheck = true
                pageNo++
                watchList()

            }

        })*/

        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }

        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
        watchList()

        binding!!.ibDelete.setOnClickListener {
            binding!!.progressCircular.visibility = View.VISIBLE
            deleteHistory()
        }

    }


    private fun watchList() {
        try {
            val rootObject = WatchHistoryModel(mobileVerificationResult!!._id,pageNo)

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.watchHistory(rootObject)
            call.enqueue(object : Callback<WatchListModel> {
                override fun onResponse(call: Call<WatchListModel>, response: Response<WatchListModel>) {
                    val viewModel = response.body()!!
                    if(viewModel.code == 200){
                        try {
                            getMoreItems(viewModel.result)
                            binding!!.progressCircular.visibility = View.GONE
                        }catch (ex : Exception){

                        }


                    }



                }


                override fun onFailure(call: Call<WatchListModel>, t: Throwable) {
                    binding!!.progressCircular.visibility = View.GONE
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getMoreItems(list: ArrayList<VideoListModel>?) {
        //after fetching your data assuming you have fetched list in your
        // recyclerview adapter assuming your recyclerview adapter is
        //rvAdapter
        //  after getting your data you have to assign false to isLoading
        isLoadingcheck = false

        if (pageNo == 1) {

            moviesAdapter!!.setMoviesAdapter(activity, list)
        } else {

            moviesAdapter!!.addData(list)
        }


    }

    private fun deleteHistory() {
        try {
            val rootObject = UserGetModel(mobileVerificationResult!!._id)

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.deleteHistory(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(call: Call<SuccessModel>, response: Response<SuccessModel>) {
                    val viewModel = response.body()!!
                    if(viewModel.code == 200){
                        try {
                            moviesAdapter!!.setMoviesAdapter(activity,null)
                            binding!!.progressCircular.visibility = View.GONE
                        }catch (ex : Exception){

                        }

                    }



                }


                override fun onFailure(call: Call<SuccessModel>, t: Throwable) {
                    binding!!.progressCircular.visibility = View.GONE
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}