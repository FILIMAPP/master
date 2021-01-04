package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.isoftinc.film.R
import com.isoftinc.film.databinding.HorizontalListBinding
import com.isoftinc.film.databinding.LayoutMoviesAdapterBinding
import com.isoftinc.film.fragment.ViewAllFragment
import com.isoftinc.film.model.SubCategoryVideoModel
import com.isoftinc.film.util.BaseActivity

class MoviesVerticalAdapter : RecyclerView.Adapter<MoviesVerticalAdapter.ViewHolder>() {
    var activity: BaseActivity? = null
     var moviesList : ArrayList<SubCategoryVideoModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesVerticalAdapter.ViewHolder {

        val layoutAdapterBinding = DataBindingUtil.inflate<HorizontalListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.horizontal_list, parent, false
        )

        return ViewHolder(layoutAdapterBinding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val context = holder.itemView.context
        val layoutManager: RecyclerView.LayoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

        holder.layoutAdapterBinding.horizontalList.layoutManager = layoutManager
        if ( holder.layoutAdapterBinding.horizontalList.onFlingListener == null) {
            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView( holder.layoutAdapterBinding.horizontalList)
        }
        val model = moviesList!![position]


        holder.layoutAdapterBinding.horizontalList.setHasFixedSize(true)
        holder.layoutAdapterBinding.horizontalList.isNestedScrollingEnabled =
            false
        holder.layoutAdapterBinding.horizontalList.adapter =
            HomeCategoryAdapter(activity!!, model.video,null)
        holder.layoutAdapterBinding.horizontalList.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

        holder.layoutAdapterBinding.horizontalListTitle.text = model.name
        holder.layoutAdapterBinding.viewall.setOnClickListener {
            activity!!.replaceFragment(ViewAllFragment.newInstance(model._id))
        }

    }

    override fun getItemCount(): Int {
        return if(moviesList == null) 0 else moviesList!!.size
    }

    fun setMoviesList(activity: BaseActivity,moviesList : ArrayList<SubCategoryVideoModel>){
        this.activity = activity
        this.moviesList = moviesList
        notifyDataSetChanged()

    }

    inner class ViewHolder(val layoutAdapterBinding: HorizontalListBinding) :
        RecyclerView.ViewHolder(layoutAdapterBinding.root)

}