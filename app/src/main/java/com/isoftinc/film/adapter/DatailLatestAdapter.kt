package com.isoftinc.film.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.databinding.LayoutMoviesAdapterBinding
import com.isoftinc.film.databinding.LayoutYouMayAlsoLikeAdapterBinding
import com.isoftinc.film.model.CommonVideoModel
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.ShowingImage

class DatailLatestAdapter : RecyclerView.Adapter<DatailLatestAdapter.ViewHolder>(){

    var activity: BaseActivity? = null
   var list: ArrayList<CommonVideoModel>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val layoutStickerAdapterBinding = DataBindingUtil.inflate<LayoutYouMayAlsoLikeAdapterBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.layout_you_may_also_like_adapter, viewGroup, false
        )

        return ViewHolder(layoutStickerAdapterBinding)
    }

    override fun onBindViewHolder(ViewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        ShowingImage.showImage(activity!!,model.video[0].thumbnail,ViewHolder.layoutStickerAdapterBinding.ivMovies)
        ViewHolder.layoutStickerAdapterBinding.ivMovies.setOnClickListener {
            val intent = Intent(activity, DetailActivity.newInstance(null,null, model._id,true,"")::class.java)
            activity!!.startActivity(intent)
        }


    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

       fun setDetailAdapter(activity: BaseActivity, list: ArrayList<CommonVideoModel>?) {
           this.list = list
           this.activity = activity
           notifyDataSetChanged()
       }


    inner class ViewHolder(val layoutStickerAdapterBinding: LayoutYouMayAlsoLikeAdapterBinding) :
        RecyclerView.ViewHolder(layoutStickerAdapterBinding.root)
}