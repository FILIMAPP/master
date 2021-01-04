package com.isoftinc.film.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.isoftinc.film.R
import com.isoftinc.film.adapter.MoviesAdapter
import com.isoftinc.film.databinding.FragmentSearchBinding
import com.isoftinc.film.databinding.FragmentViewAllBinding
import com.isoftinc.film.model.SearchModel
import com.isoftinc.film.model.SearchRequestModel
import com.isoftinc.film.model.VideoListModel
import com.isoftinc.film.model.ViewAllResponse
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.GridPaginationScrollListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : BaseFragment() {
    var binding : FragmentSearchBinding? = null
    val service: ApiClient? = ApiClient()
    var subCategoryId = ""
    var moviesAdapter : MoviesAdapter? = null
    var isLastPagecheck: Boolean = false
    var isLoadingcheck: Boolean = false
    var pageNo = 1
    var  search = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.bind(inflater.inflate(R.layout.fragment_search, container, false))
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding!!.ibBack.setOnClickListener {
            onBackPressed()
        }


        binding!!.etsearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    binding!!.progressCircular.visibility = View.VISIBLE
                    search = ""
                    viewData(s.toString())
                }

            }
        })

        val gridLayoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding!!.rvViewAll.layoutManager = gridLayoutManager
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 2 column size for first row
                return 1
            }
        }
        moviesAdapter = MoviesAdapter()
        binding!!.rvViewAll.adapter = moviesAdapter
        binding!!.rvViewAll.addOnScrollListener(object : GridPaginationScrollListener(gridLayoutManager) {


            override fun loadMoreItems() {
                isLoadingcheck = true
                pageNo++
                //you have to call loadmore items to get more data
                viewData(search)


            }



            override val isLastPage: Boolean
                get() = isLastPagecheck
            override val isLoading: Boolean
                get() = isLoadingcheck
        })

    }


    companion object{

        fun newInstance(subCategoryId : String) = ViewAllFragment().apply {
            this.subCategoryId = subCategoryId


        }
    }

    private fun viewData(search: String) {
        try {
            val rootObject = SearchRequestModel(pageNo.toString(),search)

            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.search(rootObject)
            call.enqueue(object : Callback<SearchModel> {
                override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {
                    val viewModel = response.body()!!
                    if(viewModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            //  moviesAdapter!!.setMoviesAdapter(activity,viewModel.result.movie_list)
                            getMoreItems(viewModel.result)
                        }catch (ex : Exception){

                        }

                    }



                }


                override fun onFailure(call: Call<SearchModel>, t: Throwable) {
                    binding!!.progressCircular.visibility = View.GONE
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getMoreItems(list: ArrayList<VideoListModel>?) {
        //after fetching your data assuming you have fetched list in your
        // recyclerview adapter assuming your recyclerview adapter is
        //rvAdapter
        //  after getting your data you have to assign false to isLoading
        isLoadingcheck = false

        if (pageNo == 1) {

            moviesAdapter!!.setMoviesAdapter(activity, list)
        } else {

            moviesAdapter!!.addData(list)
        }


    }
}