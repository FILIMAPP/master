package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.util.BaseActivity

class TitleAdapter(val activity: BaseActivity) : RecyclerView.Adapter<TitleAdapter.TitleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_title_adapter, parent, false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {

        when(position){
            0 ->{
                holder.title.text = "Trending"
            }

            1 ->{
                holder.title.text = "Drama"
            }
            2 ->{
                holder.title.text = "Romentic"
            }
            3 ->{
                holder.title.text = "youth"
            }
            4 ->{
                holder.title.text = "Adventure"
            }
            5 ->{
                holder.title.text = "Thriller"
            }
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView

        init {
            title = itemView.findViewById(R.id.title)
        }
    }
}