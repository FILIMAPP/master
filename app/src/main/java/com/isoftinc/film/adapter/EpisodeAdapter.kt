package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.databinding.LayoutEpisodeAdpterBinding
import com.isoftinc.film.model.SessionDetail
import com.isoftinc.film.util.AndroidUtilities
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.ShowingImage
import kotlin.math.roundToInt

class EpisodeAdapter  : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>(){

    var activity: BaseActivity? = null

    var list: ArrayList<SessionDetail>? = null
    private var onClickListner: OnListener? = null
    interface OnListener {
        fun click(episodeModel  : SessionDetail)
    }

    fun setListner(onClickListner: OnListener) {
        this.onClickListner = onClickListner
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val layoutStickerAdapterBinding = DataBindingUtil.inflate<LayoutEpisodeAdpterBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.layout_episode_adpter, viewGroup, false
        )

        return ViewHolder(layoutStickerAdapterBinding)
    }

    override fun onBindViewHolder(ViewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        ShowingImage.showImage(activity!!, model.episode.video[0].thumbnail, ViewHolder.layoutStickerAdapterBinding.ivMovies )
        val epiNo =  model.episode.name
        ViewHolder.layoutStickerAdapterBinding.episode.text =  epiNo
        ViewHolder.layoutStickerAdapterBinding.description.text = model.episode.description
       val time = model.episode.main_video_duration.toFloat().roundToInt()
        try {
            ViewHolder.layoutStickerAdapterBinding.time.text = AndroidUtilities().secToTime(time)
        }catch (ex : Exception){

        }

        ViewHolder.layoutStickerAdapterBinding.llepisode.setOnClickListener {
            if(onClickListner != null)
                onClickListner!!.click(model)
        }

      


    }
    override fun getItemCount(): Int {
        return if(list == null) 0 else list!!.size
    }

       fun setEpisodeAdapter(activity: BaseActivity, list: ArrayList<SessionDetail>?) {
           this.list = list
           this.activity = activity
           notifyDataSetChanged()
       }


    inner class ViewHolder(val layoutStickerAdapterBinding: LayoutEpisodeAdpterBinding) :
        RecyclerView.ViewHolder(layoutStickerAdapterBinding.root)
}