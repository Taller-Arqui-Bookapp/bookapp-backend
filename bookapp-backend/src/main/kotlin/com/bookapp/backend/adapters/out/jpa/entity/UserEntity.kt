package com.bookapp.backend.adapters.out.jpa.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity @Table(name = "users")
class UserEntity(
    @Id var id: UUID,
    var name: String,
    @Column(unique = true) var email: String,
    @Column(name = "created_at") var createdAt: Instant
)
