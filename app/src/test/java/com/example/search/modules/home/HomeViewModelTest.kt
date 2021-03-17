package com.example.search.modules.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavDirections
import com.example.data.domain.search.ProductsUseCase
import com.example.data.domain.search.entities.Product
import com.example.data.model.EMPTY_STRING
import com.example.search.modules.utils.RxTrampolineSchedulerRule
import com.example.search.modules.utils.callPrivateFun
import com.example.search.modules.utils.setFieldHelper
import com.example.search.modules.utils.setSuperClassFieldHelper
import com.example.search.presentation.modules.home.home.view.HomeFragmentDirections
import com.example.search.presentation.modules.home.home.viewmodel.HomeViewModel
import com.example.search.presentation.utils.NetworkUtils
import com.example.search.presentation.utils.ServiceStatus
import com.example.search.presentation.utils.ServiceStatus.*
import com.example.search.presentation.utils.SingleLiveData
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    companion object {
        private const val FIELD_NAVIGATION_EVENT_LIVE_DATA = "_navigationEventLiveData"
        private const val FIELD_PRODUCTS_LIVE_DATA = "_productsLiveData"
        private const val FIELD_SERVICE_STATUS = "_serviceStatus"
        private const val FIELD_SUGGESTED_PRODUCTS_LIVE_DATA = "_suggestedProductsLiveData"
    }

    @Mock
    private lateinit var navigationEventLiveData: SingleLiveData<NavDirections>

    @Mock
    private lateinit var productsLiveData: SingleLiveData<List<Product>>

    @Mock
    private lateinit var serviceStatus: SingleLiveData<ServiceStatus>

    @Mock
    private lateinit var suggestedProductsLiveData: SingleLiveData<List<String>>

    @Mock
    private lateinit var networkUtilsMock: NetworkUtils

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxTrampolineSchedulerRule()

    @Mock
    private lateinit var productsUseCaseMock: ProductsUseCase
    private lateinit var sut: HomeViewModel

    @Before
    fun setup() {
        sut = HomeViewModel(productsUseCaseMock)
        setFieldHelper(
            FIELD_NAVIGATION_EVENT_LIVE_DATA,
            navigationEventLiveData,
            sut
        )
        setFieldHelper(
            FIELD_PRODUCTS_LIVE_DATA,
            productsLiveData,
            sut
        )
        setSuperClassFieldHelper(
            FIELD_SERVICE_STATUS,
            serviceStatus,
            sut
        )
        setFieldHelper(
            FIELD_SUGGESTED_PRODUCTS_LIVE_DATA,
            suggestedProductsLiveData,
            sut
        )
    }

    @Test
    fun searchProducts_invokedWithInput_updateProperty() {
        // given
        given(networkUtilsMock.isNetworkAvailable()).willReturn(false)
        sut.networkUtils = networkUtilsMock
        val userInputText = "someText"
        given(productsUseCaseMock.searchProducts(userInputText)).willReturn(
            Single.just(emptyList())
        )
        // when
        sut.searchProducts(userInputText)
        // then
        Assert.assertEquals(userInputText, sut.userInputValue)
    }

    @Test
    fun searchProducts_invokedWithInput_serviceReturnsSuccess() {
        // given
        val userInputText = "someText"
        val products = emptyList<Product>()

        given(networkUtilsMock.isNetworkAvailable()).willReturn(true)
        sut.networkUtils = networkUtilsMock
        given(productsUseCaseMock.searchProducts(userInputText)).willReturn(
            Single.just(products)
        )
        // when
        sut.searchProducts(userInputText)
        // then
        argumentCaptor<ServiceStatus> {
            verify(serviceStatus, times(2)).value = capture()
            Assert.assertEquals(PENDING, allValues[0])
            Assert.assertEquals(SUCCESS, allValues[1])
        }
        argumentCaptor<List<Product>> {
            verify(productsLiveData).value = capture()
            Assert.assertEquals(products, allValues[0])
        }
    }

    @Test
    fun searchProducts_invokedWithInput_serviceReturnsError() {
        // given
        val userInputText = "someText"
        val error = Throwable()

        given(networkUtilsMock.isNetworkAvailable()).willReturn(true)
        sut.networkUtils = networkUtilsMock
        given(productsUseCaseMock.searchProducts(userInputText)).willReturn(
            Single.error(error)
        )
        // when
        sut.searchProducts(userInputText)
        // then
        argumentCaptor<ServiceStatus> {
            verify(serviceStatus, times(2)).value = capture()
            Assert.assertEquals(PENDING, allValues[0])
            Assert.assertEquals(ERROR, allValues[1])
        }
        verifyZeroInteractions(productsLiveData)
    }

    @Test
    fun searchProducts_invokedWithNoInternet_noInternetStatusSet() {
        // given
        val userInputText = "someText"
        given(productsUseCaseMock.searchProducts(userInputText)).willReturn(
            Single.error(Throwable())
        )
        given(networkUtilsMock.isNetworkAvailable()).willReturn(false)
        sut.networkUtils = networkUtilsMock
        // when
        sut.searchProducts(userInputText)
        // then
        argumentCaptor<ServiceStatus> {
            verify(serviceStatus, times(2)).value = capture()
            Assert.assertEquals(PENDING, allValues[0])
            Assert.assertEquals(NO_INTERNET, allValues[1])
        }
    }

    @Test
    fun userInputChanged_invoked_returnsFalse() {
        // when
        val value = sut.callPrivateFun("userInputChanged", "") as Boolean
        // then
        Assert.assertFalse(value)
    }

    @Test
    fun userInputChanged_invoked_returnsTrue() {
        // when
        val value = sut.callPrivateFun("userInputChanged", "newValue") as Boolean
        // then
        Assert.assertTrue(value)
    }

    @Test
    fun navigateToProductDetail_invoked_navDirectionsEventInvoked() {
        // given
        val product = Product()
        // when
        sut.navigateToProductDetail(product)
        // then
        argumentCaptor<NavDirections> {
            verify(navigationEventLiveData).value = capture()
            Assert.assertEquals(
                HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product),
                allValues[0]
            )
        }
    }

    @Test
    fun onSuccess_invoked_setValueInvoked() {
        // given
        val products = emptyList<Product>()
        // when
        sut.callPrivateFun("onSuccess", products)
        // then
        argumentCaptor<List<Product>> {
            verify(productsLiveData).value = capture()
            Assert.assertEquals(products, allValues[0])
        }
    }

    @Test
    fun shouldSuggestFilter_invoked_returnFalse() {
        // when
        val result = sut.shouldSuggestFilter("c")
        // then
        Assert.assertFalse(result)
    }

    @Test
    fun shouldSuggestFilter_invoked_returnTrue() {
        // when
        val result = sut.shouldSuggestFilter("consulta")
        // then
        Assert.assertTrue(result)
    }

    @Test
    fun searchSuggestions_invoked_serviceSuccess() {
        // given
        val list = emptyList<String>()
        given(productsUseCaseMock.searchSuggestions(sut.userInputValue)).willReturn(Single.just(list))
        given(networkUtilsMock.isNetworkAvailable()).willReturn(true)
        sut.networkUtils = networkUtilsMock
        //when
        sut.searchSuggestions(sut.userInputValue)
        // then
        argumentCaptor<ServiceStatus> {
            verify(serviceStatus, times(2)).value = capture()
            Assert.assertEquals(PENDING, allValues[0])
            Assert.assertEquals(SUCCESS, allValues[1])
        }
        argumentCaptor<List<String>> {
            verify(suggestedProductsLiveData).value = capture()
            Assert.assertEquals(list, allValues[0])
        }
    }

    @Test
    fun searchSuggestions_invoked_serviceFailure() {
        // given
        val error = Throwable()
        given(productsUseCaseMock.searchSuggestions(sut.userInputValue)).willReturn(
            Single.error(
                error
            )
        )
        given(networkUtilsMock.isNetworkAvailable()).willReturn(true)
        sut.networkUtils = networkUtilsMock
        // when
        sut.searchSuggestions(EMPTY_STRING)
        // then
        argumentCaptor<ServiceStatus> {
            verify(serviceStatus, times(2)).value = capture()
            Assert.assertEquals(PENDING, allValues[0])
            Assert.assertEquals(ERROR, allValues[1])
        }
    }

    @Test
    fun onSearchSuggestionSuccess_invoked_setSuggestedProductsValue() {
        // given
        val list = emptyList<List<String>>()
        // when
        sut.callPrivateFun("onSearchSuggestionSuccess", list)
        // then
        argumentCaptor<List<String>> {
            verify(suggestedProductsLiveData).value = capture()
            Assert.assertEquals(list, allValues[0])
        }
    }
}