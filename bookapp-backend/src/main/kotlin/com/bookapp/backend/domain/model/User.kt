package com.bookapp.backend.domain.model

import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val createdAt: Instant
)
