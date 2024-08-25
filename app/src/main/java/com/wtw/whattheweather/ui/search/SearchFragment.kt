package com.wtw.whattheweather.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel : SearchViewModel
    private lateinit var mContext : Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBtn.setOnClickListener {

            if(!viewModel.searchKeyword.value.isNullOrBlank()) {

                this@SearchFragment.findNavController().navigate(SearchFragmentDirections.actionNavigationSearchToNavigationSearchFeed(searchFeedArgument = viewModel.searchKeyword.value!!.toString()))

            }

        }

    }

    override fun onResume() {
        super.onResume()

        val cityArray = resources.getStringArray(R.array.city_array)
        val cityArrayAdapter = ArrayAdapter(mContext, R.layout.dropdown_item_layout, cityArray)
        binding.selectCityAutoCompleteTextView.setAdapter(cityArrayAdapter)

        val districtArray = resources.getStringArray(R.array.district_array)
        val districtArrayAdapter = ArrayAdapter(mContext, R.layout.dropdown_item_layout, districtArray)
        binding.selectGuAutoCompleteTextView.setAdapter(districtArrayAdapter)

        binding.selectGuAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                viewModel.searchKeyword.value = s.toString()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context

    }

}