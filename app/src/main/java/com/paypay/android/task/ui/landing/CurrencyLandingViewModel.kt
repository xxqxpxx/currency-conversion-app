package com.paypay.android.task.ui.landing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paypay.android.task.base.BaseViewModel
import com.paypay.android.task.data.helper.ComplexPreferencesImpl
import com.paypay.android.task.data.repo.CurrencyRepository
import com.paypay.android.task.data.repo.LocalCurrencyRepository
import com.paypay.android.task.data.response.CurrencyModel
import com.paypay.android.task.data.response.SearchCityResoponse
import com.paypay.android.task.network.ResultModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class CurrencyLandingViewModel @Inject constructor(private val repository: CurrencyRepository) :
    BaseViewModel() {

    var list: List<CurrencyModel> = arrayListOf()

    var originalList: List<CurrencyModel> = arrayListOf()


    private val TAG = "CurrencyLandingViewModel"

    private val _currencyDataObserver = MutableLiveData<ResultModel<List<CurrencyModel>>>()
    val currencyDataObserver: LiveData<ResultModel<List<CurrencyModel>>> = _currencyDataObserver

    private val _searchTextDataObserver = MutableLiveData<ResultModel<SearchCityResoponse>>()
    val searchTextDataObserver: LiveData<ResultModel<SearchCityResoponse>> = _searchTextDataObserver

    lateinit var baseCurrency: CurrencyModel
    var isFirstTime = true
    var timestamp:Long = 0

    var mintsNeedToUpdate = 1800000 // 30 mints

    private lateinit var localCurrencyRepository: LocalCurrencyRepository




    private fun getDataOrUpdate(){
        if (timestamp.compareTo(0) == 0)
            fetchCurrencyList()
        else{
            if (timestamp + (mintsNeedToUpdate) < getCurrentSystemTimeStamp() ){
                getLocalCurrency()
            }else{
                fetchCurrencyList()
            }
        }
    }

    private fun getLocalCurrency() {

        if (list.isNullOrEmpty()) {

            _currencyDataObserver.postValue(ResultModel.Loading(isLoading = true))
            viewModelScope.launch {

                var rateslist = localCurrencyRepository.getCurrencyRatesList()

                if (rateslist.isNotEmpty()) {

                    list = rateslist
                    originalList = rateslist

                    baseCurrency = list.find {
                        it.id == "USD"
                    } ?: CurrencyModel("USD", "United States Dollar", 1.0)

                    _currencyDataObserver.postValue(ResultModel.Success(data = list))

                } else {
                    _currencyDataObserver.value = ResultModel.Failure(code = (-1))
                    _currencyDataObserver.postValue(ResultModel.Loading(isLoading = false))
                }
            }
        }
    }

    private fun getCurrentSystemTimeStamp(): Long {
        val date = Date()
        return date.time
    }


    private fun getSortedList(response: List<CurrencyModel>): List<CurrencyModel> {
        return response.sortedBy { it.id }
    }

    private fun fetchCurrencyList() {
         _currencyDataObserver.postValue(ResultModel.Loading(isLoading = true))
        viewModelScope.launch {
            repository.fetchCurrencyList()
                .catch { exception ->
                    Log.i(TAG, "Exception : ${exception.message}")
                    _currencyDataObserver.value = ResultModel.Failure(code = getStatusCode(throwable = exception))
                    _currencyDataObserver.postValue(ResultModel.Loading(isLoading = false))
                }
                .collect { response ->
                    Log.i(TAG, "Response : $response")

                    list  = getSortedList(response)
                    originalList = list
                    timestamp = repository.getTimeStamp()
                    saveToLocalStorage(list , timestamp )
                   baseCurrency = list.find {
                       it.id == "USD"
                   }?: CurrencyModel("USD" , "United States Dollar" , 1.0)

                    _currencyDataObserver.postValue(ResultModel.Success(data = list))
                }
        }
    }

    fun searchForCity(city: String) {
         _searchTextDataObserver.postValue(ResultModel.Loading(isLoading = true))
        viewModelScope.launch {
            repository.searchForCity(city = city)
                .catch { exception ->
                    Log.i(TAG, "Exception : ${exception.message}")
                    _searchTextDataObserver.value = ResultModel.Failure(code = getStatusCode(throwable = exception))
                    _searchTextDataObserver.postValue(ResultModel.Loading(isLoading = false))
                }
                .collect { response ->
                    Log.i(TAG, "Response : $response")
                    _searchTextDataObserver.postValue(ResultModel.Success(data = response))
                }
        }
    }


    fun refresh() {
        fetchCurrencyList()
    }

    fun convertToCurrency(position: Int, amount: String) {

        _currencyDataObserver.postValue(ResultModel.Loading(isLoading = true))

        val currentBaseCurrency = list[position]
        baseCurrency = currentBaseCurrency

        changeListRateForBase(amount)

    }


    private fun changeListRateForBase(amount: String) {

        var valueAfterConversion = convertAmountToNewCurrency(amount.toDouble() , 1.0 , baseCurrency.rate)

        list = originalList

        for (item in list){
            item.rate = valueAfterConversion * item.rate
        }

        _currencyDataObserver.postValue(ResultModel.Success(data = list))

    }

    private fun calculateRate(base: Double, rateValue : Double): Double {
        return base/rateValue
    }

    fun convertAmountToNewCurrency(amount: Double, base: Double, rateValue: Double): Double {
        return amount * calculateRate(base, rateValue)
    }


    fun setComplexPref(complexPreferences: ComplexPreferencesImpl) {
        localCurrencyRepository = LocalCurrencyRepository(complexPreferences)
    }


    private fun saveToLocalStorage(list: List<CurrencyModel>, timeStamp: Long) {
        viewModelScope.launch {
            localCurrencyRepository.saveCurrencyRatesList(ArrayList(list))
            localCurrencyRepository.saveTimeStamp(timeStamp)
        }
    }

    private fun getTimeStamp(){
        timestamp = localCurrencyRepository.getTimeStamp()
    }

    fun onResume() {
        getDataOrUpdate()
    }

    fun start() {
             getTimeStamp()
            getDataOrUpdate()
     }

}