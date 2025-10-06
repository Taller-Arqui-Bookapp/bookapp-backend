package com.bookapp.backend.domain.ports

import java.util.*

interface UserBookRepository {
    fun assign(userId: UUID, bookId: UUID)
    fun unassign(userId: UUID, bookId: UUID)
    fun exists(userId: UUID, bookId: UUID): Boolean
}
