package com.bookapp.backend.domain.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.Instant
import java.util.UUID

@JacksonXmlRootElement(localName = "user")
data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val createdAt: Instant
)
