package com.isoftinc.film.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.isoftinc.film.R
import com.isoftinc.film.activity.MainActivity
import com.isoftinc.film.adapter.HomeAdapter
import com.isoftinc.film.databinding.FragmentHomeBinding
import com.isoftinc.film.dialog.SubscriptionBottomSheetDialog
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.PreferenceStore
import com.isoftinc.film.util.ShowingImage
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : BaseFragment() {
    var binding : FragmentHomeBinding? = null
    val service: ApiClient? = ApiClient()
    var homeAdapter : HomeAdapter? = null
    var preferenceStore : PreferenceStore? = null
    var mobileVerificationResult : MobileVerificationResult? = null
    var subId : String? = null
    var userIdd =""

    companion object{
        var bottmNavigation : BottomNavigationView? = null
        var isFirstTime = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        preferenceStore  = PreferenceStore(activity.applicationContext)


        return binding!!.root


    }

    override fun onResume() {
        super.onResume()

        if (bottomNavigationView != null) {
            bottmNavigation = bottomNavigationView
        }

        bottmNavigation!!.visibility = View.VISIBLE
        bottmNavigation!!.selectedItemId = R.id.home
        if(sideMenu != null){
            val gson = Gson()
            val json = preferenceStore!!.getUserDetail()
            mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
            val headerView: View =  sideMenu!!.getHeaderView(0)
            headerView.setOnClickListener {
                if(mobileVerificationResult != null){
                    activity.replaceFragment(ProfileFragment())
                    drawerLayout!!.closeDrawers()
                }

            }
            val mobileNo = headerView.findViewById<TextView>(R.id.mobileNo)
            val username = headerView.findViewById<TextView>(R.id.username)
            val ivView = headerView.findViewById<CircleImageView>(R.id.circulariv)
            if(mobileVerificationResult != null){
                mobileNo.visibility = View.VISIBLE
                mobileNo.text = mobileVerificationResult!!.phone_number
                username.text = mobileVerificationResult!!.username
                ShowingImage.showProfileImage(activity, mobileVerificationResult!!.profile_img,ivView)
            }
            else
                username.text = "Guest User"
            ShowingImage.showProfileImage(activity, "",ivView)
                mobileNo.visibility = View.GONE
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



            binding!!.toolbar.skip.visibility = View.GONE
            binding!!.toolbar.ibSearch.visibility = View.VISIBLE
            binding!!.toolbar.ibBack.visibility = View.GONE
            binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
            binding!!.toolbar.tvsubscribe.text = getText(R.string.subscribe)


        binding!!.toolbar.tvsubscribe.setOnClickListener {
          openSubscriptionDialog()
        }


        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }

            val layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            binding!!.verticalRecycler.layoutManager = layoutManager
            homeAdapter = HomeAdapter()
            binding!!.verticalRecycler.adapter = homeAdapter




        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
        if(mobileVerificationResult != null &&  mobileVerificationResult!!.subscription != null){
            binding!!.toolbar.tvsubscribe.visibility = View.GONE

        }else{
            binding!!.toolbar.tvsubscribe.visibility = View.VISIBLE
        }
        if(mobileVerificationResult != null){
            userIdd = mobileVerificationResult!!._id
        }
        homeData()



    }

    private fun homeData() {

        try {

           val  rootObject = HomeRequestModel(userIdd)


            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getHomeData(rootObject)
            call.enqueue(object : Callback<HomeModel> {
                override fun onResponse(call: Call<HomeModel>, response: Response<HomeModel>) {
                    val homeModel = response.body()!!
                    if(homeModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            homeAdapter!!.setHomeList(activity,homeModel.result,null,null,null,null,null)
                            Handler().postDelayed({
                                latest(homeModel.result)
                            },100)
                        }catch (ex : Exception){

                        }






                    }



                }


                override fun onFailure(call: Call<HomeModel>, t: Throwable) {
                    binding!!.progressCircular.visibility = View.GONE
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }



    private fun continueWatching(homeResultModel: HomeResultModel, latestList: ArrayList<VideoModel>?, trending:ArrayList<VideoModel>?,
                                 SeriesList: ArrayList<SeriesListsModels>?,ShowsList: ArrayList<SeriesListsModels>?) {

        try {

         val    rootObject = UserGetModel(mobileVerificationResult!!._id)


            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.continueWatching(rootObject)
            call.enqueue(object : Callback<ContinueResponseModel> {
                override fun onResponse(call: Call<ContinueResponseModel>, response: Response<ContinueResponseModel>) {
                    val homeModel = response.body()!!
                    if(homeModel.code == 200){
                        try {
                            homeAdapter!!.setHomeList(activity,homeResultModel,homeModel.result,latestList,trending,SeriesList,ShowsList)


                        }catch (ex : Exception){

                        }


                    }



                }


                override fun onFailure(call: Call<ContinueResponseModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun latest(homeResultModel: HomeResultModel) {

        try {

            val  rootObject = UserGetModel(userIdd)


            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getLatest(rootObject)
            call.enqueue(object : Callback<ContinueResponseModel> {
                override fun onResponse(call: Call<ContinueResponseModel>, response: Response<ContinueResponseModel>) {
                    val homeModel = response.body()!!
                    if(homeModel.code == 200){
                        try {
                            homeAdapter!!.setHomeList(activity,homeResultModel,null,homeModel.result,null,null,null)
                            Handler().postDelayed({
                                trending(homeResultModel, homeModel.result)
                            },100)
                        }catch (ex : Exception){

                        }

                    }else{
                        Handler().postDelayed({
                            trending(homeResultModel, homeModel.result)
                        },100)
                    }



                }


                override fun onFailure(call: Call<ContinueResponseModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun trending(homeResultModel: HomeResultModel, latestList : ArrayList<VideoModel>?) {

        try {

            val  rootObject = UserGetModel(userIdd)
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.getTrending(rootObject)
            call.enqueue(object : Callback<ContinueResponseModel> {
                override fun onResponse(call: Call<ContinueResponseModel>, response: Response<ContinueResponseModel>) {
                    val homeModel = response.body()!!
                    if(homeModel.code == 200){
                        try {
                            homeAdapter!!.setHomeList(activity,homeResultModel,null,latestList,homeModel.result,null,null)
                           /* Handler().postDelayed({
                                continueWatching(homeResultModel, latestList,homeModel.result)
                            },100)*/

                            Handler().postDelayed({
                                getSeriesList(homeResultModel,latestList,homeModel.result)
                            },100)
                        }catch (ex : Exception){

                        }

                    }



                }


                override fun onFailure(call: Call<ContinueResponseModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun getSeriesList(homeResultModel: HomeResultModel,latestList: ArrayList<VideoModel>?, trending:ArrayList<VideoModel>?) {
        try {

            val rootObject = SeriesHomeRequestModel(userIdd,  "SE","1")

            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getSeriesHomeList(rootObject)
            call.enqueue(object : Callback<SeriesModelListResponse> {
                override fun onResponse(
                    call: Call<SeriesModelListResponse>,
                    response: Response<SeriesModelListResponse>
                ) {
                    val seriesResponse = response.body()!!
                    if (seriesResponse.code == 200) {
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            homeAdapter!!.setHomeList(activity,homeResultModel,null,latestList,trending, seriesResponse.result.series_list,null)
                            Handler().postDelayed({
                                continueWatching(homeResultModel, latestList,trending,seriesResponse.result.series_list,null)
                            },100)

                        } catch (ex: Exception) {

                        }
                    }else{
                        Handler().postDelayed({
                            continueWatching(homeResultModel, latestList,trending,seriesResponse.result.series_list,null)
                        },100)
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

    private fun getShowsList(homeResultModel: HomeResultModel,latestList: ArrayList<VideoModel>?, trending:ArrayList<VideoModel>?, SeriesList: ArrayList<SeriesListsModels>?) {
        try {

            val rootObject = SeriesRequestModel(userIdd,  "SH")

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
                            homeAdapter!!.setHomeList(activity,homeResultModel,null,latestList,trending, SeriesList,seriesResponse.result.series_list)
                            Handler().postDelayed({
                                continueWatching(homeResultModel, latestList,trending,SeriesList,seriesResponse.result.series_list)
                            },100)




                        } catch (ex: Exception) {

                        }
                    }else{
                        Handler().postDelayed({
                            continueWatching(homeResultModel, latestList,trending,SeriesList,seriesResponse.result.series_list)
                        },100)
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