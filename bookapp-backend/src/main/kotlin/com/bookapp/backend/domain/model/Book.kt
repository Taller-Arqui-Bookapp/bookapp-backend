package com.bookapp.backend.domain.model

import java.time.Instant
import java.util.UUID

data class Book(
    val id: UUID,
    val title: String,
    val author: String,
    val isbn: String,
    val publishedYear: Int?,
    val createdAt: Instant
)
