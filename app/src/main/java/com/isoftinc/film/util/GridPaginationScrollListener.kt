package com.isoftinc.film.util

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract  class GridPaginationScrollListener constructor() :
    RecyclerView.OnScrollListener() {

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private  var visibleThreshold = 2
    var firstVisibleInListview: Int? = null
    constructor(layoutManager: GridLayoutManager) : this() {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.getSpanCount()


        firstVisibleInListview = layoutManager.findFirstVisibleItemPosition()
    }

    constructor(layoutManager: StaggeredGridLayoutManager) : this() {
        this.mLayoutManager = layoutManager
    }

    constructor(layoutManager: LinearLayoutManager) : this() {
        this.mLayoutManager = layoutManager
    }


    /*
     Method gets callback when user scroll the search list
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        var firstVisibleItemPosition = 0


        // Check the layout manager type in order to determine the last visible position

        // Check the layout manager type in order to determine the last visible position
        if (mLayoutManager is StaggeredGridLayoutManager) {
           /* val lastVisibleItemPositions =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)*/
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItemPosition =
                (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItemPosition =
                (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            firstVisibleItemPosition =
                (mLayoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        }





              val visibleItemCount = mLayoutManager.childCount


              when (mLayoutManager) {
                  is StaggeredGridLayoutManager -> {
                      val firstVisibleItemPositions =
                          (mLayoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(null)
                      // get maximum element within the list
                      firstVisibleItemPosition = firstVisibleItemPositions[0]
                  }
                  is GridLayoutManager -> {
                      firstVisibleItemPosition =
                          (mLayoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                  }
                  is LinearLayoutManager -> {
                      firstVisibleItemPosition =
                          (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                  }
              }



        if(dy > 0){
            if (!isLoading && !isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    Log.i(TAG, "Loading more items")

                    loadMoreItems()
                }
            }
        }







    }



    protected abstract fun loadMoreItems()
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean

    companion object {
        private val TAG = PaginationScrollListener::class.java.simpleName
    }
}