package com.bookapp.backend.adapters.out.jpa.repo

import com.bookapp.backend.adapters.out.jpa.entity.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserJpa : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserEntity?
    fun findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        name: String, email: String, pageable: Pageable
    ): Page<UserEntity>
}

interface BookJpa : JpaRepository<BookEntity, UUID> {
    fun findByIsbn(isbn: String): BookEntity?
    fun findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
        title: String, author: String, pageable: Pageable
    ): Page<BookEntity>
    fun findByIsbnContaining(isbn: String, pageable: Pageable): Page<BookEntity>
}

interface UserBookJpa : JpaRepository<UserBookEntity, UserBookId> {
    fun findAllByIdUserId(userId: UUID, pageable: Pageable): Page<UserBookEntity>
    fun findAllByIdBookId(bookId: UUID, pageable: Pageable): Page<UserBookEntity>
}
