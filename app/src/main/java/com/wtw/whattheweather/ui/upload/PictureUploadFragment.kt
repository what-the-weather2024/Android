package com.wtw.whattheweather.ui.upload

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentPictureUploadBinding

class PictureUploadFragment : Fragment() {

    private lateinit var binding : FragmentPictureUploadBinding
    private lateinit var mContext : Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_picture_upload, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageUploadFrame.setOnClickListener {




        }

        binding.pictureUploadBtn.setOnClickListener {




        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }
}