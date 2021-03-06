package com.amarchaud.amflowtester.model.network.trending

data class MovieResult(
    var original_title: String? = null,
    var poster_path: String? = null,
    var video: Boolean = false,
    var vote_average: Double = 0.0,
    var overview: String? = null,
    var release_date: String? = null,
    var vote_count: Int = 0,
    var id: Int = 0,
    var backdrop_path: String? = null,
    var title: String? = null,
    var genre_ids: List<Int>? = null,
    var adult: Boolean = false,
    var original_language: String? = null,
    var popularity: Double = 0.0,
    var media_type: String? = null
)