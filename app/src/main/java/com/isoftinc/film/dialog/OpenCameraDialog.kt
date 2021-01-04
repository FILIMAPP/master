package com.isoftinc.film.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.isoftinc.film.R
import com.isoftinc.film.util.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class OpenCameraDialog : BottomSheetDialogFragment() {


    private var activity: BaseActivity? = null
    private var isChat: Boolean = false

    private var listener: CustomItemListener? = null

    interface CustomItemListener {

        fun onTakeVideoClicked()

        fun onTakeTextViewClicked()

        fun onUploadTextViewClicked()

    }

    fun setListener(listener: CustomItemListener) {
        this.listener = listener
    }

    fun setActvity(actvity: BaseActivity, isChat: Boolean) {
        this.activity = actvity
        this.isChat = isChat
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the spinner_textview for this fragment
        return inflater.inflate(R.layout.open_camera_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraButton = view.findViewById(R.id.cameraButton) as ImageButton
        val galleryButton = view.findViewById(R.id.galleryButton) as ImageButton
        val cameraVideoButton = view.findViewById(R.id.cameraVideoButton) as ImageButton
        val viewVideo = view.findViewById(R.id.CameraVideoView) as View

        if(isChat){
            cameraVideoButton?.visibility = View.VISIBLE
            viewVideo?.visibility = View.VISIBLE
        }
        cameraButton.setOnClickListener { view1 ->
            if (listener != null)
                listener!!.onTakeTextViewClicked()
        }
        cameraVideoButton.setOnClickListener { view1 ->
            if (listener != null)
                listener!!.onTakeVideoClicked()
        }

        galleryButton.setOnClickListener { view12 ->
            if (listener != null)
                listener!!.onUploadTextViewClicked()
        }

    }
}