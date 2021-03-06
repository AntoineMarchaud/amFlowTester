package com.amarchaud.amflowtester.model.flow.sub

import com.amarchaud.amflowtester.model.flow.ResultFlow

data class ErrorFlow(val status_code: Int = 0,
                     val status_message: String? = null) : ResultFlow(Companion.TypeResponse.ERROR)