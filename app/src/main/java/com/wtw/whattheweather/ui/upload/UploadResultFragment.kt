package com.wtw.whattheweather.ui.upload

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentUploadResultBinding

class UploadResultFragment : Fragment() {

    private lateinit var binding : FragmentUploadResultBinding
    private lateinit var viewModel : UploadViewModel
    private lateinit var mContext : Context

    private val args: UploadResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_upload_result,container,false)
        viewModel = ViewModelProvider(this)[UploadViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadResultImageView.load(args.feedUploadArgument.feedImageUrl)
        viewModel.uploadWeatherStatus.value = args.feedUploadArgument.weatherStatus
        viewModel.uploadAddress.value = args.feedUploadArgument.address
        viewModel.uploadWeatherProb.value = args.feedUploadArgument.weatherProb

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }
}