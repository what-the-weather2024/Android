package com.wtw.whattheweather.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentHomeBinding
import com.wtw.whattheweather.model.FeedItemData
import com.wtw.whattheweather.network.RetrofitBuilder.networkService
import com.wtw.whattheweather.util.HomeFeedRecyclerAdapter
import kotlinx.coroutines.launch
import okhttp3.internal.filterList

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var mContext : Context

    private lateinit var feedList : List<FeedItemData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getFeedList()

//        initRecyclerView()
    }

    private fun getFeedList() {
        lifecycleScope.launch {
            val list = networkService.getFeedList()

            if(list.isSuccessful) {
                list.body()?.forEachIndexed { index, feedItemData ->
                    Log.e("$index",feedItemData.toString())
                }
                feedList = list.body()?.filter { it.weatherStatus != "기타" }.orEmpty()

                initRecyclerView()
            }
            else {
                Log.e("errorBody",list.errorBody().toString())

            }
        }
    }

    private fun initRecyclerView() {

        val adapter = HomeFeedRecyclerAdapter()
        binding.homeRecyclerView.layoutManager = GridLayoutManager(mContext,3)
        binding.homeRecyclerView.adapter = adapter

        adapter.submitList(feedList)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

}