package com.bookapp.backend.adapters.out.jpa.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity @Table(name = "books")
class BookEntity(
    @Id var id: UUID,
    var title: String,
    var author: String,
    @Column(unique = true) var isbn: String,
    @Column(name = "published_year") var publishedYear: Int?,
    @Column(name = "created_at") var createdAt: Instant,
    @Column(name = "cover_url") var coverUrl: String? = null

)
