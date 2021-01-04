package com.isoftinc.film.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.isoftinc.film.R
import com.isoftinc.film.databinding.DialogAlertBinding
import com.isoftinc.film.util.BaseActivity

class LogoutDialog : DialogFragment() {
    var activity: BaseActivity? = null
    var binding: DialogAlertBinding? = null

    private var onDialog: OnListener? = null

    interface OnListener {
        fun accept()
        fun decline()
    }


    fun setListner(onSuccess: OnListener) {
        this.onDialog = onSuccess
    }

    companion object {

        fun newInstance(activity: BaseActivity) = LogoutDialog().apply {
            this.activity = activity

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.dialog_alert, container, false))
        val dialog = dialog
        if (dialog != null) {
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {

            binding!!.yes.setOnClickListener {
                onDialog!!.accept()
                dismiss()
            }
            binding!!.no.setOnClickListener {
                onDialog!!.decline()
                dismiss()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

}
