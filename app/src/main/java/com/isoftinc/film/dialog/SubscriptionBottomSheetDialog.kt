package com.isoftinc.film.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.isoftinc.film.R
import com.isoftinc.film.adapter.PlanListAdapter
import com.isoftinc.film.databinding.DialogSubscriptionBottomSheetBinding
import com.isoftinc.film.fragment.LoginRegisterFragment
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.PreferenceStore
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.activity.MainActivity
import com.isoftinc.film.fragment.RegisterFragment
import com.razorpay.Checkout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubscriptionBottomSheetDialog : BottomSheetDialogFragment() {
    var binding: DialogSubscriptionBottomSheetBinding? = null


    val TAG = "ActionBottomDialog"
    var activity:BaseActivity? = null
    var planListAdapter : PlanListAdapter? = null
    var planList :ArrayList<PlanList>? = null
    var preferenceStore: PreferenceStore? = null
    var mobileVerificationResult : MobileVerificationResult? = null
    private var onDialog: OnListener? = null
    var price = ""
    var planId = ""
    var id = ""
    interface OnListener {
        fun accept(price : String, planId :String)
        fun navigate()
    }

    fun setListner(onSuccess: OnListener) {
        this.onDialog = onSuccess
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
     * To ensure faster loading of the Checkout form,
     * call this method as early as possible in your checkout flow
     * */
        Checkout.preload(activity!!.applicationContext)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.dialog_subscription_bottom_sheet, container, false))


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPlanList()
        preferenceStore = PreferenceStore(activity!!.applicationContext)
        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding!!.verticalRecycler.layoutManager = layoutManager
        planListAdapter = PlanListAdapter()
        binding!!.verticalRecycler.adapter = planListAdapter
        planListAdapter!!.setListner(object : PlanListAdapter.OnListener{
            override fun accept(pos: Int) {
                price = planList!![pos].price
                planId = planList!![pos]._id
                binding!!.payNow.text = planList!![pos].price + " /-  Pay Now"
            }

        })

        if(mobileVerificationResult == null)
            binding!!.gotoLogin.visibility = View.VISIBLE
        else
            binding!!.gotoLogin.visibility = View.GONE

        binding!!.payNow.setOnClickListener {
            if(price.isNullOrEmpty()){
                Toast.makeText(activity,"Please select any Packages",Toast.LENGTH_SHORT).show()
            }else{
                if(mobileVerificationResult == null){
                    dismiss()
                    if(onDialog != null)
                        onDialog!!.navigate()

                }else{
                    if(id != planId){
                        dismiss()
                        if(onDialog != null)
                            onDialog!!.accept(price, planId)
                    }else{
                        Toast.makeText(activity,"Already Subscribed. Please update",Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }

        binding!!.gotoLogin.setOnClickListener {
            dismiss()
           // DetailActivity().finish()
          //  activity!!.replaceFragment(RegisterFragment.newInstance(false))
            val intent = Intent(activity,MainActivity::class.java)
            activity!!.startActivity(intent)
        }




    }




    companion object {


        fun newInstance(activity: BaseActivity, id : String?) = SubscriptionBottomSheetDialog().apply {

            this.activity = activity
            this.id = id!!

        }
    }



    private fun getPlanList() {
        try {


            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.getPlanList()
            call.enqueue(object : Callback<PlanListModel> {
                override fun onResponse(call: Call<PlanListModel>, response: Response<PlanListModel>) {
                    val singleDetailModel = response.body()!!
                    if(singleDetailModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            planList = singleDetailModel.result.plan_list
                            planListAdapter!!.setPlanList(activity!!,singleDetailModel.result.plan_list, id)

                        }catch (ex :Exception){

                        }

                    }



                }


                override fun onFailure(call: Call<PlanListModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }




}