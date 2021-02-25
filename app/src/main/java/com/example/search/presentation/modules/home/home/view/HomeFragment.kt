package com.example.search.presentation.modules.home.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.domain.search.entities.Product
import com.example.search.R
import com.example.search.presentation.components.SearchTextWatcher
import com.example.search.presentation.modules.home.home.view.adapter.SearchResultsAdapter
import com.example.search.presentation.modules.home.home.viewmodel.HomeViewModel
import com.example.search.presentation.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    companion object {
        private const val WAIT_TO_CALL_SERVICE = 2000L
    }

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupRecyclerView()
        setupViewModelObservers()
    }

    private fun setupRecyclerView() {
        recycler_view_search_results.layoutManager = LinearLayoutManager(context)
        recycler_view_search_results.adapter = SearchResultsAdapter { selectedProduct: Product ->
            viewModel.navigateToProductDetail(
                selectedProduct
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setupView()
    }

    private val textWatcher = SearchTextWatcher() {
        Observable.just(txt_input_layout_search.text)
            .debounce(WAIT_TO_CALL_SERVICE, TimeUnit.MILLISECONDS)
            .observeOn(mainThread()).subscribe {
                it?.let {
                    viewModel.searchProducts(userInputText = it.toString())
                }
            }
    }

    private fun setupListeners() {
        txt_input_layout_search.addTextChangedListener(textWatcher)
        text_input_search.setEndIconOnClickListener {
            viewModel.searchProducts(txt_input_layout_search.text.toString())
        }
    }

    private fun setupViewModelObservers() {
        viewModel.serviceStatusLiveData.observe(viewLifecycleOwner, {
            it.handleStatus(progress_indicator)
        })
        viewModel.searchValueLiveData.observe(viewLifecycleOwner, {
            txt_input_layout_search.removeTextChangedListener(textWatcher)
            txt_input_layout_search.setText(it)
            txt_input_layout_search.addTextChangedListener(textWatcher)
        })
        viewModel.productsLiveData.observe(viewLifecycleOwner, {
            (recycler_view_search_results.adapter as SearchResultsAdapter).updateData(it.products)
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