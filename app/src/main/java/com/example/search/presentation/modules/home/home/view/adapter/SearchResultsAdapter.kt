package com.example.search.presentation.modules.home.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.data.domain.search.entities.Product
import com.example.search.R
import com.example.search.presentation.utils.setupImageUri
import com.example.search.presentation.utils.toMoneyFormat

class SearchResultsAdapter(val callBack: (selectedProduct: Product) -> Unit) :
    RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder>() {

    private val products: ArrayList<Product> = ArrayList()

    class SearchResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.title_text_view)
        val priceTextView: TextView = view.findViewById(R.id.price_text_view)
        val productImageView: ImageView = view.findViewById(R.id.product_image_view)
        val cityTextView: TextView = view.findViewById(R.id.city_text_view)
        val container: ConstraintLayout = view.findViewById(R.id.product_row_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_row_item, parent, false)
        return SearchResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.titleTextView.text = products[position].title
        holder.priceTextView.text = products[position].price?.toMoneyFormat()
        holder.cityTextView.text = products[position].cityLocation
        holder.container.setOnClickListener {
            callBack(products[position])
        }
        holder.productImageView.setupImageUri(
            holder.container.context,
            products[position].thumbnailUri
        )
    }

    fun updateData(data: List<Product>) {
        if (data !== products) {
            this.products.clear()
            this.products.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = products.size
}