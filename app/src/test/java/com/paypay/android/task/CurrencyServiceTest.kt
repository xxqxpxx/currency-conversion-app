package com.paypay.android.task

import android.util.Log
import com.paypay.android.task.data.repo.CurrencyRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class CurrencyServiceTest {
    private val TAG = "CurrencyServiceTest"

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: CurrencyRepository

    @Before
    fun init() {
        hiltAndroidRule.inject()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testResponseCode() = runTest {
        repository.fetchCurrencyList()
            .catch { exception ->
                Log.i(TAG, "Exception : ${exception.message}")
                assert(false)
            }
            .collect { response ->
                Log.i(TAG, "Response : $response")
           //     assert(response.AED != null)
            }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testResponseCodeForSearch() = runTest {
        repository.searchForCity("Ams")
            .catch { exception ->
                Log.i(TAG, "Exception : ${exception.message}")
                assert(false)
            }
            .collect { response ->
                Log.i(TAG, "Response : $response")
                assert(response[0].name != null)
            }
    }
}
