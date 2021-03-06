package com.amarchaud.amflowtester.model.network.trending

data class TrendingResponse(
    var page: Int = 0,
    var results: List<MovieResult>? = null,
    var total_pages: Int = 0,
    var total_results: Int = 0
)