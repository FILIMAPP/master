package com.isoftinc.film.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.isoftinc.film.R
import com.isoftinc.film.databinding.FragmentRegisterBinding
import com.isoftinc.film.dialog.OneTimePasswordDialog
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.PreferenceStore
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterFragment : BaseFragment() {
    var binding : FragmentRegisterBinding? = null
    var preferenceStore: PreferenceStore? = null
    private var mCallbackManager: CallbackManager? = null
    var mGoogleSignInClient : GoogleSignInClient? = null
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 100
    var isFromSubscription = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.bind(inflater.inflate(R.layout.fragment_register, container, false))
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceStore = PreferenceStore(activity.applicationContext)
        binding!!.toolbar.toolbarTitle.text = getText(R.string.login_register)
        binding!!.toolbar.toolbarTitle.visibility = View.GONE
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        binding!!.toolbar.tvsubscribe.visibility = View.GONE
        binding!!.toolbar.skip.text = getText(R.string.skip)
       binding!!.toolbar.ibBack.visibility = View.GONE
        binding!!.btnlogin.setOnClickListener{
            if(isValidation()){
                binding!!.progressCircular.visibility = View.VISIBLE
                signUp()
            }
        }




        binding!!.toolbar.skip.setOnClickListener{
            preferenceStore!!.setIsLogin(false)
            preferenceStore!!.setUserDetail("")
            activity.replaceFragment(HomeFragment(),false,false,false)
        }

        binding!!.txtsignup.setOnClickListener {
            activity.replaceFragment(LoginRegisterFragment())
        }

        binding!!.facebookLogin.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }


        binding!!.googleLogin.setOnClickListener {
            if (mGoogleSignInClient != null) {
                mGoogleSignInClient!!.revokeAccess()
            }
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    //    mCallbackManager = CallbackManager.Factory.create()
        // val loginButton = findViewById(R.id.login_button) as LoginButton
        mCallbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(mCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    //loginResult?.accessToken?.userId
                    val request: GraphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        GraphRequest.GraphJSONObjectCallback() { jsonObject: JSONObject, graphResponse: GraphResponse ->
                            val id: String? = jsonObject.getString("id")
                            val name: String? = jsonObject.getString("name")
                            var email: String? = ""

                            if (jsonObject.length() >= 3) {
                                email = jsonObject.getString("email")
                            }

                            Log.d("successful","successFul")
                            binding!!.progressCircular.visibility = View.VISIBLE
                            socialLogin(id, //Social Id
                                "F"
                            )


                        })
                    val parameters = Bundle()
                    parameters.putString("fields", "id, name, email, gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                    // App code
                }

                override fun onCancel() {
                    Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT).show()
                }
            })

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser
                binding!!.progressCircular.visibility = View.VISIBLE
                socialLogin(user!!.uid,"G")


            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                // ...
                Snackbar.make(view!!, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()

            }

            // ...
        }
    }

    fun isValidation() : Boolean{
        if(binding!!.mobileNo.text.toString().trim().isEmpty()){
            Toast.makeText(activity,"Please Enter Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }else if(binding!!.mobileNo.text!!.length  != 10){
            Toast.makeText(activity, "Please Enter correct phone number", Toast.LENGTH_SHORT).show()
            return  false
        }

        return true
    }


    private fun signUp() {
        try {
            val rootObject = SignupRequestModel(binding!!.mobileNo.text.toString().trim())

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.userSignUp(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(call: Call<SuccessModel>, response: Response<SuccessModel>) {
                    val successModel = response.body()!!
                    if(successModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            val myDialog = OneTimePasswordDialog.newInstance(activity,binding!!.mobileNo.text.toString().trim(),isFromSubscription)
                            myDialog.show(activity.supportFragmentManager, "Alert Dialog")
                        }catch (ex:Exception){

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

    private fun socialLogin(id:String?, login_type:String?) {
        try {
            val  android_id = Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
            val device = DeviceModel(android_id,"A",preferenceStore!!.getToken()!!)
            val rootObject = SocialModelRequest(id!!, login_type!!, device)
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.socialLogin(rootObject)
            call.enqueue(object : Callback<MobileVerificationModel> {
                override fun onResponse(call: Call<MobileVerificationModel>, response: Response<MobileVerificationModel>) {
                    val successModel = response.body()!!
                    if(successModel.code == 200){
                        try {
                            userGet(successModel.result._id)
                        }catch (ex: Exception){

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


    private fun userGet(id : String) {
        try {

            val rootObject = UserGetModel(id)
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.userget(rootObject)
            call.enqueue(object : Callback<MobileVerificationModel> {
                override fun onResponse(call: Call<MobileVerificationModel>, response: Response<MobileVerificationModel>) {
                    val mobileVerificationModel = response.body()!!
                    if(mobileVerificationModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            preferenceStore!!.setUserDetail("")
                            preferenceStore!!.setIsLogin(true)
                            val gson = Gson()
                            val json = gson.toJson(mobileVerificationModel.result)
                            preferenceStore!!.setUserDetail(json)
                            activity.replaceFragment(HomeFragment(), false, false, false)

                        }catch (ex : Exception){

                        }




                    }



                }


                override fun onFailure(call: Call<MobileVerificationModel>, t: Throwable) {
                    binding!!.progressCircular.visibility = View.GONE
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    companion object {


        fun newInstance(isFromSubscription: Boolean) = RegisterFragment().apply {

            this.isFromSubscription = isFromSubscription

        }
    }
}