package com.isoftinc.film.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.activity.DetailActivity
import com.isoftinc.film.databinding.BannerItemBinding
import com.isoftinc.film.fragment.ViewAllFragment
import com.isoftinc.film.model.BannerModel
import com.isoftinc.film.util.BaseActivity
import com.isoftinc.film.util.ShowingImage

/**
 * Created by rahuljanagouda on 04/11/17.
 */
class BannerAdapter(val activity: BaseActivity, val bannerList : ArrayList<BannerModel>?) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bannerItemBinding = DataBindingUtil.inflate<BannerItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.banner_item, parent, false
        )
        return ViewHolder(bannerItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = bannerList!![position]
        ShowingImage.showImage(activity,model.url,holder.bannerItemBinding.image)
        val count = (position+1).toString() + "/" + bannerList.size.toString()
        holder.bannerItemBinding.count.text = count
        holder.bannerItemBinding.image.setOnClickListener {
            if(model.type.equals("V")){
                val intent = Intent(activity, DetailActivity.newInstance(null, null, model.video,true,"")::class.java)
                activity.startActivity(intent)
            }else if(model.type.equals("SE") ||model.type.equals("SH")  ){
            val sessionId = ""
            val intent = Intent(activity, DetailActivity.newInstance(null, null, model.series_show,false,sessionId)::class.java)
            activity.startActivity(intent)
              } else{
                activity.replaceFragment(ViewAllFragment.newInstance(model._id))
            }


        }
    }

    override fun getItemCount(): Int {
        return bannerList?.size ?: 0
    }

    inner class ViewHolder(val bannerItemBinding: BannerItemBinding) :
        RecyclerView.ViewHolder(bannerItemBinding.root)


}