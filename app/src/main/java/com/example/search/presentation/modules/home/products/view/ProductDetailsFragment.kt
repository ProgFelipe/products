package com.example.search.presentation.modules.home.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.data.domain.search.entities.Product
import com.example.search.R
import com.example.search.presentation.modules.home.products.viewmodel.ProductDetailsViewModel
import com.example.search.presentation.utils.setupImageUri
import com.example.search.presentation.utils.toMoneyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_product_detail.*

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val args by navArgs<ProductDetailFragmentArgs>()
    private val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setupArgs(args.product)
        setupViewModelObservers()
        viewModel.setupView()
    }

    private fun setupViewModelObservers() {
        viewModel.productLiveData.observe(viewLifecycleOwner, {
            setupView(it)
        })
    }

    private fun setupView(product: Product) {
        product.apply {
            product_image_view.setupImageUri(requireContext(), thumbnailUri)
            title_text_view.text = title
            price_text_view.text = price?.toMoneyFormat()
            city_text_view.text = cityLocation
            attributes_text_view.text = attributes
        }
    }
}