package com.isoftinc.film.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.isoftinc.film.R
import com.isoftinc.film.adapter.MoviesAdapter
import com.isoftinc.film.databinding.FragmentViewAllBinding
import com.isoftinc.film.model.ViewAllRequest
import com.isoftinc.film.model.ViewAllResponse
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllFragment : BaseFragment() {
    var binding : FragmentViewAllBinding? = null
    val service: ApiClient? = ApiClient()
    var subCategoryId = ""
    var moviesAdapter :MoviesAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentViewAllBinding.bind(inflater.inflate(R.layout.fragment_view_all, container, false))
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.GONE
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        binding!!.toolbar.tvsubscribe.visibility = View.GONE
        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }

        viewData()
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

    }


    companion object{

        fun newInstance(subCategoryId : String) = ViewAllFragment().apply {
            this.subCategoryId = subCategoryId


        }
    }

    private fun viewData() {
        try {
            val rootObject = ViewAllRequest(subCategoryId)

            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getViewAll(rootObject)
            call.enqueue(object : Callback<ViewAllResponse> {
                override fun onResponse(call: Call<ViewAllResponse>, response: Response<ViewAllResponse>) {
                    val viewModel = response.body()!!
                    if(viewModel.code == 200){
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            moviesAdapter!!.setMoviesAdapter(activity,viewModel.result.list)
                        }catch (ex : Exception){

                        }

                    }



                }


                override fun onFailure(call: Call<ViewAllResponse>, t: Throwable) {
                    Log.e("responseError", t.message)
                    binding!!.progressCircular.visibility = View.GONE
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}