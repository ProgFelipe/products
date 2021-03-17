package com.example.data.repositories.domain.search

import com.example.data.domain.search.ProductsRepository
import com.example.data.domain.search.ProductsUseCase
import com.nhaarman.mockitokotlin2.given
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductsUseCaseTest {

    companion object {
        private const val searchValue = ""
    }

    @Mock
    private lateinit var productsRepositoryMock: ProductsRepository
    private lateinit var sut: ProductsUseCase

    @Before
    fun setup() {
        sut = ProductsUseCase(productsRepositoryMock)
    }

    @Test
    fun searchProducts_invoked_returnSuccess() {
        // give
        given(productsRepositoryMock.searchProducts(searchValue)).willReturn(
            Single.just(emptyList())
        )
        // when
        val single = sut.searchProducts(searchValue)
        // then
        single.test().assertComplete()
    }

    @Test
    fun searchProducts_invoked_returnsFailure() {
        // give
        val error = Throwable()
        given(productsRepositoryMock.searchProducts(searchValue)).willReturn(
            Single.error(error)
        )
        // when
        val single = sut.searchProducts(searchValue)
        // then
        single.test().assertError(error)
    }

    @Test
    fun searchSuggestions_invoked_returnSuccess() {
        // given
        val result = emptyList<String>()
        given(productsRepositoryMock.searchSuggestions(searchValue)).willReturn(Single.just(result))
        // when
        val single = sut.searchSuggestions(searchValue)
        // then
        single.test().assertComplete()
    }

    @Test
    fun searchSuggestions_invoked_returnFailure() {
        // give
        val error = Throwable()
        given(productsRepositoryMock.searchSuggestions(searchValue)).willReturn(
            Single.error(error)
        )
        // when
        val single = sut.searchSuggestions(searchValue)
        // then
        single.test().assertError(error)
    }
}