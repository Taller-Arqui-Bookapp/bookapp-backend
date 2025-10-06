package com.bookapp.backend.domain.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.Instant
import java.util.UUID

@JacksonXmlRootElement(localName = "book")
data class Book(
    val id: UUID,
    val title: String,
    val author: String,
    val isbn: String,
    val publishedYear: Int?,
    val createdAt: Instant,
    val coverUrl: String? = null
)

