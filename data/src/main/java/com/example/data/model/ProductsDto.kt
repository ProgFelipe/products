package com.example.data.model

import com.example.data.domain.search.entities.Product
import com.example.data.domain.search.entities.Products
import com.squareup.moshi.Json

const val EMPTY_STRING = ""
const val EMPTY_DOUBLE = 0.0
const val EMPTY_LONG = 0L

data class ProductsDto(
    @Json(name = "site_id")
    val siteID: String = EMPTY_STRING,

    val query: String? = EMPTY_STRING,
    val paging: Paging? = null,
    val results: List<Result>? = emptyList(),

    @Json(name = "secondary_results")
    val secondaryResults: List<Any?> = emptyList(),

    @Json(name = "related_results")
    val relatedResults: List<Any?> = emptyList(),

    val sort: Sort? = null,

    @Json(name = "available_sorts")
    val availableSorts: List<Sort>? = emptyList(),

    val filters: List<Filter> = emptyList(),

    @Json(name = "available_filters")
    val availableFilters: List<AvailableFilter> = emptyList()
) {
    fun mapToDomain(): Products {
        val products = ArrayList<Product>(results?.size ?: 0)
        this.results?.forEach { result ->
            products.add(
                Product(
                    title = result.title,
                    price = result.price,
                    thumbnailUri = result.thumbnail,
                    cityLocation = result.address?.cityName,
                    attributes = result.attributes?.joinToString(
                        separator = "",
                        postfix = System.lineSeparator()
                    ) { it.name + "  :  " + it.valueName }
                )
            )
        }
        return Products(products)
    }
}

data class AvailableFilter(
    val id: String? = EMPTY_STRING,
    val name: String? = EMPTY_STRING,
    val type: String? = EMPTY_STRING,
    val values: List<AvailableFilterValue>
)

data class AvailableFilterValue(
    val id: String? = EMPTY_STRING,
    val name: String? = EMPTY_STRING,
    val results: Long? = EMPTY_LONG
)

data class Sort(
    val id: String? = EMPTY_STRING,
    val name: String? = EMPTY_STRING
)

data class Filter(
    val id: String? = EMPTY_STRING,
    val name: String? = EMPTY_STRING,
    val type: String? = EMPTY_STRING,
    val values: List<FilterValue>? = emptyList()
)

data class FilterValue(
    val id: String? = EMPTY_STRING,
    val name: String? = EMPTY_STRING,

    @Json(name = "path_from_root")
    val pathFromRoot: List<Sort>? = emptyList()
)

data class Paging(
    val total: Long? = EMPTY_LONG,

    @Json(name = "primary_results")
    val primaryResults: Long? = EMPTY_LONG,

    val offset: Long? = EMPTY_LONG,
    val limit: Long? = EMPTY_LONG
)

data class Result(
    val id: String? = EMPTY_STRING,

    @Json(name = "site_id")
    val siteID: String? = EMPTY_STRING,

    val title: String? = EMPTY_STRING,
    val seller: Seller,
    val price: Long? = EMPTY_LONG,
    val prices: Any? = null,

    @Json(name = "sale_price")
    val salePrice: Any? = null,

    @Json(name = "currency_id")
    val currencyID: String? = EMPTY_STRING,

    @Json(name = "available_quantity")
    val availableQuantity: Long? = EMPTY_LONG,

    @Json(name = "sold_quantity")
    val soldQuantity: Long? = EMPTY_LONG,

    @Json(name = "buying_mode")
    val buyingMode: String? = EMPTY_STRING,

    @Json(name = "listing_type_id")
    val listingTypeID: String? = EMPTY_STRING,

    @Json(name = "stop_time")
    val stopTime: String? = EMPTY_STRING,

    val condition: String? = EMPTY_STRING,
    val permalink: String? = EMPTY_STRING,
    val thumbnail: String? = EMPTY_STRING,

    @Json(name = "thumbnail_id")
    val thumbnailID: String? = EMPTY_STRING,

    @Json(name = "accepts_mercadopago")
    val acceptsMercadopago: Boolean,

    val installments: Installments? = null,
    val address: Address? = null,
    val shipping: Shipping? = null,

    @Json(name = "seller_address")
    val sellerAddress: SellerAddress? = null,

    val attributes: List<Attribute>? = emptyList(),

    @Json(name = "original_price")
    val originalPrice: Any? = null,

    @Json(name = "category_id")
    val categoryID: String? = EMPTY_STRING,

    @Json(name = "official_store_id")
    val officialStoreID: Any? = null,

    @Json(name = "domain_id")
    val domainID: String? = EMPTY_STRING,

    @Json(name = "catalog_product_id")
    val catalogProductID: String? = EMPTY_STRING,

    val tags: List<String>? = emptyList(),

    @Json(name = "catalog_listing")
    val catalogListing: Boolean? = null,

    @Json(name = "use_thumbnail_id")
    val useThumbnailID: Boolean,

    @Json(name = "order_backend")
    val orderBackend: Long? = EMPTY_LONG,

    @Json(name = "differential_pricing")
    val differentialPricing: DifferentialPricing? = null
)

