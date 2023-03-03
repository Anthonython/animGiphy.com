package com.example.giphycom.api

data class GiphyResponse(
    val data: List<Gif>
)

data class Gif(
    val id: String,
    val title: String,
    val images: Images
)

data class Images(
    val fixed_height: Image,
    val fixed_height_still: Image,
    val fixed_height_downsampled: Image,
    val fixed_width: Image,
    val fixed_width_still: Image,
    val fixed_width_downsampled: Image,
    val original: Image,
    val original_still: Image
)

data class Image(
    val url: String,
    val width: String,
    val height: String
)

