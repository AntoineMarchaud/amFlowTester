package com.amarchaud.amflowtester.model.flow.sub

import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.network.detail.DetailResponse

class DetailApiFlow(val details: DetailResponse?) : ResultFlow(Companion.TypeResponse.OK)