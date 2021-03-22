package com.squarespace.android.interview.api


data class GetPhotosJsonResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<Result>?
) {

    data class Result(
        val id: String,
        val description: String?,
        val alt_description: String?,
        val urls: Urls
    ) {
        data class Urls(
            val thumb: String,
            val full: String
        )
    }
}