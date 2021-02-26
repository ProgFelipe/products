package com.example.data.repositories

import com.example.data.domain.search.ProductsRepository
import com.example.data.model.ProductsDto
import com.example.data.network.ProductApi
import com.nhaarman.mockitokotlin2.given
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductsRepositoryTest {

    companion object {
        private const val searchValue = ""
        private const val limit = 0
        private const val apiVersion = 0
    }

    @Mock
    private lateinit var productApiMock: ProductApi
    private lateinit var sut: ProductsRepository

    @Before
    fun setup() {
        sut = ProductsRepositoryImpl(productApiMock)
    }

    @Test
    fun searchProducts_invoked_returnSuccess() {
        // given
        given(productApiMock.searchProducts(searchValue, limit, apiVersion)).willReturn(
            Single.just(ProductsDto())
        )
        // when
        val single = sut.searchProducts(searchValue, limit, apiVersion)
        // then
        single.test().assertComplete()
    }

    @Test
    fun searchProducts_invoked_returnsFailure() {
        // given
        val error = Throwable()
        given(productApiMock.searchProducts(searchValue, limit, apiVersion)).willReturn(
            Single.error(error)
        )
        // when
        val single = sut.searchProducts(searchValue, limit, apiVersion)
        // then
        single.test().assertError(error)
    }
}