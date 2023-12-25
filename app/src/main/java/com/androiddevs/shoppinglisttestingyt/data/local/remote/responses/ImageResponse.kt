package com.androiddevs.shoppinglisttestingyt.data.remote.responses

import com.androiddevs.shoppinglisttestingyt.data.local.remote.responses.ImageResult


data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)