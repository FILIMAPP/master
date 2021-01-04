package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.databinding.LayoutNotificationBinding
import com.isoftinc.film.util.BaseActivity

class NotificationAdapter(val activity: BaseActivity) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutNotificationBinding = DataBindingUtil.inflate<LayoutNotificationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_notification, parent, false
        )
        return ViewHolder(layoutNotificationBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class ViewHolder(val layoutNotificationBinding: LayoutNotificationBinding) :
        RecyclerView.ViewHolder(layoutNotificationBinding.root)
}