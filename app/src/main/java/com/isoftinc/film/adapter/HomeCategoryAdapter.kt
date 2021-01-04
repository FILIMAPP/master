package com.isoftinc.film.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.databinding.HorizontalListItemBinding
import com.isoftinc.film.model.SeriesListsModels
import com.isoftinc.film.model.VideoModel
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.PreferenceStore
import com.isoftinc.film.util.ShowingImage

/**
 * Created by rahuljanagouda on 04/11/17.
 */
class HomeCategoryAdapter(val activity: BaseActivity, val videoList :ArrayList<VideoModel>?, val seriesList : ArrayList<SeriesListsModels>?) : RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>() {
    var preferenceStore : PreferenceStore ? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val horizontalListItemBinding = DataBindingUtil.inflate<HorizontalListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.horizontal_list_item, parent, false
        )
        return ViewHolder(horizontalListItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        preferenceStore = PreferenceStore(activity.applicationContext)
        if(videoList != null && videoList.size > 0 && videoList[position] != null){
            val model = videoList[position]
            holder.horizontalListItemBinding.image.layoutParams.width = 300
            holder.horizontalListItemBinding.image.layoutParams.height = 420
            if(model.video != null && model.video[0].thumbnail.isNotEmpty())
            ShowingImage.showImage(activity,model.video[0].thumbnail,holder.horizontalListItemBinding.image)
            else
                ShowingImage.showImage(activity,model.thumbnail,holder.horizontalListItemBinding.image)
            holder.horizontalListItemBinding.image.setOnClickListener {
                if(model.type.equals("M")){
                    val intent = Intent(activity, DetailActivity.newInstance(model, null, model._id,true,"")::class.java)
                    activity.startActivity(intent)
                }else if (model.type.equals("SE") || model.type.equals("SH") || model.type.equals("S")){
                    val intent = Intent(activity, DetailActivity.newInstance(model, null, model._id,false,"")::class.java)
                    activity.startActivity(intent)
                }


            }

        }else if (seriesList != null){
            val model = seriesList[position]
            holder.horizontalListItemBinding.image.layoutParams.width = 300
            holder.horizontalListItemBinding.image.layoutParams.height = 420
            ShowingImage.showImage(activity,model.thumbnail,holder.horizontalListItemBinding.image)
            holder.horizontalListItemBinding.image.setOnClickListener {
                var sessionId = ""
                if(model.season != null && model.season.size > 0)
                    sessionId = model.season[0]._id
                val intent = Intent(activity, DetailActivity.newInstance(null, null, model._id,false,sessionId)::class.java)
                activity!!.startActivity(intent)

            }


            }






    }

    override fun getItemCount(): Int {
        return if(seriesList != null) seriesList.size else videoList?.size ?: 0
    }


    inner class ViewHolder(val horizontalListItemBinding: HorizontalListItemBinding) :
        RecyclerView.ViewHolder(horizontalListItemBinding.root)
}