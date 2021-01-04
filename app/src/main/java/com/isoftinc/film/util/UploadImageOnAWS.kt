package com.isoftinc.film.util

import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.isoftinc.film.fragment.ProfileFragment
import java.io.File

class UploadImageOnAWS {
    companion object {
        var url: String? = null
        var activity: BaseActivity? = null

        fun uploadImage(activity: BaseActivity, str: String, fragment: ProfileFragment) {
            this.url = str
            this.activity = activity
            val credentialsProvider = CognitoCachingCredentialsProvider(
                ApplicationLoader.applicationContext!!,
                AppConstants().POOL_ID, Regions.US_EAST_2
            )
            val s3 = AmazonS3Client(credentialsProvider)
            val transferUtility = TransferUtility(s3, activity)
            val file = File(str)
            val observer = transferUtility.upload(
                AppConstants().BUSKET_NAME,
                "user/profile_img/images" + file.getName(), file, CannedAccessControlList.PublicRead
            )
            observer.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    if (state === TransferState.COMPLETED) {
                        val url = AppConstants().AWS_URL + observer.key
                        //set Image in imageView


                        //     (activity as AwsResponse).onStateComplete(url)
                        fragment?.onStateComplete(url)
                        Log.e("responseProgress", "Image Url : " + url)

                        //we just need to share this File url with Api service request.
                    } else if (state === TransferState.FAILED) {
                        Log.e("responseProgress", "FAILED")

                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    val done = (((bytesCurrent.toDouble() / bytesTotal) * 100.0).toInt())
                    Log.d("responseProgress", "UPLOAD - - ID: $id, percent done = $done")
                }

                override fun onError(id: Int, ex: Exception) {

                    Log.e("responseError", "Error  : " + ex.message)
                    ex.printStackTrace()
                }
            })
        }


    }
}