package com.isoftinc.film.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.isoftinc.film.R
import com.isoftinc.film.databinding.DialogCastingBinding
import com.isoftinc.film.util.BaseActivity


class CastingDialog : DialogFragment() {
    var activity: BaseActivity? = null
    var binding: DialogCastingBinding? = null


    companion object {

        fun newInstance(activity: BaseActivity) = CastingDialog().apply {
            this.activity = activity

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.dialog_casting, container, false))
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
            binding!!.nameText.transformationMethod = null
            val ss = SpannableString(activity!!.getText(R.string.casting_msg))
            val android = activity!!.resources.getDrawable(R.drawable.ic_baseline_cast_gray_24)
            android.setBounds(0, 0, 35, 35)
            ss.setSpan(
                ImageSpan(android, ImageSpan.ALIGN_BASELINE),
                38,
                39,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            binding!!.nameText.text = ss

            binding!!.ok.setOnClickListener {
                dismiss()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}
