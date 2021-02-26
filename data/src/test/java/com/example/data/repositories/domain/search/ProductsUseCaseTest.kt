package com.example.data.repositories.domain.search

import com.example.data.domain.search.ProductsRepository
import com.example.data.domain.search.ProductsUseCase
import com.example.data.domain.search.entities.Products
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
            Single.just(Products(emptyList()))
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
}