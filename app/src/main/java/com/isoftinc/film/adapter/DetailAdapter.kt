package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.isoftinc.film.R
import com.isoftinc.film.util.BaseActivity

class DetailAdapter (val activity: BaseActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {

            ROW_VIEW_TYPE -> {
                val rowView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.horizontal_list, parent, false)
                HorizontalListViewHolder(rowView)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.horizontal_list, parent, false)
                HorizontalListViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val context = holder.itemView.context
        val layoutManager: RecyclerView.LayoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        when (holder.itemViewType) {

            ROW_VIEW_TYPE -> {
                val horizontalListViewHolder =
                    holder as HorizontalListViewHolder
                horizontalListViewHolder.horizontalList.layoutManager = layoutManager
                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                    val snapHelper: SnapHelper = LinearSnapHelper()
                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                }
                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled = false
                horizontalListViewHolder.horizontalList.adapter = HomeCategoryAdapter(activity,null,null)

                when(position) {
                    0 -> {
                        horizontalListViewHolder.horizontalListTitle.visibility = View.GONE
                        horizontalListViewHolder.viewAll.visibility = View.GONE
                    }
                    1 -> {
                        horizontalListViewHolder.horizontalListTitle.visibility = View.GONE
                        horizontalListViewHolder.viewAll.visibility = View.GONE
                    }
                    2 ->{
                        horizontalListViewHolder.horizontalListTitle.visibility = View.GONE
                        horizontalListViewHolder.viewAll.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun getItemViewType(position: Int): Int {
        return  ROW_VIEW_TYPE
    }

    internal inner class HorizontalListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var horizontalList: RecyclerView
        var horizontalListTitle: TextView
        var viewAll : TextView

        init {
            horizontalList = itemView.findViewById(R.id.horizontalList)
            horizontalListTitle = itemView.findViewById(R.id.horizontalListTitle)
            viewAll = itemView.findViewById(R.id.viewall)
        }
    }



    companion object {
        private const val ROW_VIEW_TYPE = 200
    }
}