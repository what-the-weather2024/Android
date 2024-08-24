package com.wtw.whattheweather.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedItemData(
    var feedImageUrl : String?,
    var weatherStatus : String,
    var address : String?,
    var createdAt : String?,
    var memberId : String?,
    var id : Long?,
    var weatherProb : String?
) : Parcelable
