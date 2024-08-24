package com.wtw.whattheweather.ui.home

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.ActivityFeedDetailInfoBinding
import com.wtw.whattheweather.model.FeedItemData

class FeedDetailInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFeedDetailInfoBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        init()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data", FeedItemData::class.java)
        } else {
            intent.getParcelableExtra("data")
        }

        data?.let {
            binding.feedWeatherImg.load(data.feedImageUrl)
            viewModel.feedDetailWeatherStatus.value = "날씨 : ${it.weatherStatus}"
            viewModel.feedDetailCreatedAt.value = "촬영 시간 : ${it.createdAt}"
            viewModel.feedDetailAddress.value = it.address
        }
    }

    private fun init() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_detail_info)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }
}