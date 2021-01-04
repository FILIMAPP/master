package com.isoftinc.film.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.databinding.LayoutMoviesAdapterBinding
import com.isoftinc.film.model.SeriesListModel
import com.isoftinc.film.model.SeriesListModels
import com.isoftinc.film.model.SeriesListsModels
import com.isoftinc.film.model.VideoListModel
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.PreferenceStore
import com.isoftinc.film.util.ShowingImage
import java.util.ArrayList

class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.ViewHolder>(){

    var activity: BaseActivity? = null
    var list: ArrayList<SeriesListsModels>? = null
    var preferenceStore : PreferenceStore? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutMoviesAdapterBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.layout_movies_adapter, viewGroup, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(ViewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        ShowingImage.showImage(activity!!,model.thumbnail,ViewHolder.binding.ivMovies)
        ViewHolder.binding.ivMovies.setOnClickListener {
            var sessionId = ""
            if(model.season != null && model.season.size > 0)
               sessionId = model.season[0]._id
            val intent = Intent(activity, DetailActivity.newInstance(null, null, model._id,false,sessionId)::class.java)
            activity!!.startActivity(intent)

        }

    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setSeriesAdapter(activity: BaseActivity, list: ArrayList<SeriesListsModels>?) {
        this.list = list
        this.activity = activity
        notifyDataSetChanged()
    }

    fun addData(listItems: ArrayList<SeriesListsModels>?) {
        val size = this.list!!.size
        this.list!!.addAll(listItems!!)
        val sizeNew = this.list!!.size
        notifyItemRangeChanged(size, sizeNew)
    }


    inner class ViewHolder(val binding: LayoutMoviesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)
}