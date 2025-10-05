package com.bookapp.backend.infrastructure

data class ApiError(
    val status: Int,
    val code: String? = null,
    val message: String,
    val traceId: String? = null
)
