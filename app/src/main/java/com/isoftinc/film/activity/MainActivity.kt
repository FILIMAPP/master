package com.isoftinc.film.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.transition.Transition
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.facebook.CallbackManager
import com.isoftinc.film.R
import com.isoftinc.film.databinding.ActivityMainBinding
import com.isoftinc.film.dialog.ExitDialog
import com.isoftinc.film.dialog.LogoutDialog
import com.isoftinc.film.dialog.SubscriptionBottomSheetDialog
import com.isoftinc.film.fragment.*
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity(),BottomNavigationView.OnNavigationItemSelectedListener,
    PaymentResultListener {
    var binding: ActivityMainBinding? = null
    private var hideBottomClassList: ArrayList<Class<out BaseFragment?>>? = null
    private var isHideBotNavigation = false
    private var mMenuId = 0
    var preferenceStore: PreferenceStore? = null
    var bottomNavigationView : BottomNavigationView? = null
    var mobileVerificationResult : MobileVerificationResult? = null
    private var callbackManager: CallbackManager? = null

    var navigationView : NavigationView? = null
    var drawerLayout : DrawerLayout? = null

    companion object{
        var    subId = ""
        var isFromProfile = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        preferenceStore = PreferenceStore(applicationContext)
        createHideClassList()

        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                val deviceToken = instanceIdResult.token
                Log.d("devicetoken",deviceToken)
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
                preferenceStore!!.setToken(deviceToken)
            }


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        checkInternetConnection(this,binding!!.drawerLayout).execute()




        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
        showHeaderView()
        if(preferenceStore!!.getIsLogin()){
            replaceFragment(HomeFragment(),false,false,false)
        }else
            replaceFragment(RegisterFragment(),false,false,false)
        binding!!.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {

                    replaceFragment(HomeFragment(),false,false,false)

                }

                R.id.plan ->{
                    val jsons = preferenceStore!!.getUserDetail()
                    mobileVerificationResult = gson.fromJson(jsons, MobileVerificationResult::class.java)
                        val  subscriptionBottomSheetDialog =  SubscriptionBottomSheetDialog.newInstance(this,"")
                        subscriptionBottomSheetDialog.setListner(object  : SubscriptionBottomSheetDialog.OnListener{
                            override fun accept(price: String, planId: String) {
                                subId = planId
                                startPayment(price,this@MainActivity,subId,false)
                            }

                            override fun navigate() {
                                replaceFragment(RegisterFragment.newInstance(true))
                            }

                        })
                        subscriptionBottomSheetDialog.show(supportFragmentManager, SubscriptionBottomSheetDialog().TAG)


                }

                R.id.watchList -> {
                    if(preferenceStore!!.getIsLogin())
                    replaceFragment(WatchListFragment())
                    else
                        replaceFragment(LoginRegisterFragment())
                }

               /* R.id.notification -> {
                   // replaceFragment(NotificationFragment())
                }*/

                R.id.menu ->{
                    replaceFragment(AboutUsFragment())

                }
                R.id.invitefrnd ->{
                    onShareClick()
                }

                R.id.privacypolicy -> {
                     replaceFragment(PrivacyPolicyFragment())
                }

                R.id.faq ->{
                    replaceFragment(FAQFragment())
                }

                R.id.contactus -> {
                    replaceFragment(ContactUsFragment())

                }
                R.id.signIn -> {
                    replaceFragment(RegisterFragment(),false,false,false)
                }
                R.id.logout -> {
                    openDialog()
                }
            }
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            binding!!.drawerLayout.closeDrawers()
            true
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (callbackManager != null) {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun getCallBackManager(): CallbackManager {
        callbackManager = CallbackManager.Factory.create()
        return callbackManager!!
    }


    override fun replaceFragment(fragment: BaseFragment?, isAdd: Boolean, addtobs: Boolean, forceWithoutAnimation: Boolean, transition: Transition?) {
        if (fragment == null)
            return

        try {
            val ft = supportFragmentManager.beginTransaction()
            val tag = fragment.javaClass.simpleName
            ft.setAllowOptimization(true)
            if (!forceWithoutAnimation) {
                if (AndroidUtilities().isAndroid5()) {
                    if (transition != null) {
                        fragment.enterTransition = transition
                    } else {
                        fragment.enterTransition = TransitionUtil().slide(Gravity.END)
                    }
                } else {
                    ft.setCustomAnimations(
                        R.anim.pull_in_right,
                        R.anim.push_out_left,
                        R.anim.pull_in_left,
                        R.anim.push_out_right
                    )
                }
            }
            if (isAdd)
                ft.add(R.id.frame, fragment, tag)
            else
                ft.replace(R.id.frame, fragment, tag)
            if (addtobs)
            // if (false)
                ft.addToBackStack(tag)
            ft.commitAllowingStateLoss()
            if(!preferenceStore!!.getIsLogin()){
                binding!!.navigationView.menu.findItem(R.id.logout).isVisible = false
                binding!!.navigationView.menu.findItem(R.id.signIn).isVisible = true
            }else{
                binding!!.navigationView.menu.findItem(R.id.logout).isVisible = true
                binding!!.navigationView.menu.findItem(R.id.signIn).isVisible = false
            }
            isHideBotNavigation = hideBottomClassList!!.contains(fragment.javaClass)
            binding!!.bottomNavigation.visibility = if (isHideBotNavigation) View.VISIBLE else View.GONE

            binding!!.bottomNavigation.setOnNavigationItemSelectedListener(this)
            binding!!.bottomNavigation.menu.findItem(R.id.home).isChecked = true


        } catch (e: Exception) {

        }

    }

    override fun getCurrentFragment(): BaseFragment? {
        return supportFragmentManager.findFragmentById(R.id.frame) as BaseFragment?


    }

    override fun onBackPressed() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val backEntryCount = supportFragmentManager.backStackEntryCount
            if (backEntryCount > 0) {
                super.onBackPressed()
            } else
                exitDialog()

        }

    }

    private fun createHideClassList() {
        hideBottomClassList = ArrayList(5)
        hideBottomClassList!!.add(HomeFragment::class.java)
        hideBottomClassList!!.add(MoviesFragment::class.java)
        hideBottomClassList!!.add(PlayFragment::class.java)
        hideBottomClassList!!.add(DubFragment::class.java)
        hideBottomClassList!!.add(ProfileFragment::class.java)
        hideBottomClassList!!.add(NotificationFragment::class.java)
        hideBottomClassList!!.add(ContactUsFragment::class.java)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mMenuId = item.itemId
        for (i in 0 until binding!!.bottomNavigation.menu.size()) {
            val menuItem: MenuItem = binding!!.bottomNavigation.menu.getItem(i)
            val isChecked = menuItem.itemId == item.itemId
            menuItem.isChecked = isChecked
        }

        when (item.itemId) {
            R.id.bottomhome -> {
                HomeFragment.isFirstTime = true
                replaceFragment(HomeFragment(),false,false)
            }
            R.id.movies -> {
                HomeFragment.isFirstTime = true
                replaceFragment(MoviesFragment(),false,false)
            }
            R.id.plays -> {
                HomeFragment.isFirstTime = true
              replaceFragment(SeriesFragment(),false,false)
            }
            R.id.dubs -> {
                HomeFragment.isFirstTime = true
                Toast.makeText(applicationContext,"Coming Soon",Toast.LENGTH_SHORT).show()
              //  replaceFragment(SeriesFragment.newInstance(true),false,false)
            }
            R.id.menu ->{
                HomeFragment.isFirstTime = true
                showHeaderView()
                binding!!.drawerLayout.openDrawer(Gravity.LEFT)
            }
        }
        return true
    }


    fun onShareClick(){
        val msg = getString(R.string.share_msg)
        val mInvitationUrl = "https://play.google.com/store/apps/details?id=com.isoftinc.film"
        val shareMessage = "\n" + msg + "\n" + mInvitationUrl + "\n"
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(intent, "choose one"))
    }

    override fun onResume() {
        super.onResume()
        userGet()
      /*  if(preferenceStore!!.getIsLogin()){
            replaceFragment(HomeFragment(),false,false,false)
        }else
            replaceFragment(RegisterFragment(),false,false,false)*/
    }

    private fun userGet() {
        try {

            val rootObject = UserGetModel(mobileVerificationResult!!._id)
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.userget(rootObject)
            call.enqueue(object : Callback<MobileVerificationModel> {
                override fun onResponse(call: Call<MobileVerificationModel>, response: Response<MobileVerificationModel>) {
                    val mobileVerificationModel = response.body()!!
                    if(mobileVerificationModel.code == 200){
                          try {
                              val  android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                              var isMatch = false
                              for(i in 0 until mobileVerificationModel.result.device.size){
                                  if(mobileVerificationModel.result.device[i].id == android_id){
                                      isMatch = true
                                      break;

                                  }
                              }

                              if(isMatch){
                                  val gson = Gson()
                                  val json = gson.toJson(mobileVerificationModel.result)
                                  preferenceStore!!.setUserDetail(json)
                              }else{
                                  replaceFragment(RegisterFragment(),false,false)
                              }


                              if(isFromProfile)
                                  ProfileFragment().updateSubscriptionDetail(this@MainActivity)
                          }catch (ex : Exception){

                          }


                    }else{
                        clearAllStack()
                        Firebase.auth.signOut()
                        preferenceStore!!.setIsLogin(false)
                        preferenceStore!!.setUserDetail("")
                        replaceFragment(RegisterFragment(),false,false,false)

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


     fun startPayment(price : String,  activity: Activity, SubId : String?, isFromProfile : Boolean) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */

         subId = SubId!!
         Companion.isFromProfile  = isFromProfile
        val co = Checkout()
         var subPrice = ""
         if(price.contains(".")){
             val lastIndex = price.lastIndexOf(".")
           subPrice  = price.substring(0,lastIndex)

         }
         subPrice = subPrice + "00"

        try {
            val options = JSONObject()
            options.put("name","Filim")
            options.put("description","Subscription")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency","INR")
            options.put("amount",subPrice)

            val prefill = JSONObject()
            prefill.put("email","test@razorpay.com")
            prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: java.lang.Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        try{
            Toast.makeText(this,"Payment failed $errorCode \n $response",Toast.LENGTH_LONG).show()
        }catch (e: java.lang.Exception){
            Log.e("checkkk","Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        try{
            purchasePlan(razorpayPaymentId!!)
            Toast.makeText(this,"Payment Successful $razorpayPaymentId",Toast.LENGTH_LONG).show()
        }catch (e: java.lang.Exception){
            Log.e("checkkk","Exception in onPaymentSuccess", e)
        }

    }

    private fun purchasePlan(razorpayPaymentId: String) {
        try {

            val rootObject = PurchaseModelRequest(mobileVerificationResult!!._id, subId,razorpayPaymentId)
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.purchasePlan(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(call: Call<SuccessModel>, response: Response<SuccessModel>) {
                    val singleDetailModel = response.body()!!
                    if(singleDetailModel.code == 200){
                        try {
                            userGet()
                            Toast.makeText(this@MainActivity, "Payment Success",Toast.LENGTH_SHORT).show()

                        }catch (ex : Exception){

                        }


                    }
                }


                override fun onFailure(call: Call<SuccessModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun openDialog(){
        val myDialog = LogoutDialog.newInstance(this)
        myDialog.setListner(object : LogoutDialog.OnListener{
            override fun accept() {
                clearAllStack()
                Firebase.auth.signOut()
                preferenceStore!!.setIsLogin(false)
                preferenceStore!!.setUserDetail("")
               // preferenceStore!!.clearPreference()
                replaceFragment(RegisterFragment(),false,false,false)
            }
            override fun decline() {
                myDialog.dismiss()

            }


        })
        myDialog.show(supportFragmentManager, "Hello Testing")
    }

    private fun exitDialog(){

        val myDialog = ExitDialog.newInstance(this)
        myDialog.setListner(object : ExitDialog.OnListener{
            override fun accept() {
                finish()
            }



        })
        myDialog.show(supportFragmentManager, "Hello Testing")

    }


    fun showHeaderView(){
        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
        val headerView: View =  binding!!.navigationView.getHeaderView(0)
        headerView.setOnClickListener {
            if(mobileVerificationResult != null){
                binding!!.drawerLayout.closeDrawers()
                replaceFragment(ProfileFragment())
            }

        }
        val mobileNo = headerView.findViewById<TextView>(R.id.mobileNo)
        val username = headerView.findViewById<TextView>(R.id.username)
        val ivView = headerView.findViewById<CircleImageView>(R.id.circulariv)
        if(mobileVerificationResult != null){
            mobileNo.visibility = View.VISIBLE
            mobileNo.text = mobileVerificationResult!!.phone_number
            username.text = mobileVerificationResult!!.username
            ivView.visibility = View.VISIBLE
            ShowingImage.showProfileImage(this, mobileVerificationResult!!.profile_img,ivView)
        }
        else{
            username.text = "Guest User"
            mobileNo.visibility = View.GONE
            ivView.visibility = View.INVISIBLE
            ShowingImage.showProfileImage(this, "",ivView)

        }

    }


    class checkInternetConnection(val context: Context, val  drawerLayout: DrawerLayout) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean? {
            return  AndroidUtilities().hasActiveInternetConnection(context)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if(!result!!){
                val  snackbar = Snackbar.make(drawerLayout, "Internet Connection not Available", Snackbar.LENGTH_LONG)
                snackbar.show()
            }

        }
    }



}