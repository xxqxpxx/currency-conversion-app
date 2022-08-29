package com.paypay.android.task.ui.landing

import android.R
import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.paypay.android.task.base.BaseActivity
import com.paypay.android.task.data.helper.ComplexPreferencesImpl
import com.paypay.android.task.data.response.CurrencyModel
import com.paypay.android.task.data.response.SearchCityResoponse
import com.paypay.android.task.data.response.SearchCityResoponseItem
import com.paypay.android.task.databinding.ActivityCurrencyLandingBinding
import com.paypay.android.task.network.ResultModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_currency_landing.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CurrencyLandingActivity : BaseActivity<ActivityCurrencyLandingBinding>()   {

    private val viewModel: CurrencyLandingViewModel by viewModels()

    private lateinit var currencyAdapter: CurrencyAdapter

    private lateinit var searchItemsAdapter: SearchItemsAdapter

    override fun getViewBinding() = ActivityCurrencyLandingBinding.inflate(layoutInflater)


    var cityOnClickListener = (SearchItemsAdapter.OnClickListener { item ->
        handleCitySelection(item)
    })

    private lateinit var complexPreferences: ComplexPreferencesImpl



    override fun setupView() {

        complexPreferences = ComplexPreferencesImpl(this)
        viewModel.setComplexPref(complexPreferences)

        initListeners()
         initCurrencyList()

        viewModel.start()
    }


    @SuppressLint("SetTextI18n")
    private fun initListeners() {


        binding.layoutError.button.setOnClickListener {
            viewModel.refresh()
            hideErrorAndRefresh()
        }


        binding.etAmount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                updateConversionValues(true)
            }
        })

        /*      binding.icSearch.setOnClickListener{
                  handleSearchLayout(true)
              }
      */
    }


/*
    private fun handleSearchLayout(showLayout: Boolean) {
        if (showLayout){
            binding.searchLayout.searchLayout.visibility = View.VISIBLE

            binding.searchLayout.imageView2.setOnClickListener {
                handleSearchLayout(false)
            }

            binding.searchLayout.imgClose.setOnClickListener {
                handleSearchLayout(false)
            }

            binding.searchLayout.bottomLayoutClose.setOnClickListener{
                handleSearchLayout(false)
            }

            binding.searchLayout.etSearch.addTextChangedListener {

                if (! it.isNullOrEmpty()){
                    if (it.length >= 2){
                        viewModel.searchForCity(it.toString().trim())
                    }
                }

            }

        }else{
            binding.searchLayout.searchLayout.visibility = View.GONE

            if (searchItemsAdapter.itemCount > 0)
               searchItemsAdapter.submitList(emptyList())

            binding.searchLayout.etSearch.text.clear()
        }
    }
*/


    private fun hideErrorAndRefresh() {
        handleError(isError = false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun handleError(isError: Boolean) {
        if (isError) {
            binding.layoutError.layoutError.visibility = View.VISIBLE
        } else {
            binding.layoutError.layoutError.visibility = View.GONE
        }
    }


    private fun initCurrencyList() {
        currencyAdapter = CurrencyAdapter(context = this)
        val gridLayoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
         binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = currencyAdapter
    }


/*
    private fun initCitiesList(){
        searchItemsAdapter = SearchItemsAdapter(context = this , onClickListener = cityOnClickListener)
        binding.searchLayout.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.searchLayout.recyclerView.adapter = searchItemsAdapter
    }
*/


    override fun setupViewModelObservers() {
        viewModel.currencyDataObserver.observe(this, currencyDataObserver)
        viewModel.searchTextDataObserver.observe(this, searchDataObserver)
    }



    private val currencyDataObserver = Observer<ResultModel<List<CurrencyModel>>> { result ->
        lifecycleScope.launch {
            when (result) {
                is ResultModel.Loading -> {
                    handleProgress(isLoading = result.isLoading ?: false)
                }
                is ResultModel.Success -> {
                    onSuccess(data = result.data)
                }
                is ResultModel.Failure -> {
                    onFail()
                }
            }
        }
    }


    private fun handleCitySelection(item: SearchCityResoponseItem) {
   //     viewModel.fetchCurrencyList(item.name)
        //    handleSearchLayout(false)

    }


    private val searchDataObserver = Observer<ResultModel<SearchCityResoponse>> { result ->
        lifecycleScope.launch {
            when (result) {
                is ResultModel.Loading -> {
                    handleProgress(isLoading = result.isLoading ?: false)
                }
                is ResultModel.Success -> {
                    onSuccess(data = result.data)
                }
                is ResultModel.Failure -> {
                    onFail()
                }
            }
        }
    }

    private fun handleProgress(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE

    }

    @SuppressLint("SetTextI18n")
    private fun onSuccess(data: SearchCityResoponse) {
        showCitiesRecycler()
        //     initCitiesList()

        searchItemsAdapter.submitList(data)
    }

    private fun showCitiesRecycler() {
        //    binding.searchLayout.recyclerView.visibility = View.VISIBLE
        //    binding.searchLayout.bottomLayoutClose.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun onSuccess(data: List<CurrencyModel>) {
        handleProgress(isLoading = false)
        currencyAdapter.submitList(data ?: arrayListOf())
        handleSpinner(data)
    }

    private fun handleSpinner(data: List<CurrencyModel>) {

        if ( binding.spinner.adapter == null) {

            val list = ArrayList<String>()

            for (item in data)
                list.add(item.id)

            val aa = ArrayAdapter(this, R.layout.simple_spinner_item, list)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = aa


            if (viewModel.isFirstTime) {
                val spinnerPosition: Int = aa.getPosition(viewModel.baseCurrency.id)
                binding.spinner.setSelection(spinnerPosition)
                viewModel.isFirstTime = false
            } else {
                val spinnerPosition: Int = aa.getPosition(viewModel.baseCurrency.id)
                binding.spinner.setSelection(spinnerPosition)
            }


            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // use position to know the selected item

                    localposition = position

                    val amount = binding.etAmount.text.trim().toString()

                    if (amount.isNotEmpty())
                        viewModel.convertToCurrency(position, amount)
                    else if (!viewModel.list.isNullOrEmpty()) {
                        viewModel.convertToCurrency(position, "1")
                    }
                }

            }

        }
    }

    var localposition = 0

    private fun updateConversionValues(b: Boolean) {

        val amount = binding.etAmount.text.trim().toString()

        if (amount.isNotEmpty())
            viewModel.convertToCurrency(localposition, amount)
        else if (!viewModel.list.isNullOrEmpty()) {
            viewModel.convertToCurrency(localposition, "1")
        }
    }


    private fun onFail() {
        handleProgress(isLoading = false)
        handleError(isError = true)
    }


}
