package com.wtw.whattheweather.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentSearchFeedBinding
import com.wtw.whattheweather.model.FeedItemData
import com.wtw.whattheweather.network.RetrofitBuilder.networkService
import com.wtw.whattheweather.util.HomeFeedRecyclerAdapter
import kotlinx.coroutines.launch

class SearchFeedFragment : Fragment() {

    private lateinit var binding : FragmentSearchFeedBinding
    private lateinit var viewModel : SearchViewModel
    private lateinit var mContext: Context

    private val args : SearchFeedFragmentArgs by navArgs()
    private lateinit var feedList : List<FeedItemData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_feed,container,false)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchKeyword.value = args.searchFeedArgument

        getFeedList(viewModel.searchKeyword.value!!)
        getWeatherInfo(viewModel.searchKeyword.value!!)

        binding.searchFeedBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun getFeedList(searchKeyword : String) {
        lifecycleScope.launch {
            val list = networkService.getSearchAddressFeedList("서울특별시",searchKeyword)

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

    private fun getWeatherInfo(searchKeyword: String) {

        lifecycleScope.launch {
            val response = networkService.getWeatherInfo("서울특별시",searchKeyword)

            if(response.isSuccessful) {
                val responseBody = response.body()
                viewModel.searchWeatherInfo.value = responseBody?.weatherStatus + " " + responseBody?.temperature + "'C"

            }
            else {
                Log.e("errorBody",response.errorBody().toString())

            }
        }

    }

    private fun initRecyclerView() {

        val adapter = HomeFeedRecyclerAdapter()
        binding.searchFeedRecyclerView.layoutManager = GridLayoutManager(mContext,3)
        binding.searchFeedRecyclerView.adapter = adapter

        adapter.submitList(feedList)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }
}