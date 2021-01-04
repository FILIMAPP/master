package com.isoftinc.film.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.isoftinc.film.R
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.databinding.FragmentContactUsBinding
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.AndroidUtilities
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.PreferenceStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsFragment: BaseFragment() {
    var binding : FragmentContactUsBinding? = null
    var    mobileVerificationResult : MobileVerificationResult? = null
    var preferenceStore : PreferenceStore? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactUsBinding.bind(inflater.inflate(R.layout.fragment_contact_us, container, false))
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.setImageResource(R.drawable.ic_baseline_close_24)
        binding!!.toolbar.tvsubscribe.visibility = View.GONE
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        preferenceStore = PreferenceStore(activity.applicationContext)

        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)

        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }

        binding!!.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.send.setOnClickListener {
            if(isValidate()){
                binding!!.progressCircular.visibility = View.VISIBLE
                feedback()
            }
        }


    }

    fun isValidate():Boolean{
        if(binding!!.name.text.toString().trim().isEmpty()){
            Toast.makeText(activity,"Please add name",Toast.LENGTH_SHORT).show()
            return false
        }else if(binding!!.email.text.toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding!!.email.text.toString().trim()).matches()){
            Toast.makeText(activity, "Please add email",Toast.LENGTH_SHORT).show()
            return  false
        }else if(binding!!.mobile.text.toString().trim().isEmpty() || binding!!.mobile.text?.length != 10){
            Toast.makeText(activity, "Please add mobile",Toast.LENGTH_SHORT).show()
            return  false
        }else  if(binding!!.message.text.toString().trim().isEmpty()){
            Toast.makeText(activity, "Please give feedback",Toast.LENGTH_SHORT).show()
            return  false
        }
        return true
    }

    fun feedback() {
        try {
            val rootObject = feedbackRequestModel(binding!!.name.text.toString().trim(),binding!!.email.text.toString().trim(),binding!!.mobile.text.toString().trim(),binding!!.message.text.toString().trim())

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.feedback(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(call: Call<SuccessModel>, response: Response<SuccessModel>) {
                    val sessionModel = response.body()!!
                    if(sessionModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            Toast.makeText(activity,"Feedback Submit",Toast.LENGTH_SHORT).show()
                            clearData()
                        }catch (ex : Exception){

                        }


                    }



                }


                override fun onFailure(call: Call<SuccessModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                    binding!!.progressCircular.visibility = View.GONE
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun clearData(){
        binding!!.name.text!!.clear()
        binding!!.email.text!!.clear()
        binding!!.mobile.text!!.clear()
        binding!!.message.text!!.clear()
    }

}