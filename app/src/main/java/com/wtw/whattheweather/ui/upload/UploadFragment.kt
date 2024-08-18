package com.wtw.whattheweather.ui.upload

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentUploadBinding

class UploadFragment : Fragment() {

    private lateinit var binding : FragmentUploadBinding
    private lateinit var mContext : Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload, container, false)



        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }
}