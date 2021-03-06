package com.amarchaud.amflowtester.model.flow.sub

import com.amarchaud.amflowtester.model.entity.MovieEntity
import com.amarchaud.amflowtester.model.flow.ResultFlow

class MovieEntityFlow(val movies: List<MovieEntity>?) : ResultFlow(Companion.TypeResponse.OK)