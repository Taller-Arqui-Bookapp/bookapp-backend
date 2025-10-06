package com.bookapp.backend.adapters.out.jpa.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant
import java.util.*

@Embeddable
data class UserBookId(var userId: UUID? = null, var bookId: UUID? = null) : Serializable

@Entity @Table(name = "user_books")
class UserBookEntity(
    @EmbeddedId var id: UserBookId,
    @Column(name = "assigned_at") var assignedAt: Instant
)
