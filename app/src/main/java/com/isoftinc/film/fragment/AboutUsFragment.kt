package com.isoftinc.film.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isoftinc.film.R
import com.isoftinc.film.databinding.FragmentPrivacyPolicyBinding
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.util.BaseFragment

class AboutUsFragment : BaseFragment() {
    var binding : FragmentPrivacyPolicyBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPrivacyPolicyBinding.bind(inflater.inflate(R.layout.fragment_privacy_policy, container, false))
        return binding!!.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.setImageResource(R.drawable.ic_baseline_close_24)
        binding!!.toolbar.ivappIcon.visibility = View.VISIBLE
        binding!!.toolbar.tvsubscribe.visibility = View.GONE

        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }

        binding!!.privacyPolicy.settings.javaScriptEnabled = true
        binding!!.privacyPolicy.loadUrl(ApiClient().BASE_URL+"/about-us")
        binding!!.toolbar.ibBack.setOnClickListener {
            onBackPressed()
        }

    }
}