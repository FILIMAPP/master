package com.isoftinc.film.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.databinding.LayoutMoviesAdapterBinding
import com.isoftinc.film.model.VideoListModel
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.PreferenceStore
import com.isoftinc.film.util.ShowingImage
import java.util.ArrayList

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.ViewHolder>(){

    var activity: BaseActivity? = null
    var list: ArrayList<VideoListModel>? = null
    var preferenceStore : PreferenceStore? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val layoutStickerAdapterBinding = DataBindingUtil.inflate<LayoutMoviesAdapterBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.layout_movies_adapter, viewGroup, false
        )

        return ViewHolder(layoutStickerAdapterBinding)
    }

    override fun onBindViewHolder(ViewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        if(model.video != null)
        ShowingImage.showImage(activity!!,model.video[0].thumbnail,ViewHolder.layoutStickerAdapterBinding.ivMovies)
        else
            ShowingImage.showImage(activity!!,model.thumbnail,ViewHolder.layoutStickerAdapterBinding.ivMovies)
        ViewHolder.layoutStickerAdapterBinding.ivMovies.setOnClickListener {
           preferenceStore = PreferenceStore(activity!!.applicationContext)
           /* val intent = Intent(activity, DetailActivity.newInstance(null,model, model._id,true,"")::class.java)
            activity!!.startActivity(intent)*/

            if(model.type.equals("M")){
                val intent = Intent(activity, DetailActivity.newInstance(null, model, model._id,true,"")::class.java)
                activity!!.startActivity(intent)
            }else if (model.type.equals("SE") || model.type.equals("SH") || model.type.equals("S")){
                val intent = Intent(activity, DetailActivity.newInstance(null, null, model._id,false,"")::class.java)
                activity!!.startActivity(intent)
            }

        }

    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setMoviesAdapter(activity: BaseActivity, list: ArrayList<VideoListModel>?) {
        this.list = list
        this.activity = activity
        notifyDataSetChanged()
    }

    fun addData(listItems: ArrayList<VideoListModel>?) {
        val size = this.list!!.size
        this.list!!.addAll(listItems!!)
        val sizeNew = this.list!!.size
        notifyItemRangeChanged(size, sizeNew)
    }


    inner class ViewHolder(val layoutStickerAdapterBinding: LayoutMoviesAdapterBinding) :
        RecyclerView.ViewHolder(layoutStickerAdapterBinding.root)
}