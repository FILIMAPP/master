package com.isoftinc.film.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.isoftinc.film.R
import com.isoftinc.film.databinding.DialogOneTimePasswordBinding
import com.isoftinc.film.fragment.HomeFragment
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.PreferenceStore
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OneTimePasswordDialog :  DialogFragment() {
    var activity: BaseActivity? = null
    var binding: DialogOneTimePasswordBinding? = null
    var phone = " "
    var preferenceStore: PreferenceStore? = null
    var isFromSubscription = false
    companion object {

        fun newInstance(activity: BaseActivity, phone:String, isFromSubscription : Boolean) = OneTimePasswordDialog().apply {
            this.activity = activity
            this.phone = phone
            this.isFromSubscription = isFromSubscription

        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawableResource(R.drawable.background_dialog)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.dialog_one_time_password, container, false))

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceStore = PreferenceStore(activity!!.applicationContext)
         binding!!.done.setOnClickListener {
            if(isValidate()){
                verifyOTP()
                binding!!.progressCircular.visibility = View.VISIBLE
            }

         }

        binding!!.resend.setOnClickListener {
            binding!!.progressCircular.visibility = View.VISIBLE
            signUp()
        }


    }


    fun isValidate() : Boolean{
        if(binding!!.otp.text.toString().trim().isEmpty()){
            Toast.makeText(activity,"Please Enter OTP",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    @SuppressLint("HardwareIds")
    private fun verifyOTP() {
        try {
            val  android_id = Settings.Secure.getString(activity!!.contentResolver, Settings.Secure.ANDROID_ID)
            val device = DeviceModel(android_id,"A",preferenceStore!!.getToken()!!)
            val rootObject = VerifyMobileModel(phone, binding!!.otp.text.toString().trim(),device)

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.verifyMobileNo(rootObject)
            call.enqueue(object : Callback<MobileVerificationModel> {
                override fun onResponse(call: Call<MobileVerificationModel>, response: Response<MobileVerificationModel>) {
                    val successModel = response.body()!!
                    if(successModel.code == 200){
                        try {
                            if(isFromSubscription){
                                binding!!.progressCircular.visibility = View.GONE
                                preferenceStore!!.setUserDetail("")
                                preferenceStore!!.setIsLogin(true)
                                val gson = Gson()
                                val json = gson.toJson(successModel.result)
                                preferenceStore!!.setUserDetail(json)

                                dismiss()
                                activity!!.removeFragments(1)
                            }else{
                                userGet(successModel.result._id)
                                //  activity?.replaceFragment(HomeFragment(), false, false, false)
                                //dismiss()
                            }

                        }catch (ex : Exception){

                        }


                    }



                }


                override fun onFailure(call: Call<MobileVerificationModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                    binding!!.progressCircular.visibility = View.GONE
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun signUp() {
        try {
            val rootObject = SignupRequestModel(phone)

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.userSignUp(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(call: Call<SuccessModel>, response: Response<SuccessModel>) {
                    val successModel = response.body()!!
                    if(successModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
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


    private fun userGet(id : String) {
        try {

            val rootObject = UserGetModel(id)
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.userget(rootObject)
            call.enqueue(object : Callback<MobileVerificationModel> {
                override fun onResponse(call: Call<MobileVerificationModel>, response: Response<MobileVerificationModel>) {
                    val mobileVerificationModel = response.body()!!
                    if(mobileVerificationModel.code == 200){

                        /* val gson = Gson()
                         val json = gson.toJson(mobileVerificationModel.result)
                         preferenceStore!!.setUserDetail(json)*/
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            preferenceStore!!.setUserDetail("")
                            preferenceStore!!.setIsLogin(true)
                            val gson = Gson()
                            val json = gson.toJson(mobileVerificationModel.result)
                            preferenceStore!!.setUserDetail(json)
                            activity!!.replaceFragment(HomeFragment(), false, false, false)
                            dismiss()
                        }catch (ex : Exception){

                        }




                    }



                }


                override fun onFailure(call: Call<MobileVerificationModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}
