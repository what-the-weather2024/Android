package com.wtw.whattheweather.ui.mypage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {

    private lateinit var binding : FragmentMyPageBinding
    private lateinit var mContext : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page,container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }
}