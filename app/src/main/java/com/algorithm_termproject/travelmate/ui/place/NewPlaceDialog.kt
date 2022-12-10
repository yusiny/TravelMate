package com.algorithm_termproject.travelmate.ui.place

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.FragmentNewPlaceDialogBinding
import com.google.gson.Gson

class NewPlaceDialog : DialogFragment() {
    private lateinit var binding: FragmentNewPlaceDialogBinding
    private lateinit var myDialogCallback: MyDialogCallback
    private lateinit var place: Place

    interface MyDialogCallback {
        fun add(place: Place)
    }

    fun setMyDialogCallback(myDialogCallback: MyDialogCallback) {
        this.myDialogCallback = myDialogCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val placeJson = arguments?.getString("place").toString()
        place = Gson().fromJson(placeJson, Place::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaceDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setClickListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        dialogResize(0.9f, 0.3f)
        initUI()
    }

    private fun initUI() {
        binding.newPlaceTitleTv.text = place.name
        binding.newPlaceAddressTv.text = place.address
    }

    private fun setClickListener() {
        binding.newPlaceAddTv.setOnClickListener {
            myDialogCallback.add(place)
            dismiss()
        }

        binding.newPlaceCancelTv.setOnClickListener {
            dismiss()
        }
    }

    private fun dialogResize(width: Float, height: Float) {
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay

            val size = Point()
            display.getSize(size)

            val window = this.dialog?.window
            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)
        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            val window = this.dialog?.window
            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }
}