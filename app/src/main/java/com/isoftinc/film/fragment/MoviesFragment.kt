package com.isoftinc.film.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.isoftinc.film.R
import com.isoftinc.film.adapter.MoviesVerticalAdapter
import com.isoftinc.film.databinding.FragmentHomeBinding
import com.isoftinc.film.model.HomeModel
import com.isoftinc.film.model.HomeRequestModel
import com.isoftinc.film.model.MobileVerificationResult
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.PreferenceStore
import com.google.gson.Gson
import com.isoftinc.film.activity.MainActivity
import com.isoftinc.film.dialog.SubscriptionBottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesFragment : BaseFragment() {
    var binding : FragmentHomeBinding? = null
    var userIdd =""
    var preferenceStore : PreferenceStore? = null
    var mobileVerificationResult : MobileVerificationResult? = null
    var moviesAdapter : MoviesVerticalAdapter? = null
    var subId : String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))

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



        preferenceStore  = PreferenceStore(activity.applicationContext)
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

        binding!!.toolbar.tvsubscribe.setOnClickListener {
            openSubscriptionDialog()
        }
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding!!.verticalRecycler.layoutManager = layoutManager
        moviesAdapter = MoviesVerticalAdapter()
        binding!!.verticalRecycler.adapter = moviesAdapter
        moviesList()

    }


    private fun moviesList() {

        try {

            val  rootObject = HomeRequestModel(userIdd)


            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.getMovieList(rootObject)
            call.enqueue(object : Callback<HomeModel> {
                override fun onResponse(call: Call<HomeModel>, response: Response<HomeModel>) {
                    val homeModel = response.body()!!
                    if(homeModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            moviesAdapter!!.setMoviesList(activity,homeModel.result.sub_category_video)

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