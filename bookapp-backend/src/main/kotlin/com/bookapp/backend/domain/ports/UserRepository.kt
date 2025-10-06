package com.bookapp.backend.domain.ports

import com.bookapp.backend.domain.model.Book
import com.bookapp.backend.domain.model.User
import java.util.*

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
    fun search(q: String?, page: Int, size: Int): Pair<List<User>, Long>
    fun deleteById(id: UUID)
    fun listBooks(userId: UUID, page: Int, size: Int): Pair<List<Book>, Long>
}
