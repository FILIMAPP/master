package com.isoftinc.film.util


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.isoftinc.film.R

class ShowingImage {
    var isload = false

    companion object {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)


        fun showImage(activity: BaseActivity, url: String?, imageView: ImageView) {
            if (!url.isNullOrBlank()) {
                Glide.with(activity.applicationContext)
                    .load(url)
                    .dontAnimate()
                    .apply(requestOptions)
                    .into(imageView)


            }


        }


        fun showProfileImage(activity: BaseActivity, url: String?, imageView: ImageView) {
            if (!url.isNullOrBlank()) {
                Glide.with(activity)
                    .load(url)
                    .override(100, 100)
                    .placeholder(R.drawable.profile_img)
                    .dontAnimate()
                    .apply(requestOptions)
                    .error(R.drawable.profile_img)
                    .into(imageView)


            }


        }


    }
}