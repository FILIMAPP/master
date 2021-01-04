package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R

class WatchListAdapter : RecyclerView.Adapter<WatchListAdapter.HorizontalViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_list_item, parent, false)
        return HorizontalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {




    }

    override fun getItemCount(): Int {
        return 8
    }

    inner class HorizontalViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var cardImage: ImageView
        // var cardTitle: TextView

        init {
            cardImage = itemView.findViewById(R.id.image)
            //   cardTitle = itemView.findViewById(R.id.text)
        }
    }
}