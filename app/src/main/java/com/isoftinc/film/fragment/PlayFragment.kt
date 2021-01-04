package com.isoftinc.film.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isoftinc.film.R
import com.isoftinc.film.databinding.FragmentHomeBinding
import com.isoftinc.film.util.BaseFragment

class PlayFragment: BaseFragment() {
    var binding : FragmentHomeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.bind(inflater.inflate(R.layout.fragment_home, container, false))
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.progressCircular.visibility = View.GONE

    }
}