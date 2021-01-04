package com.isoftinc.film.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.isoftinc.film.R
import com.isoftinc.film.adapter.NotificationAdapter
import com.isoftinc.film.databinding.FragmentHomeBinding
import com.isoftinc.film.util.BaseFragment

class NotificationFragment: BaseFragment() {
    var binding : FragmentHomeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))



        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.setImageResource(R.drawable.ic_baseline_close_24)
        binding!!.toolbar.tvsubscribe.visibility = View.GONE
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        val params: RelativeLayout.LayoutParams = binding!!.toolbar.ivappIcon.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        binding!!.toolbar.ivappIcon.layoutParams = params
        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }
        binding!!.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.verticalRecycler.adapter = NotificationAdapter(activity)


    }
}