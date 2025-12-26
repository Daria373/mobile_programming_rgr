package com.example.rgr.data.remote.model

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("items") val items: List<BookDto>
)