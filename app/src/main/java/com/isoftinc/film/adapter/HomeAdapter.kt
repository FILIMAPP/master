package com.isoftinc.film.adapter

import android.R.attr.end
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.isoftinc.film.R
import com.isoftinc.film.fragment.HomeFragment
import com.isoftinc.film.fragment.ViewAllFragment
import com.isoftinc.film.model.BannerModel
import com.isoftinc.film.model.HomeResultModel
import com.isoftinc.film.model.SeriesListsModels
import com.isoftinc.film.model.VideoModel
import com.isoftinc.film.util.BaseActivity
import java.util.*
import kotlin.collections.ArrayList


class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var activity:BaseActivity? = null
    var homeResultModel:HomeResultModel? = null
    var latestList : ArrayList<VideoModel>? = null
    var trndingList : ArrayList<VideoModel>? = null
    var continueList : ArrayList<VideoModel>? = null
    var  seriesList: ArrayList<SeriesListsModels>? = null
     var showsList: ArrayList<SeriesListsModels>? = null
    var episodeId = ""



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BANNER_VIEW_TYPE -> {
                val bannerView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.horizontal_list, parent, false)
                BannerViewHolder(bannerView)
            }
            CONTINUE_WATCH_TYPE -> {
                val rowView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.horizontal_list, parent, false)
                HorizontalListViewHolder(rowView)
            }
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val layoutManager: RecyclerView.LayoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        when (holder.itemViewType) {
            BANNER_VIEW_TYPE -> {
                val a = holder as BannerViewHolder
                a.horizontalList.layoutManager = layoutManager
                if (a.horizontalList.onFlingListener == null) {
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(a.horizontalList)
                }
                a.horizontalList.setHasFixedSize(true)
                a.horizontalList.isNestedScrollingEnabled = false
                val bannerAdapter = BannerAdapter(activity!!, homeResultModel!!.banner)
                a.horizontalList.adapter = bannerAdapter
                a.horizontalListTitle.visibility = View.GONE
                a.viewAll.visibility = View.GONE


                if(HomeFragment.isFirstTime && continueList != null && latestList != null && trndingList != null && seriesList != null){
                    val timer = Timer()
                    timer.scheduleAtFixedRate(AutoScrollTask(a.horizontalList, 0,homeResultModel!!.banner ,false), 2000, 5000)
                    HomeFragment.isFirstTime = false
                } else if(HomeFragment.isFirstTime && continueList == null && latestList != null && trndingList != null && seriesList != null){
                    val timer = Timer()
                    timer.scheduleAtFixedRate(AutoScrollTask(a.horizontalList, 0,homeResultModel!!.banner ,false), 2000, 5000)
                    HomeFragment.isFirstTime = false
                }else if(HomeFragment.isFirstTime && continueList == null && latestList != null && trndingList == null && seriesList != null){
                    val timer = Timer()
                    timer.scheduleAtFixedRate(AutoScrollTask(a.horizontalList, 0,homeResultModel!!.banner ,false), 2000, 5000)
                    HomeFragment.isFirstTime = false
                }
            }


            ROW_VIEW_TYPE -> {

                if (latestList != null && latestList!!.size > 0 || trndingList != null && trndingList!!.size > 0 || seriesList != null && seriesList!!.size > 0 || continueList != null && continueList!!.size > 0) {

                    if (latestList != null && latestList!!.size > 0 && (trndingList == null || trndingList!!.size == 0) && (seriesList == null || seriesList!!.size == 0)  && (continueList == null || continueList!!.size == 0)) {
                        if (position == 1) {
                            val model = latestList
                            val horizontalListViewHolder =
                                holder as HorizontalListViewHolder
                            horizontalListViewHolder.horizontalList.layoutManager = layoutManager
                            if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                val snapHelper: SnapHelper = LinearSnapHelper()
                                snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                            }
                            horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                            horizontalListViewHolder.horizontalList.isNestedScrollingEnabled = false
                            horizontalListViewHolder.horizontalList.adapter =
                                HomeCategoryAdapter(activity!!, model, null)
                            horizontalListViewHolder.horizontalList.layoutParams =
                                LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )

                            horizontalListViewHolder.viewAll.visibility = View.GONE
                            horizontalListViewHolder.horizontalListTitle.text = activity!!.getText(
                                R.string.latest
                            )


                        } else {
                            if (homeResultModel!!.sub_category_video.size > 0) {
                                val model = homeResultModel!!.sub_category_video[position - 2]
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model.video, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )
                                horizontalListViewHolder.viewAll.visibility = View.VISIBLE

                                horizontalListViewHolder.viewAll.setOnClickListener {
                                    activity!!.replaceFragment(ViewAllFragment.newInstance(model._id))
                                }
                                horizontalListViewHolder.horizontalListTitle.text = model.name

                            }
                        }
                    } else if (latestList != null && latestList!!.size > 0 && trndingList != null && trndingList!!.size > 0 && (seriesList == null || seriesList!!.size == 0)  && (continueList == null || continueList!!.size == 0)) {
                        Log.d("position==", position.toString())

                        when (position) {
                            1 -> {
                                val model = latestList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.latest
                                    )

                            }

                            2 -> {
                                val model = trndingList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.trending
                                    )


                            }


                            else -> {
                                if (homeResultModel!!.sub_category_video.size > 0) {
                                    val model = homeResultModel!!.sub_category_video[position - 3]
                                    val horizontalListViewHolder =
                                        holder as HorizontalListViewHolder
                                    horizontalListViewHolder.horizontalList.layoutManager =
                                        layoutManager
                                    if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                        val snapHelper: SnapHelper = LinearSnapHelper()
                                        snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                    }
                                    horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                    horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                        false
                                    horizontalListViewHolder.horizontalList.adapter =
                                        HomeCategoryAdapter(activity!!, model.video, null)
                                    horizontalListViewHolder.horizontalList.layoutParams =
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                        )

                                    horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                    horizontalListViewHolder.horizontalListTitle.text = model.name
                                    horizontalListViewHolder.viewAll.setOnClickListener {
                                        activity!!.replaceFragment(ViewAllFragment.newInstance(model._id))
                                    }

                                }
                            }

                        }

                    } else if (latestList != null && latestList!!.size > 0 && trndingList != null && trndingList!!.size > 0 && (seriesList != null || seriesList!!.size > 0) && (continueList == null || continueList!!.size == 0)) {
                        Log.d("position===", position.toString())
                        when (position) {
                            1 -> {
                                val model = latestList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.latest
                                    )

                            }

                            2 -> {
                                val model = trndingList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.trending
                                    )


                            }
                            3 -> {
                                val model = seriesList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, null, model)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.plays
                                    )

                            }
                            else -> {
                                if (homeResultModel!!.sub_category_video.size > 0) {
                                    val model = homeResultModel!!.sub_category_video[position - 4]
                                    val horizontalListViewHolder =
                                        holder as HorizontalListViewHolder
                                    horizontalListViewHolder.horizontalList.layoutManager =
                                        layoutManager
                                    if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                        val snapHelper: SnapHelper = LinearSnapHelper()
                                        snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                    }
                                    horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                    horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                        false
                                    horizontalListViewHolder.horizontalList.adapter =
                                        HomeCategoryAdapter(activity!!, model.video, null)
                                    horizontalListViewHolder.horizontalList.layoutParams =
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                        )

                                    horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                    horizontalListViewHolder.horizontalListTitle.text = model.name
                                    horizontalListViewHolder.viewAll.setOnClickListener {
                                        activity!!.replaceFragment(ViewAllFragment.newInstance(model._id))
                                    }

                                }
                            }

                        }

                    }else if (latestList != null && latestList!!.size > 0 && trndingList != null && trndingList!!.size > 0 && (seriesList == null || seriesList!!.size == 0) && (continueList != null || continueList!!.size != 0)) {
                        Log.d("position===", position.toString())
                        when (position) {
                            1 -> {
                                val model = continueList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.continue_watchig
                                    )

                            }
                            2 -> {
                                val model = latestList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.latest
                                    )

                            }

                            3 -> {
                                val model = trndingList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.trending
                                    )


                            }

                            else -> {
                                if (homeResultModel!!.sub_category_video.size > 0) {
                                    val model = homeResultModel!!.sub_category_video[position - 4]
                                    val horizontalListViewHolder =
                                        holder as HorizontalListViewHolder
                                    horizontalListViewHolder.horizontalList.layoutManager =
                                        layoutManager
                                    if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                        val snapHelper: SnapHelper = LinearSnapHelper()
                                        snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                    }
                                    horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                    horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                        false
                                    horizontalListViewHolder.horizontalList.adapter =
                                        HomeCategoryAdapter(activity!!, model.video, null)
                                    horizontalListViewHolder.horizontalList.layoutParams =
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                        )

                                    horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                    horizontalListViewHolder.horizontalListTitle.text = model.name
                                    horizontalListViewHolder.viewAll.setOnClickListener {
                                        activity!!.replaceFragment(ViewAllFragment.newInstance(model._id))
                                    }

                                }
                            }

                        }

                    }  /*else if (latestList != null && latestList!!.size > 0 && trndingList != null && trndingList!!.size > 0 && (seriesList != null || seriesList!!.size > 0) && (showsList != null || showsList!!.size > 0) && (continueList == null || continueList!!.size == 0)) {
                        when (position) {
                            1 -> {
                                val model = latestList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.latest
                                    )

                            }

                            2 -> {
                                val model = trndingList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.trending
                                    )


                            }
                            3 -> {
                                val model = seriesList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, null, model)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.plays
                                    )

                            }
                            4 -> {
                                val model = showsList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, null, model)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.shows
                                    )

                            }
                            else -> {
                                if (homeResultModel!!.sub_category_video.size > 0) {
                                    val model = homeResultModel!!.sub_category_video[position - 5]
                                    val horizontalListViewHolder =
                                        holder as HorizontalListViewHolder
                                    horizontalListViewHolder.horizontalList.layoutManager =
                                        layoutManager
                                    if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                        val snapHelper: SnapHelper = LinearSnapHelper()
                                        snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                    }
                                    horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                    horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                        false
                                    horizontalListViewHolder.horizontalList.adapter =
                                        HomeCategoryAdapter(activity!!, model.video, null)
                                    horizontalListViewHolder.horizontalList.layoutParams =
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                        )

                                    horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                    horizontalListViewHolder.horizontalListTitle.text = model.name

                                }
                            }

                        }

                    }*/ else if (latestList != null && latestList!!.size > 0 && trndingList != null && trndingList!!.size > 0 && seriesList != null && seriesList!!.size > 0 && continueList != null && continueList!!.size > 0) {
                        when (position) {
                            1 -> {
                                val model = continueList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.continue_watchig
                                    )

                            }

                            2 -> {
                                val model = latestList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.latest
                                    )

                            }

                            3 -> {
                                val model = trndingList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, model, null)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.trending
                                    )



                            }
                            4 -> {
                                val model = seriesList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, null, model)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.GONE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.plays
                                    )


                            }
                         /*   5 -> {
                                val model = showsList
                                val horizontalListViewHolder =
                                    holder as HorizontalListViewHolder
                                horizontalListViewHolder.horizontalList.layoutManager =
                                    layoutManager
                                if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                    val snapHelper: SnapHelper = LinearSnapHelper()
                                    snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                }
                                horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                    false
                                horizontalListViewHolder.horizontalList.adapter =
                                    HomeCategoryAdapter(activity!!, null, model)
                                horizontalListViewHolder.horizontalList.layoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                horizontalListViewHolder.horizontalListTitle.text =
                                    activity!!.getText(
                                        R.string.shows
                                    )

                            }*/
                            else -> {
                                if (homeResultModel!!.sub_category_video.size > 0) {
                                    val model = homeResultModel!!.sub_category_video[position - 5]
                                    val horizontalListViewHolder =
                                        holder as HorizontalListViewHolder
                                    horizontalListViewHolder.horizontalList.layoutManager =
                                        layoutManager
                                    if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                                        val snapHelper: SnapHelper = LinearSnapHelper()
                                        snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                                    }
                                    horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                                    horizontalListViewHolder.horizontalList.isNestedScrollingEnabled =
                                        false
                                    horizontalListViewHolder.horizontalList.adapter =
                                        HomeCategoryAdapter(activity!!, model.video, null)
                                    horizontalListViewHolder.horizontalList.layoutParams =
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                        )

                                    horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                                    horizontalListViewHolder.horizontalListTitle.text = model.name
                                    horizontalListViewHolder.viewAll.setOnClickListener {
                                        activity!!.replaceFragment(ViewAllFragment.newInstance(model._id))
                                    }

                                }
                            }

                        }
                    }


                } else {
                    if (homeResultModel!!.sub_category_video.size > 0) {
                        val model = homeResultModel!!.sub_category_video[position - 1]
                        val horizontalListViewHolder =
                            holder as HorizontalListViewHolder
                        horizontalListViewHolder.horizontalList.layoutManager = layoutManager
                        if (horizontalListViewHolder.horizontalList.onFlingListener == null) {
                            val snapHelper: SnapHelper = LinearSnapHelper()
                            snapHelper.attachToRecyclerView(horizontalListViewHolder.horizontalList)
                        }
                        horizontalListViewHolder.horizontalList.setHasFixedSize(true)
                        horizontalListViewHolder.horizontalList.isNestedScrollingEnabled = false
                        horizontalListViewHolder.horizontalList.adapter =
                            HomeCategoryAdapter(activity!!, model.video, null)
                        horizontalListViewHolder.horizontalList.layoutParams =
                            LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                            )
                        horizontalListViewHolder.viewAll.visibility = View.VISIBLE
                        horizontalListViewHolder.viewAll.setOnClickListener {
                            activity!!.replaceFragment(ViewAllFragment.newInstance(model._id))
                        }
                        horizontalListViewHolder.horizontalListTitle.text = model.name

                    }
                }


            }



        }



    }

    fun setHomeList(
        activity: BaseActivity,
        homeResultModel: HomeResultModel,
        continueList: ArrayList<VideoModel>?,
        latestList: ArrayList<VideoModel>?,
        trendingList: ArrayList<VideoModel>?,
        SeriesList: ArrayList<SeriesListsModels>?,
        ShowsList: ArrayList<SeriesListsModels>?
    ){
        this.activity = activity
        this.homeResultModel = homeResultModel
        this.continueList = continueList
        this.latestList = latestList
        this.trndingList = trendingList
        this.seriesList = SeriesList
        this.showsList = ShowsList
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return if(homeResultModel == null){
            0
        } else if(continueList != null && continueList!!.size > 0 && latestList != null && latestList!!.size > 0 && trndingList != null  && trndingList!!.size > 0 && seriesList != null && seriesList!!.size > 0 ){
            homeResultModel!!.sub_category_video.size + 5
        } else if( latestList != null && latestList!!.size > 0  && trndingList != null && trndingList!!.size > 0 && (seriesList == null || seriesList!!.size == 0) && continueList != null && continueList!!.size >0){
            homeResultModel!!.sub_category_video.size + 4
        } else if(latestList != null && latestList!!.size > 0 && trndingList == null && seriesList == null){
            homeResultModel!!.sub_category_video.size + 2
        }else if( latestList != null && latestList!!.size > 0 && trndingList != null && trndingList!!.size > 0 && (seriesList == null || seriesList!!.size == 0) ){
            homeResultModel!!.sub_category_video.size + 3
        }else if( latestList != null && latestList!!.size > 0  && trndingList != null && trndingList!!.size > 0 && seriesList != null && seriesList!!.size > 0){
            homeResultModel!!.sub_category_video.size + 4
        }else{
            homeResultModel!!.sub_category_video.size + 1
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) BANNER_VIEW_TYPE    else ROW_VIEW_TYPE
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

    internal inner class BannerViewHolder(itemView: View) :
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
        private const val BANNER_VIEW_TYPE = 100
        private const val ROW_VIEW_TYPE = 200
        private  const val CONTINUE_WATCH_TYPE = 300
    }


    private class AutoScrollTask(val recyclerView: RecyclerView, var position: Int, var arrayList : ArrayList<BannerModel>, var end :Boolean) : TimerTask() {
        override fun run() {
            if (position == arrayList.size - 1) {
                end = true
            } else if (position == 0) {
                end = false
            }

            if (!end) {
                position++
            } else {
                position--
            }
            recyclerView.smoothScrollToPosition(position)
        }
    }


}