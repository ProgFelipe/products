package com.example.search.presentation.modules.home.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.domain.search.entities.Product
import com.example.search.R
import com.example.search.presentation.components.CustomSearchView
import com.example.search.presentation.modules.home.home.view.adapter.SearchResultsAdapter
import com.example.search.presentation.modules.home.home.viewmodel.HomeViewModel
import com.example.search.presentation.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCustomSearchView()
        setupListeners()
        setupViewModelObservers()
    }

    private fun setupCustomSearchView() {
        (search_view_products as CustomSearchView).setupView(
            inputText = viewModel.userInputValue,
            shouldSuggest = { inputText -> viewModel.shouldSuggestFilter(inputText) },
            hideKeyBoard = { hideKeyboard() },
            onSuggestionsSearch = { inputText -> viewModel.searchSuggestions(inputText) },
            onSearch = { inputText -> viewModel.searchProducts(inputText) }
        )
    }

    private fun setupRecyclerView() {
        recycler_view_search_results.layoutManager = LinearLayoutManager(context)
        recycler_view_search_results.adapter = SearchResultsAdapter { selectedProduct: Product ->
            viewModel.userInputValue = search_view_products.query.toString()
            viewModel.navigateToProductDetail(
                selectedProduct
            )
        }
    }

    private fun setupListeners() {
        image_button_search.setOnClickListener {
            viewModel.searchProducts(search_view_products.query.toString())
        }
    }

    private fun setupViewModelObservers() {
        viewModel.serviceStatusLiveData.observe(viewLifecycleOwner, {
            it.handleStatus(constraint_layout_home, progress_indicator)
        })
        viewModel.productsLiveData.observe(viewLifecycleOwner, {
            it.isEmpty().let { isEmpty ->
                image_view_empty_state.isVisible = isEmpty
                empty_state_text_view.isVisible = isEmpty
                recycler_view_search_results.isVisible = isEmpty.not()
            }
            (recycler_view_search_results.adapter as SearchResultsAdapter).updateData(it)
        })
        viewModel.suggestedProductsLiveData.observe(viewLifecycleOwner, {
            search_view_products.setSuggestedList(it)
        })
        viewModel.navigationEventLiveData.observe(viewLifecycleOwner, {
            findNavController().navigate(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unsubscribe()
    }
}