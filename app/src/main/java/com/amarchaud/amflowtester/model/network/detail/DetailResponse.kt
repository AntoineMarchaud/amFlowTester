package com.amarchaud.amflowtester.model.network.detail

data class DetailResponse(
    var adult: Boolean = false,
    var backdrop_path: String? = null,
    var belongs_to_collection: BelongsToCollection? = null,
    var budget: Int = 0,
    var genres: List<Genre>? = null,
    var homepage: String? = null,
    var id: Int = 0,
    var imdb_id: String? = null,
    var original_language: String? = null,
    var original_title: String? = null,
    var overview: String? = null,
    var popularity: Double = 0.0,
    var poster_path: String? = null,
    var production_companies: List<ProductionCompany>? = null,
    var production_countries: List<ProductionCountry>? = null,
    var release_date: String? = null,
    var revenue: Int = 0,
    var runtime: Int = 0,
    var spoken_languages: List<SpokenLanguage>? = null,
    var status: String? = null,
    var tagline: String? = null,
    var title: String? = null,
    var video: Boolean = false,
    var vote_average: Double = 0.0,
    var vote_count: Int = 0
)