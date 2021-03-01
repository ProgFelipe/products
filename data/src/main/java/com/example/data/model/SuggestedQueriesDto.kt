package com.example.data.model

import com.squareup.moshi.Json

data class SuggestedQueriesDto(
    @Json(name = "suggested_queries")
    val suggestedQueries: List<SuggestedQuery>
) {
    fun mapToDomain(): List<String> {
        return suggestedQueries.map { it.q }.toList()
    }
}

data class SuggestedQuery(
    val q: String,

    @Json(name = "match_start")
    val matchStart: Long,

    @Json(name = "match_end")
    val matchEnd: Long
)