package com.example.search.modules.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavDirections
import com.example.data.domain.search.ProductsUseCase
import com.example.data.domain.search.entities.Product
import com.example.data.domain.search.entities.Products
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
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
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
    }

    @Mock
    private lateinit var navigationEventLiveData: SingleLiveData<NavDirections>

    @Mock
    private lateinit var productsLiveData: SingleLiveData<Products>

    @Mock
    private lateinit var serviceStatus: SingleLiveData<ServiceStatus>

    @Mock
    private lateinit var networkUtilsMock: NetworkUtils

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

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
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun searchProducts_invokedWithInput_updateProperty() {
        // given
        given(networkUtilsMock.isNetworkAvailable()).willReturn(false)
        sut.networkUtils = networkUtilsMock
        val userInputText = "someText"
        given(productsUseCaseMock.searchProducts(userInputText)).willReturn(
            Single.just(Products(emptyList()))
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
        val products = Products(emptyList())

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
        argumentCaptor<Products> {
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
        val products = Products(emptyList())
        // when
        sut.callPrivateFun("onSuccess", products)
        // then
        argumentCaptor<Products> {
            verify(productsLiveData).value = capture()
            Assert.assertEquals(products, allValues[0])
        }
    }
}