package com.example.rgr.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.rgr.R
import com.example.rgr.databinding.DialogImageFullscreenBinding

class ImageFullscreenDialog : DialogFragment() {

    private var _binding: DialogImageFullscreenBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_IMAGE_URL = "image_url"

        fun newInstance(imageUrl: String?): ImageFullscreenDialog {
            return ImageFullscreenDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogImageFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = arguments?.getString(ARG_IMAGE_URL)

        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_launcher_background)
            .skipMemoryCache(false)
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
            .into(binding.fullscreenImage)

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.fullscreenImage.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