data class Address(
    @Json(name = "state_id")
    val stateID: String? = EMPTY_STRING,

    @Json(name = "state_name")
    val stateName: String? = EMPTY_STRING,

    @Json(name = "city_id")
    val cityID: String? = EMPTY_STRING,

    @Json(name = "city_name")
    val cityName: String? = EMPTY_STRING
)

data class Attribute(
    @Json(name = "value_struct")
    val valueStruct: Any? = null,

    val values: List<AttributeValue>? = emptyList(),

    @Json(name = "attribute_group_name")
    val attributeGroupName: String? = EMPTY_STRING,

    @Json(name = "attribute_group_id")
    val attributeGroupID: String? = EMPTY_STRING,

    val source: Long? = EMPTY_LONG,
    val id: String? = EMPTY_STRING,
    val name: String? = EMPTY_STRING,

    @Json(name = "value_id")
    val valueID: String? = EMPTY_STRING,

    @Json(name = "value_name")
    val valueName: String? = EMPTY_STRING
)

data class Struct(
    val number: Double? = EMPTY_DOUBLE,
    val unit: String? = EMPTY_STRING
)

data class AttributeValue(
    val name: String? = EMPTY_STRING,
    val struct: Struct? = null,
    val source: Long? = EMPTY_LONG,
    val id: String? = EMPTY_STRING
)

data class DifferentialPricing(
    val id: Long? = EMPTY_LONG
)

data class Installments(
    val quantity: Long? = EMPTY_LONG,
    val amount: Double? = EMPTY_DOUBLE,
    val rate: Long? = EMPTY_LONG,

    @Json(name = "currency_id")
    val currencyID: String? = EMPTY_STRING
)

data class Seller(
    val id: Long? = EMPTY_LONG,
    val permalink: String? = null,

    @Json(name = "registration_date")
    val registrationDate: Any? = null,

    @Json(name = "car_dealer")
    val carDealer: Boolean,

    @Json(name = "real_estate_agency")
    val realEstateAgency: Boolean,

    val tags: Any? = null
)

data class SellerAddress(
    val id: String? = EMPTY_STRING,
    val comment: String? = EMPTY_STRING,

    @Json(name = "address_line")
    val addressLine: String? = EMPTY_STRING,

    @Json(name = "zip_code")
    val zipCode: String? = EMPTY_STRING,

    val country: Sort? = null,
    val state: Sort? = null,
    val city: Sort? = null,
    val latitude: String? = EMPTY_STRING,
    val Longitude: String? = EMPTY_STRING
)

data class Shipping(
    @Json(name = "free_shipping")
    val freeShipping: Boolean,

    val mode: String? = EMPTY_STRING,
    val tags: List<String>? = emptyList(),

    @Json(name = "logistic_type")
    val logisticType: String? = EMPTY_STRING,

    @Json(name = "store_pick_up")
    val storePickUp: Boolean
)
