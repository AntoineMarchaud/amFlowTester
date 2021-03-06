package com.amarchaud.amflowtester.model.flow

open class ResultFlow(var typeResponse: TypeResponse) {

    companion object {
        enum class TypeResponse {
            OK, LOADING, ERROR
        }
    }
}