package com.example.search.modules.products

import com.example.data.domain.search.entities.Product
import com.example.search.modules.utils.getProperty
import com.example.search.modules.utils.setFieldHelper
import com.example.search.presentation.modules.home.products.viewmodel.ProductDetailsViewModel
import com.example.search.presentation.utils.SingleLiveData
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductDetailsViewModelTest {

    companion object {
        private const val FIELD_PRODUCT = "product"
        private const val FIELD_PRODUCT_LIVE_DATA = "_productLiveData"
    }

    @Mock
    private lateinit var productLiveData: SingleLiveData<Product>

    private lateinit var sut: ProductDetailsViewModel

    @Before
    fun setup() {
        sut = ProductDetailsViewModel()
    }

    @Test
    fun setupArgs_invoked_setField() {
        // given
        val product = Product()
        // when
        sut.setupArgs(product)
        // then
        Assert.assertEquals(product, getProperty<Product>(sut, FIELD_PRODUCT))
    }

    @Test
    fun setupView_invoked_setValueInvoked() {
        // given
        val product = Product()
        setFieldHelper(FIELD_PRODUCT, product, sut)
        setFieldHelper(FIELD_PRODUCT_LIVE_DATA, productLiveData, sut)
        // when
        sut.setupView()
        // then
        argumentCaptor<Product> {
            verify(productLiveData).value = capture()
            Assert.assertEquals(product, allValues[0])
        }
    }
}