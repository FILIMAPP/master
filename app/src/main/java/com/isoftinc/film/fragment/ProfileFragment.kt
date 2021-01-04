package com.isoftinc.film.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.isoftinc.film.R
import com.isoftinc.film.activity.MainActivity
import com.isoftinc.film.databinding.FragmentProfileBinding
import com.isoftinc.film.dialog.OpenCameraDialog
import com.isoftinc.film.dialog.SubscriptionBottomSheetDialog
import com.isoftinc.film.model.EditProfileRequestModel
import com.isoftinc.film.model.MobileVerificationModel
import com.isoftinc.film.model.MobileVerificationResult
import com.isoftinc.film.model.SubscriptionModel
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.retrofit.AwsResponse
import com.isoftinc.film.util.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.acos

class ProfileFragment: BaseFragment() ,AwsResponse {

    var preferenceStore: PreferenceStore? = null
    var mobileVerificationResult: MobileVerificationResult? = null
    private var GALLERY = 1
    private var CAMERA = 2
    private var OPTION = 3
    var picture_path = ""

    companion object{
        var binding: FragmentProfileBinding? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.bind(
            inflater.inflate(
                R.layout.fragment_profile,
                container,
                false
            )
        )
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceStore = PreferenceStore(activity.applicationContext)
        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.setImageResource(R.drawable.back_img)
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        binding!!.toolbar.tvsubscribe.text = getText(R.string.subscribe)
        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }

        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)

        if (mobileVerificationResult != null) {
            binding!!.mobileNo.text = mobileVerificationResult!!.phone_number
            binding!!.username.setText(mobileVerificationResult!!.username)
            binding!!.username.setSelection(mobileVerificationResult!!.username.length)
            ShowingImage.showProfileImage(
                activity,
                mobileVerificationResult!!.profile_img,
                binding!!.circulariv
            )
            if (mobileVerificationResult!!.subscription == null) {
                binding!!.toolbar.tvsubscribe.visibility = View.VISIBLE

            } else {
                binding!!.toolbar.tvsubscribe.visibility = View.GONE
            }
        }



        binding!!.update.setOnClickListener {
            if (isValidate()) {
                binding!!.progressCircular.visibility = View.VISIBLE
                if (picture_path.isEmpty())
                    ediProfile(mobileVerificationResult!!.profile_img)
                else
                    UploadImageOnAWS.uploadImage(activity, picture_path, this)

            }

        }


        binding!!.circulariv.setOnClickListener {
            openPermission()

        }
        if (mobileVerificationResult!!.subscription != null ) {
            binding!!.llsubscriprtionDetail.visibility = View.VISIBLE
            val jsonObject =
                getJsonFromMap(mobileVerificationResult!!.subscription as Map<String, Any>)
            val subscriptionModel =
                gson.fromJson(jsonObject.toString(), SubscriptionModel::class.java)
            binding!!.subsDetail.text = subscriptionModel.plan?.month + " months " + subscriptionModel.plan?.price + " /-"
            binding!!.validDate.text = "Subscription Valid Till: " + subscriptionModel.end_date

            binding!!.updatePackage.setOnClickListener {
                openSubscriptionDialog(subscriptionModel.plan?._id)

            }


        }


    }

    @Throws(JSONException::class)
    private fun getJsonFromMap(map: Map<String, Any>): JSONObject {
        val jsonData = JSONObject()
        for (key in map.keys) {
            var value = map[key]
            if (value is Map<*, *>) {
                value = getJsonFromMap(value as Map<String, Any>)
            }
            jsonData.put(key, value)
        }
        return jsonData
    }

    fun openSubscriptionDialog(id: String?) {
        val subscriptionBottomSheetDialog = SubscriptionBottomSheetDialog.newInstance(activity, id)
        subscriptionBottomSheetDialog.setListner(object : SubscriptionBottomSheetDialog.OnListener {
            override fun accept(price: String, planId: String) {
                subscriptionBottomSheetDialog.dismiss()
                val subId = planId
                MainActivity().startPayment(price, activity, subId, true)
            }

            override fun navigate() {
                activity!!.replaceFragment(RegisterFragment.newInstance(true))
            }

        })
        subscriptionBottomSheetDialog.show(
            activity.supportFragmentManager,
            SubscriptionBottomSheetDialog().TAG
        )
    }


    fun isValidate(): Boolean {
        if (binding!!.username.text.isNullOrEmpty()) {
            Toast.makeText(activity, "Please Enter Username", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun ediProfile(profile_img: String) {
        try {
            val rootObject = EditProfileRequestModel(
                mobileVerificationResult!!._id,
                binding!!.username.text.toString().trim(),
                profile_img
            )

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.editProfile(rootObject)
            call.enqueue(object : Callback<MobileVerificationModel> {
                override fun onResponse(
                    call: Call<MobileVerificationModel>,
                    response: Response<MobileVerificationModel>
                ) {
                    val viewModel = response.body()!!
                    if (viewModel.code == 200) {
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            val gson = Gson()
                            val json = gson.toJson(viewModel.result)
                            preferenceStore!!.setUserDetail(json)
                            Toast.makeText(activity,"Profile Updated Successfully",Toast.LENGTH_SHORT).show()
                            onBackPressed()
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


    fun openPermission() {
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Call some material design APIs here
            if (ContextCompat.checkSelfPermission(
                    ApplicationLoader.applicationContext!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) + ContextCompat
                    .checkSelfPermission(
                        ApplicationLoader.applicationContext!!,
                        Manifest.permission.CAMERA
                    )
                != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), OPTION
                )
            } else {
                captureImage()
            }
        } else {
            // Implement this feature without material design
            captureImage()
        }
    }

    fun captureImage() {
        val dialogImages = OpenCameraDialog()
        dialogImages.setActvity(activity, false)
        dialogImages.setListener(object : OpenCameraDialog.CustomItemListener {
            override fun onTakeVideoClicked() {

            }

            override fun onTakeTextViewClicked() {
                dialogImages.dismiss()
                takePhotoFromCamera()
            }

            override fun onUploadTextViewClicked() {
                dialogImages.dismiss()
                choosePhotoFromGallary()
            }
        })
        dialogImages.show(activity.supportFragmentManager, "")
    }


    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            galleryIntent,
            GALLERY
        )
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA) {
            takePhotoFromCamera()
        } else if (requestCode == GALLERY) {
            choosePhotoFromGallary()
        } else {
            openPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {


                    val thumbnail = MediaStore.Images.Media.getBitmap(
                        getActivity()?.getContentResolver(),
                        contentURI
                    )
                    val name = UUID.randomUUID().toString().toUpperCase()
                    val picturePath: File? = AndroidUtilities().getSavedBitmapPath(
                        name,
                        activity,
                        thumbnail!!
                    )
                    picture_path = picturePath!!.absolutePath
                    binding!!.circulariv.setImageBitmap(thumbnail)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            binding!!.circulariv.setImageBitmap(thumbnail)

            // Save image in External storage
            val name = UUID.randomUUID().toString().toUpperCase()
            val ImageFile = AndroidUtilities().getSavedBitmapPath(name, activity, thumbnail)
            picture_path = ImageFile!!.absolutePath

            //   UploadImageOnAWS.uploadImage(activity, ImageFile!!.absolutePath, this)
        }
    }

    override fun onStateComplete(response: String?) {
        Log.d("check", response)
        ediProfile(response!!)

    }

    fun updateSubscriptionDetail(activity: BaseActivity) {
        val gson = Gson()
        preferenceStore = PreferenceStore(activity.applicationContext)
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)


        val jsonObject = getJsonFromMap(mobileVerificationResult!!.subscription as Map<String, Any>)
        val subscriptionModel = gson.fromJson(jsonObject.toString(), SubscriptionModel::class.java)
        binding!!.subsDetail.text =
            subscriptionModel.plan?.name + " " + subscriptionModel.plan?.month + " months " + subscriptionModel.plan?.price + " /-"
        binding!!.validDate.text = "Subscription Valid Till: " + subscriptionModel.end_date

        binding!!.updatePackage.setOnClickListener {
            openSubscriptionDialog(subscriptionModel.plan?._id)
        }
    }
}