package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.databinding.BannerItemBinding
import com.isoftinc.film.databinding.LayoutTitleAdapterBinding
import com.isoftinc.film.dialog.SubscriptionBottomSheetDialog
import com.isoftinc.film.model.TrailerModel
import com.isoftinc.film.model.VideoCategoryListModel
import com.isoftinc.film.util.BaseActivity
import java.util.logging.Handler

class LanguageAdapter(val activity: BaseActivity, val list:ArrayList<VideoCategoryListModel>, val  qualityList : ArrayList<TrailerModel>?) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {
    private var onListner: OnListener? = null
    interface OnListener {
        fun accept(type :String)
    }

    fun setListner(onSuccess: OnListener) {
        this.onListner = onSuccess
    }

    var clickPos= 0
    var isClick = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutTitleAdapterBinding = DataBindingUtil.inflate<LayoutTitleAdapterBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_title_adapter, parent, false
        )
        return ViewHolder(layoutTitleAdapterBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(qualityList != null){
            when(qualityList[position].type){
                "1080"->{
                    holder.layoutTitleAdapterBinding.title.text = "High " + qualityList[position].type

                }
                "720"->{
                    holder.layoutTitleAdapterBinding.title.text = "Medium " + qualityList[position].type
                }

                "480"->{
                    holder.layoutTitleAdapterBinding.title.text = "Low " + qualityList[position].type
                }

            }

           /* if(clickPos == position){
                if( !qualityList[position].isSelected){
                    qualityList[position].isSelected = true
                }else{
                    qualityList[position].isSelected = false
                }
            }*/

            if(qualityList[position].isSelected){
                holder.layoutTitleAdapterBinding.llquality.setBackgroundResource(R.color.bg_gray)
            }else{
                holder.layoutTitleAdapterBinding.llquality.setBackgroundResource(0)
            }

            if(isClick){
                if(onListner != null)
                    onListner!!.accept(qualityList[position].type)
                qualityList[position].isSelected = clickPos == position
            }



          } else{
            val model = list[position]
            holder.layoutTitleAdapterBinding.title.text = model.language.name


        }



          //  holder.layoutTitleAdapterBinding.title.text = qualityList[position].type
        holder.layoutTitleAdapterBinding.title.setOnClickListener {
            if(qualityList != null){
                isClick = true
                clickPos = position
                notifyDataSetChanged()

               /* if(onListner != null)
                    onListner!!.accept(qualityList[position].type)*/
           //    clickPos = position



            }


        }


    }

    override fun getItemCount(): Int {
        return if(qualityList != null) qualityList.size else list.size
    }



    inner class ViewHolder(val layoutTitleAdapterBinding: LayoutTitleAdapterBinding) :
        RecyclerView.ViewHolder(layoutTitleAdapterBinding.root)

}