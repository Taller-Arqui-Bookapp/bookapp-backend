package com.bookapp.backend.adapters.out.jpa.adapter

import com.bookapp.backend.adapters.out.jpa.entity.*
import com.bookapp.backend.domain.model.*

fun User.toEntity() = UserEntity(id, name, email, createdAt)
fun UserEntity.toDomain() = User(id, name, email, createdAt)

fun Book.toEntity() = BookEntity(id, title, author, isbn, publishedYear, createdAt)
fun BookEntity.toDomain() = Book(id, title, author, isbn, publishedYear, createdAt)
