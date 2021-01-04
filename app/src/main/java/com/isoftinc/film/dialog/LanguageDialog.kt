package com.isoftinc.film.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.isoftinc.film.R
import com.isoftinc.film.adapter.LanguageAdapter
import com.isoftinc.film.databinding.LanguageDialogBinding
import com.isoftinc.film.model.TrailerModel
import com.isoftinc.film.model.VideoCategoryListModel
import com.isoftinc.film.util.BaseActivity

class LanguageDialog :  DialogFragment() {
    var activity: BaseActivity? = null
    var binding: LanguageDialogBinding? = null
    private var onDialog: OnListener? = null
    var list:ArrayList<VideoCategoryListModel>? = null
    var qualityList : ArrayList<TrailerModel>? = null

    interface OnListener {
        fun accept(quality : String)

    }


    fun setListner(onSuccess: OnListener) {
        this.onDialog = onSuccess
    }

    companion object {

        fun newInstance(activity: BaseActivity,list:ArrayList<VideoCategoryListModel>, qualityList : ArrayList<TrailerModel>?) = LanguageDialog().apply {
            this.activity = activity
            this.list = list
            this.qualityList = qualityList

        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawableResource(R.drawable.background_dialog)
            dialog.setCanceledOnTouchOutside(true)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.language_dialog, container, false))

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(qualityList.isNullOrEmpty()){
            binding!!.title.text = getText(R.string.content_language)
        }else{
            binding!!.title.text = getText(R.string.video_quality)
        }

        val languageAdapter = LanguageAdapter(activity!!,list!!,qualityList)
        binding!!.verticalRecycler.adapter = languageAdapter
        languageAdapter.setListner(object  : LanguageAdapter.OnListener{
            override fun accept(type: String) {
                if(onDialog != null){
                    onDialog!!.accept(type)
                }


            }

        })


    }

}
